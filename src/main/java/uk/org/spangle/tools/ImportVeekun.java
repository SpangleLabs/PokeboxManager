package uk.org.spangle.tools;

import javafx.util.Pair;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import uk.org.spangle.data.Pokemon;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Quick and easy tool to import list of pokemon and forms from veekun data
 */
public class ImportVeekun {
    Session dbSession;
    public final static List<Integer> listGenderless = Arrays.asList(81,82,100,101,120,121,137,233,292,337,338,343,344,374,375,376,436,437,462,474,479,489,490,599,600,601,615,622,623,703);
    public final static List<Integer> listUnbreedable = Arrays.asList(132,144,145,146,150,151,201,243,244,245,249,250,251,377,378,379,382,383,384,385,386,480,481,482,483,484,486,487,491,492,493,494,638,639,640,643,644,646,647,648,649,716,717,718,719,720,721);

    public static void main(String[] args) {
        ImportVeekun imp = new ImportVeekun();
        imp.run();
    }

    public void run() {
        // Create connection
        SessionFactory sessionFactory = new org.hibernate.cfg.Configuration().configure(getClass().getResource("/hibernate.cfg.xml")).buildSessionFactory();
        dbSession = sessionFactory.openSession();

        // Check if pokemon table is empty
        List listGens = dbSession.createCriteria(Pokemon.class).list();
        if(listGens.size() > 0) {
            System.out.println("Pokemon have already been imported.");
            dbSession.close();
            sessionFactory.close();
            return;
        }

        // Check that required resources exist
        File resultsCSV = new File(getClass().getResource("/result.csv").getFile());
        File resultsPng = new File(getClass().getResource("/result.png").getFile());
        if(!resultsCSV.exists() || !resultsPng.exists()) {
            System.out.println("Results files not found. Please run ImportIcons first.");
            return;
        }
        File veekunPokemon = new File(getClass().getResource("/pokedex/pokedex/data/csv/pokemon.csv").getFile());
        if(!veekunPokemon.exists()) {
            System.out.println("Please add veekun data to resources before running this tool.");
            return;
        }

        // Try and create stuff
        try {
            createAbilities();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Shut down cleanly
            dbSession.close();
            sessionFactory.close();
        }
    }

    private CSVParser loadCSV(String csvName) throws Exception {
        String filename = "/pokedex/pokedex/data/csv/"+csvName+".csv";
        File csvData = new File(getClass().getResource(filename).getFile());
        return CSVParser.parse(csvData, Charset.defaultCharset(), CSVFormat.RFC4180.withHeader());
    }

    private String getLanguageId(String iso639Code) throws Exception {
        CSVParser parser = loadCSV("languages");
        for (CSVRecord csvRecord : parser) {
            if(csvRecord.get("iso639").equals(iso639Code)) {
                return csvRecord.get("id");
            }
        }
        throw new IllegalArgumentException();
    }

    private String getVersionGroupId(String versionGroup) throws Exception {
        CSVParser parser = loadCSV("version_groups");
        for (CSVRecord csvRecord : parser) {
            if(csvRecord.get("identifier").equals(versionGroup)) {
                return csvRecord.get("id");
            }
        }
        throw new IllegalArgumentException();
    }

    private String getPokedexId(String pokedex) throws Exception {
        CSVParser parser = loadCSV("pokedexes");
        for (CSVRecord csvRecord : parser) {
            if(csvRecord.get("identifier").equals(pokedex)) {
                return csvRecord.get("id");
            }
        }
        throw new IllegalArgumentException();
    }

    private CSVRecord getPokemonRecordById(String pokemonId) throws Exception {
        CSVParser parser = loadCSV("pokemon");
        for (CSVRecord csvRecord : parser) {
            if(csvRecord.get("id").equals(pokemonId)) {
                return csvRecord;
            }
        }
        throw new IllegalArgumentException();
    }

    private CSVRecord getPokemonSpeciesRecordById(String speciesId) throws Exception {
        CSVParser parser = loadCSV("pokemon_species");
        for (CSVRecord csvRecord : parser) {
            if(csvRecord.get("id").equals(speciesId)) {
                return csvRecord;
            }
        }
        throw new IllegalArgumentException();
    }

    private CSVRecord getPokemonSpeciesNameRecordById(String speciesId, String languageId) throws Exception {
        CSVParser parser = loadCSV("pokemon_species_names");
        for (CSVRecord csvRecord : parser) {
            if(!csvRecord.get("pokemon_species_id").equals(speciesId)) {
                continue;
            }
            if(!csvRecord.get("local_language_id").equals(languageId)) {
                continue;
            }
            return csvRecord;
        }
        throw new IllegalArgumentException();
    }

    public Pokemon getOrMakePokemon(int nationalDex, String name) {
        // Search for pokemon by national dex
        List listPokemon = dbSession.createCriteria(Pokemon.class).add(Restrictions.eq("nationalDex",nationalDex)).list();
        if(listPokemon.size() == 0) {
            // Not found it, make it.
            Pokemon mon = new Pokemon(nationalDex,name);
            mon.setIsGenderless(listGenderless.contains(nationalDex) || listUnbreedable.contains(nationalDex));
            dbSession.save(mon);
            return mon;
        }
        return (Pokemon) listPokemon.get(0);
    }

    public void createAbilities() throws Exception {
        // Load languages table
        String languageId = getLanguageId("en");
        /*String versionGroupId = getVersionGroupId("omega-ruby-alpha-sapphire");
        String nationalDexId = getPokedexId("national");/**/

        // Load pokemon list
        CSVParser parser = loadCSV("pokemon_species");
        //Map<String,Ability> abilityMap = new HashMap<>();
        for (CSVRecord csvRecord : parser) {
            int nationalDex = Integer.parseInt(csvRecord.get("id"));
            String pokemonName = getPokemonSpeciesNameRecordById(Integer.toString(nationalDex),languageId).get("name");
            boolean isGenderless = false;
            if(listGenderless.contains(nationalDex) || listUnbreedable.contains(nationalDex)) isGenderless = true;
            Pokemon pokemon = new Pokemon(nationalDex,pokemonName);
            pokemon.setIsGenderless(isGenderless);
            dbSession.save(pokemon);
            System.out.println(pokemon);
        }
    }
}

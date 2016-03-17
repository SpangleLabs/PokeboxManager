package uk.org.spangle.tools;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import uk.org.spangle.data.Ability;
import uk.org.spangle.data.AbilitySlot;
import uk.org.spangle.data.Pokemon;
import uk.org.spangle.data.PokemonForm;
import uk.org.spangle.model.Configuration;

import java.io.File;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Quick and easy tool to import the list of pokeballs.
 * Hardcoded in from information from serebii list
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

        // Check if abilities table is empty
        List listGens = dbSession.createCriteria(Pokemon.class).list();
        if(listGens.size() > 0) {
            System.out.println("Pokemon have already been imported.");
            dbSession.close();
            sessionFactory.close();
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
        return null;
    }

    private String getVersionGroupId(String versionGroup) throws Exception {
        CSVParser parser = loadCSV("version_groups");
        for (CSVRecord csvRecord : parser) {
            if(csvRecord.get("identifier").equals(versionGroup)) {
                return csvRecord.get("id");
            }
        }
        return null;
    }

    private String getPokedexId(String pokedex) throws Exception {
        CSVParser parser = loadCSV("pokedexes");
        for (CSVRecord csvRecord : parser) {
            if(csvRecord.get("identifier").equals(pokedex)) {
                return csvRecord.get("id");
            }
        }
        return null;
    }

    private CSVRecord getPokemonRecordById(String pokemonId) throws Exception {
        CSVParser parser = loadCSV("pokemon");
        for (CSVRecord csvRecord : parser) {
            if(csvRecord.get("id").equals(pokemonId)) {
                return csvRecord;
            }
        }
        return null;
    }

    private CSVRecord getPokemonSpeciesRecordById(String speciesId) throws Exception {
        CSVParser parser = loadCSV("pokemon_species");
        for (CSVRecord csvRecord : parser) {
            if(csvRecord.get("id").equals(speciesId)) {
                return csvRecord;
            }
        }
        return null;
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
        String versionGroupId = getVersionGroupId("omega-ruby-alpha-sapphire");
        String nationalDexId = getPokedexId("national");

        // Load abilities id list
        CSVParser parser = loadCSV("pokemon_forms");
        //Map<String,Ability> abilityMap = new HashMap<>();
        for (CSVRecord csvRecord : parser) {
            String formName = csvRecord.get("form_identifier");
            CSVRecord pokemonRecord = getPokemonRecordById(csvRecord.get("pokemon_id"));
            String nationalDex = pokemonRecord.get("species_id");
            CSVRecord speciesRecord = getPokemonSpeciesRecordById(nationalDex);
            String speciesIdentifier = speciesRecord.get("identifier");
            String formExt = "";
            if(!formName.equals("")) formExt = "-"+formName;
            String veekunIconImage = "/pokedex-media/pokemon/icons/"+nationalDex+formExt+".png";
            try {
                File veekun = new File(getClass().getResource(veekunIconImage).getFile());
            } catch (NullPointerException e) {
                System.out.println(veekunIconImage);
            }

            boolean isDuplicate = false;
            String pokespriteIcon = "/pokesprite/icons/pokemon/regular/"+speciesIdentifier+formExt+".png";
            try {
                File pokesprite = new File(getClass().getResource(pokespriteIcon).getFile());
            } catch (NullPointerException e) {
                pokespriteIcon = "/pokesprite/icons/pokemon/regular/duplicates/"+speciesIdentifier+formExt+".png";
                try {
                    File pokesprite = new File(getClass().getResource(pokespriteIcon).getFile());
                    isDuplicate = true;
                } catch (NullPointerException ex) {
                    System.out.println(pokespriteIcon);
                }
            }

            String pokespriteFemaleIcon = "/pokesprite/icons/pokemon/regular/female/"+speciesIdentifier+formExt+".png";
            try {
                File pokespriteFemale = new File(getClass().getResource(pokespriteFemaleIcon).getFile());
            } catch (NullPointerException e) {
                //System.out.println(pokespriteFemaleIcon);
            }

            String pokespriteShinyIcon = "/pokesprite/icons/pokemon/shiny/"+speciesIdentifier+formExt+".png";
            if(isDuplicate) {
                pokespriteShinyIcon = "/pokesprite/icons/pokemon/shiny/"+speciesIdentifier+".png";
            }
            try {
                File pokespriteShiny = new File(getClass().getResource(pokespriteShinyIcon).getFile());
            } catch (NullPointerException e) {
                System.out.println(pokespriteShinyIcon);
            }

            String pokespriteShinyFemaleIcon = "/pokesprite/icons/pokemon/shiny/female/"+speciesIdentifier+formExt+".png";
            try {
                File pokespriteShinyFemale = new File(getClass().getResource(pokespriteShinyFemaleIcon).getFile());
            } catch (NullPointerException e) {
                //System.out.println(pokespriteShinyFemaleIcon);
            }


            /*System.out.println(csvRecord.get("form_identifier"));
            System.out.println(csvRecord.get("pokemon_id"));
            System.out.println(pokemonRecord.get("species_id"));
            System.out.println(speciesIdentifier);*/
        }
    }
}

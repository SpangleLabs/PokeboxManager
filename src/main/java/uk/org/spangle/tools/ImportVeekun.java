package uk.org.spangle.tools;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import uk.org.spangle.data.Ability;
import uk.org.spangle.data.Pokemon;
import uk.org.spangle.data.PokemonForm;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Quick and easy tool to import list of pokemon and forms from veekun data
 */
public class ImportVeekun {
    Session dbSession;
    public final static List<Integer> listGenderless = Arrays.asList(81,82,100,101,120,121,137,233,292,337,338,343,344,374,375,376,436,437,462,474,479,489,490,599,600,601,615,622,623,703);
    public final static List<Integer> listUnbreedable = Arrays.asList(132,144,145,146,150,151,201,243,244,245,249,250,251,377,378,379,382,383,384,385,386,480,481,482,483,484,486,487,491,492,493,494,638,639,640,643,644,646,647,648,649,716,717,718,719,720,721);
    String languageId;
    String versionGroupId;
    String nationalDexId;

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

        // Set up some handy variables
        try {
            languageId = getLanguageId("en");
            versionGroupId = getVersionGroupId("omega-ruby-alpha-sapphire");
            nationalDexId = getPokedexId("national");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Try and create stuff
        try {
            createAbilities();
            createPokemon();
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

    private String getAbilityNameById(String abilityId, String languageId) throws Exception {
        CSVParser parser = loadCSV("ability_names");
        for (CSVRecord csvRecord : parser) {
            if(!csvRecord.get("ability_id").equals(abilityId)) {
                continue;
            }
            if(!csvRecord.get("local_language_id").equals(languageId)) {
                continue;
            }
            return csvRecord.get("name");
        }
        throw new IllegalArgumentException();
    }

    private String getAbilityDescById(String abilityId, String languageId, String versionGroupId) throws Exception {
        CSVParser parser = loadCSV("ability_flavor_text");
        for (CSVRecord csvRecord : parser) {
            if(!csvRecord.get("ability_id").equals(abilityId)) {
                continue;
            }
            if(!csvRecord.get("version_group_id").equals(versionGroupId)) {
                continue;
            }
            if(!csvRecord.get("language_id").equals(languageId)) {
                continue;
            }
            return csvRecord.get("flavor_text");
        }
        throw new IllegalArgumentException();
    }

    private List<CSVRecord> getPokemonRecordsByPokemonSpeciesId(String speciesId) throws Exception {
        CSVParser parser = loadCSV("pokemon");
        List<CSVRecord> results = new ArrayList<>();
        for(CSVRecord record : parser) {
            if(record.get("species_id").equals(speciesId)) {
                results.add(record);
            }
        }
        return results;
    }

    private List<CSVRecord> getPokemonFormRecordsByPokemonId(String pokemonId) throws Exception {
        CSVParser parser = loadCSV("pokemon_forms");
        List<CSVRecord> results = new ArrayList<>();
        for(CSVRecord record : parser) {
            if(record.get("pokemon_id").equals(pokemonId)) {
                results.add(record);
            }
        }
        return results;
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

    private CSVRecord getFormCoordRecordByFormId(String formId) throws Exception {
        String filename = "/result.csv";
        File csvData = new File(getClass().getResource(filename).getFile());
        CSVParser parser = CSVParser.parse(csvData, Charset.defaultCharset(), CSVFormat.RFC4180.withHeader());
        for(CSVRecord record : parser) {
            if(record.get("pokemon_form_id").equals(formId)) {
                return record;
            }
        }
        throw new IllegalArgumentException();
    }

    public void createAbilities() throws Exception {
        // Load abilities list
        CSVParser parser = loadCSV("abilities");
        for (CSVRecord record : parser) {
            String abilityId = record.get("id");
            String isMain = record.get("is_main_series");
            if(isMain.equals("0")) continue;
            String abilityName = getAbilityNameById(abilityId,languageId);
            String abilityDesc = getAbilityDescById(abilityId,languageId,versionGroupId);
            Ability ability = new Ability(abilityName,abilityDesc);
            dbSession.save(ability);
        }
    }

    public void createPokemon() throws Exception {
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

            List<CSVRecord> listPokemon = getPokemonRecordsByPokemonSpeciesId(Integer.toString(nationalDex));
            for (CSVRecord pokemonRecord : listPokemon) {
                List<CSVRecord> listForms = getPokemonFormRecordsByPokemonId(pokemonRecord.get("id"));
                for (CSVRecord formRecord : listForms) {
                    String formId = formRecord.get("id");
                    String formName = formRecord.get("form_identifier");
                    if(formName.length() == 0) formName = "normal";
                    System.out.println(formName);
                    CSVRecord coordsRecord = getFormCoordRecordByFormId(formId);
                    PokemonForm pokemonForm = new PokemonForm();
                    pokemonForm.setPokemon(pokemon);
                    pokemonForm.setName(formName);
                    pokemonForm.setSpriteMaleX(Integer.parseInt(coordsRecord.get("normal-m-x")));
                    pokemonForm.setSpriteMaleY(Integer.parseInt(coordsRecord.get("normal-m-y")));
                    pokemonForm.setSpriteFemaleX(Integer.parseInt(coordsRecord.get("normal-f-x")));
                    pokemonForm.setSpriteFemaleY(Integer.parseInt(coordsRecord.get("normal-f-y")));
                    pokemonForm.setSpriteShinyMaleX(Integer.parseInt(coordsRecord.get("shiny-m-x")));
                    pokemonForm.setSpriteShinyMaleY(Integer.parseInt(coordsRecord.get("shiny-m-y")));
                    pokemonForm.setSpriteShinyFemaleX(Integer.parseInt(coordsRecord.get("shiny-f-x")));
                    pokemonForm.setSpriteShinyFemaleY(Integer.parseInt(coordsRecord.get("shiny-f-y")));
                    dbSession.save(pokemonForm);
                }
            }

            System.out.println(pokemon);
        }
    }
}

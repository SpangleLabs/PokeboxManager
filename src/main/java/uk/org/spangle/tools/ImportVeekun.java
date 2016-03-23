package uk.org.spangle.tools;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import uk.org.spangle.data.*;
import uk.org.spangle.model.Configuration;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Quick and easy tool to import list of pokemon and forms from veekun data
 */
public class ImportVeekun {
    private Session dbSession;
    private final static List<Integer> listGenderless = Arrays.asList(81,82,100,101,120,121,137,233,292,337,338,343,344,374,375,376,436,437,462,474,479,489,490,599,600,601,615,622,623,703);
    private final static List<Integer> listUnbreedable = Arrays.asList(132,144,145,146,150,151,201,243,244,245,249,250,251,377,378,379,382,383,384,385,386,480,481,482,483,484,486,487,491,492,493,494,638,639,640,643,644,646,647,648,649,716,717,718,719,720,721);
    private final static Map<String,String> mapStatAbbr = new HashMap<>();
    static {
        mapStatAbbr.put("hp","HP");
        mapStatAbbr.put("attack","Atk");
        mapStatAbbr.put("defense","Def");
        mapStatAbbr.put("special-attack","SpA");
        mapStatAbbr.put("special-defense","SpD");
        mapStatAbbr.put("speed","Spe");
    }
    private String languageId;
    private String versionGroupId;
    private Map<String,Ability> abilityMap; // Map of veekun IDs to abilities.
    private Map<String,AbilitySlot> abilitySlotMap; // Map of veekun IDs to ability slots.
    private Map<String,Move> moveMap; // Map of veekun IDs to moves.
    private Map<String,MoveMethod> moveMethodMap; // Map of veekun IDs to move methods.

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
        File resultsCSV = new File("pokemon-icons.csv");
        File resultsPng = new File("pokemon-icons.png");
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Try and create stuff
        try {
            createAbilities();
            createPokeBalls();
            createStatsAndNatures();
            createMoves();
            createPokemon();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Shut down cleanly
            dbSession.close();
            sessionFactory.close();
        }
    }

    private CSVRecord getFormCoordRecordByFormId(String formId) throws Exception {
        String filename = "pokemon-icons.csv";
        File csvData = new File(filename);
        CSVParser parser = CSVParser.parse(csvData, Charset.defaultCharset(), CSVFormat.RFC4180.withHeader());
        for(CSVRecord record : parser) {
            if(record.get("pokemon_form_id").equals(formId)) {
                return record;
            }
        }
        throw new IllegalArgumentException();
    }

    private CSVRecord getBallCoordRecordByItemId(String itemId) throws Exception {
        String filename = "pokeball-icons.csv";
        File csvData = new File(filename);
        CSVParser parser = CSVParser.parse(csvData, Charset.defaultCharset(), CSVFormat.RFC4180.withHeader());
        for(CSVRecord record : parser) {
            if(record.get("item_id").equals(itemId)) {
                return record;
            }
        }
        throw new IllegalArgumentException();
    }

    static CSVParser loadCSV(String csvName) throws Exception {
        String filename = "/pokedex/pokedex/data/csv/"+csvName+".csv";
        File csvData = new File((new ImportVeekun()).getClass().getResource(filename).getFile());
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
    
    private List<CSVRecord> getPokemonAbilityRecordsByPokemonId(String pokemonId) throws Exception {
    	CSVParser parser = loadCSV("pokemon_abilities");
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

    static CSVRecord getItemPocketRecordByIdentifier(String pocketIdentifier) throws Exception {
        CSVParser parser = loadCSV("item_pockets");
        for(CSVRecord record : parser) {
            if(record.get("identifier").equals(pocketIdentifier)) {
                return record;
            }
        }
        throw new IllegalArgumentException();
    }

    static List<CSVRecord> getItemCategoryRecordsByPocketId(String pocketId) throws Exception {
        CSVParser parser = loadCSV("item_categories");
        List<CSVRecord> results = new ArrayList<>();
        for(CSVRecord record : parser) {
            if(record.get("pocket_id").equals(pocketId)) {
                results.add(record);
            }
        }
        return results;
    }

    static List<CSVRecord> getItemRecordsByItemCategoryId(String categoryId) throws Exception {
        CSVParser parser = loadCSV("items");
        List<CSVRecord> results = new ArrayList<>();
        for(CSVRecord record : parser) {
            if(record.get("category_id").equals(categoryId)) {
                results.add(record);
            }
        }
        return results;
    }

    private CSVRecord getItemNameRecordById(String itemId, String languageId) throws Exception {
        CSVParser parser = loadCSV("item_names");
        for(CSVRecord record : parser) {
            if(!record.get("item_id").equals(itemId)) {
                continue;
            }
            if(!record.get("local_language_id").equals(languageId)) {
                continue;
            }
            return record;
        }
        throw new IllegalArgumentException();
    }

    private CSVRecord getItemDescRecordById(String itemId, String languageId, String versionGroupId) throws Exception {
        CSVParser parser = loadCSV("item_flavor_text");
        int maxVersionGroup = 0;
        CSVRecord maxVersionRecord = null;
        for(CSVRecord record : parser) {
            if(!record.get("item_id").equals(itemId)) {
                continue;
            }
            if(!record.get("language_id").equals(languageId)) {
                continue;
            }
            if(record.get("version_group_id").equals(versionGroupId)) {
                return record;
            }
            int recordVersion = Integer.parseInt(record.get("version_group_id"));
            if(recordVersion>maxVersionGroup) {
                maxVersionGroup = recordVersion;
                maxVersionRecord = record;
            }
        }
        return maxVersionRecord;
    }

    private static CSVRecord getStatNameById(String statId, String languageId) throws Exception {
        CSVParser parser = loadCSV("stat_names");
        for(CSVRecord record : parser) {
            if(!record.get("stat_id").equals(statId)) continue;
            if(!record.get("local_language_id").equals(languageId)) continue;
            return record;
        }
        throw new IllegalArgumentException();
    }

    private static CSVRecord getNatureNameById(String natureId, String languageId) throws Exception {
        CSVParser parser = loadCSV("nature_names");
        for(CSVRecord record : parser) {
            if(!record.get("nature_id").equals(natureId)) continue;
            if(!record.get("local_language_id").equals(languageId)) continue;
            return record;
        }
        throw new IllegalArgumentException();
    }

    private static CSVRecord getMoveNameById(String moveId, String languageId) throws Exception {
        CSVParser parser = loadCSV("move_names");
        for(CSVRecord record : parser) {
            if(!record.get("move_id").equals(moveId)) continue;
            if(!record.get("local_language_id").equals(languageId)) continue;
            return record;
        }
        throw new IllegalArgumentException();
    }

    private static CSVRecord getMoveDescById(String moveId, String languageId, String versionGroupId) throws Exception {
        CSVParser parser = loadCSV("move_flavor_text");
        for(CSVRecord record : parser) {
            if(!record.get("move_id").equals(moveId)) continue;
            if(!record.get("language_id").equals(languageId)) continue;
            if(!record.get("version_group_id").equals(versionGroupId)) continue;
            return record;
        }
        throw new IllegalArgumentException();
    }

    private static CSVRecord getMoveMethodProseById(String moveMethodId, String languageId) throws Exception {
        CSVParser parser = loadCSV("pokemon_move_method_prose");
        for(CSVRecord record : parser) {
            if(!record.get("pokemon_move_method_id").equals(moveMethodId)) continue;
            if(!record.get("local_language_id").equals(languageId)) continue;
            return record;
        }
        throw new IllegalArgumentException();
    }

    private static List<CSVRecord> getPokemonMoveRecordsByPokemonId(String pokemonId) throws Exception {
        CSVParser parser = loadCSV("pokemon_moves");
        List<CSVRecord> results = new ArrayList<>();
        for(CSVRecord record : parser) {
            if(record.get("pokemon_id").equals(pokemonId)) {
                results.add(record);
            }
        }
        return results;
    }

    private void createAbilities() throws Exception {
        // Create and save all AbilitySlot values
    	abilitySlotMap = new HashMap<>();
        AbilitySlot ability1 = new AbilitySlot(Configuration.ABILITY_SLOT_1);
        abilitySlotMap.put("1",ability1);
        dbSession.save(ability1);
        AbilitySlot ability2 = new AbilitySlot(Configuration.ABILITY_SLOT_2);
        abilitySlotMap.put("2",ability2);
        dbSession.save(ability2);
        AbilitySlot abilityH = new AbilitySlot(Configuration.ABILITY_SLOT_HIDDEN);
        abilitySlotMap.put("3",abilityH);
        dbSession.save(abilityH);

        // Load abilities list
        CSVParser parser = loadCSV("abilities");
        abilityMap = new HashMap<>();
        for (CSVRecord record : parser) {
            String abilityId = record.get("id");
            String isMain = record.get("is_main_series");
            if(isMain.equals("0")) continue;
            String abilityName = getAbilityNameById(abilityId,languageId);
            String abilityDesc = getAbilityDescById(abilityId,languageId,versionGroupId);
            Ability ability = new Ability(abilityName,abilityDesc);
            abilityMap.put(abilityId,ability);
            dbSession.save(ability);
        }
    }

    private void createPokeBalls() throws Exception {
        // Get pokeball pocket value
        CSVRecord pokeballRecord = getItemPocketRecordByIdentifier("pokeballs");
        // Get item categories in pocket
        List<CSVRecord> listCategories = getItemCategoryRecordsByPocketId(pokeballRecord.get("id"));
        for(CSVRecord itemCategory : listCategories) {
            // Get items in category
            List<CSVRecord> listItems = getItemRecordsByItemCategoryId(itemCategory.get("id"));
            for(CSVRecord item : listItems) {
                String pokeballName = getItemNameRecordById(item.get("id"),languageId).get("name");
                String pokeballDesc = getItemDescRecordById(item.get("id"),languageId,versionGroupId).get("flavor_text");
                CSVRecord coordRecord = getBallCoordRecordByItemId(item.get("id"));
                int x = Integer.parseInt(coordRecord.get("x"));
                int y = Integer.parseInt(coordRecord.get("y"));
                PokeBall pokeball = new PokeBall(pokeballName,pokeballDesc,x,y);
                dbSession.save(pokeball);
            }
        }
    }

    private void createPokemon() throws Exception {
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
                List<CSVRecord> listAbilities = getPokemonAbilityRecordsByPokemonId(pokemonRecord.get("id"));
                List<CSVRecord> listMoves = getPokemonMoveRecordsByPokemonId(pokemonRecord.get("id"));
                for (CSVRecord formRecord : listForms) {
                    String formId = formRecord.get("id");
                    String formName = formRecord.get("form_identifier");
                    if(formName.length() == 0) formName = "normal";
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
                    for(CSVRecord abilityRecord : listAbilities) {
                    	PokemonFormAbility pokemonAbility = new PokemonFormAbility();
                    	pokemonAbility.setPokemonForm(pokemonForm);
                    	pokemonAbility.setAbility(abilityMap.get(abilityRecord.get("ability_id")));
                    	pokemonAbility.setAbilitySlot(abilitySlotMap.get(abilityRecord.get("slot")));
                    	dbSession.save(pokemonAbility);
                    }
                    for(CSVRecord moveRecord : listMoves) {
                        Move move = moveMap.get(moveRecord.get("move_id"));
                        MoveMethod moveMethod = moveMethodMap.get(moveRecord.get("pokemon_move_method_id"));
                        PokemonFormMove pokemonMove = new PokemonFormMove(pokemonForm,move,moveMethod);
                        if(moveRecord.get("pokemon_move_method_id").equals("1")) {
                            pokemonMove.setLevel(Integer.parseInt(moveRecord.get("level")));
                            String orderStr = moveRecord.get("order");
                            Integer order = 0;
                            if(!orderStr.equals("")) order = Integer.parseInt(orderStr);
                            pokemonMove.setOrdinal(order);
                        }
                        dbSession.save(pokemonMove);
                    }
                }
            }
        }
    }

    private void createStatsAndNatures() throws Exception {
        // Load stats
        Map<String,Stat> statMap = new HashMap<>();
        CSVParser parser = loadCSV("stats");
        for(CSVRecord record : parser) {
            if(record.get("is_battle_only").equals("1")) continue;
            String statAbbr = mapStatAbbr.get(record.get("identifier"));
            String statName = getStatNameById(record.get("id"),languageId).get("name");
            Stat newStat = new Stat(statName,statAbbr);
            statMap.put(record.get("id"),newStat);
            dbSession.save(newStat);
        }

        // Load natures
        CSVParser natureParser = loadCSV("natures");
        for(CSVRecord natureRecord : natureParser) {
            String natureName = getNatureNameById(natureRecord.get("id"),languageId).get("name");
            Stat statDown = statMap.get(natureRecord.get("decreased_stat_id"));
            Stat statUp = statMap.get(natureRecord.get("increased_stat_id"));
            Nature nature = new Nature(natureName,statUp,statDown);
            dbSession.save(nature);
        }
    }

    private void createMoves() throws Exception {
        // Load moves
        CSVParser parser = loadCSV("moves");
        moveMap = new HashMap<>();
        for(CSVRecord record : parser) {
            String moveId = record.get("id");
            String moveName = getMoveNameById(moveId,languageId).get("name");
            String moveDesc;
            try {
                moveDesc = getMoveDescById(moveId, languageId, versionGroupId).get("flavor_text");
            } catch (IllegalArgumentException e) { continue; }
            Move move = new Move(moveName,moveDesc);
            moveMap.put(moveId,move);
            dbSession.save(move);
        }

        // Load move methods
        CSVParser methodParser = loadCSV("pokemon_move_methods");
        moveMethodMap = new HashMap<>();
        for(CSVRecord record : methodParser) {
            String methodId = record.get("id");
            CSVRecord proseRecord = getMoveMethodProseById(methodId,languageId);
            String methodName = proseRecord.get("name");
            String methodDesc = proseRecord.get("description");
            MoveMethod method = new MoveMethod(methodName,methodDesc);
            moveMethodMap.put(methodId,method);
            dbSession.save(method);
        }
    }
}

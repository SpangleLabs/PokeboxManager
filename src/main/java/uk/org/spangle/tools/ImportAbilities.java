package uk.org.spangle.tools;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import uk.org.spangle.data.Ability;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Quick and easy tool to import the list of pokeballs.
 * Hardcoded in from information from serebii list
 */
public class ImportAbilities {
    Session dbSession;

    public static void main(String[] args) {
        ImportAbilities imp = new ImportAbilities();
        imp.run();
    }

    public void run() {
        // Create connection
        SessionFactory sessionFactory = new org.hibernate.cfg.Configuration().configure(getClass().getResource("/hibernate.cfg.xml")).buildSessionFactory();
        dbSession = sessionFactory.openSession();

        // Check if abilities table is empty
        List listGens = dbSession.createCriteria(Ability.class).list();
        if(listGens.size() > 0) {
            System.out.println("Abilities have already been imported.");
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

    private CSVParser loadCSV(String filename) throws Exception {
        File csvData = new File(getClass().getResource(filename).getFile());
        return CSVParser.parse(csvData, Charset.defaultCharset(), CSVFormat.RFC4180.withHeader());
    }

    private String getLanguageId(String iso639Code) throws Exception {
        CSVParser parser = loadCSV("/csv/languages.csv");
        for (CSVRecord csvRecord : parser) {
            if(csvRecord.get("iso639").equals(iso639Code)) {
                return csvRecord.get("id");
            }
        }
        return null;
    }

    private String getVerisonGroupId(String versionGroup) throws Exception {
        CSVParser parser = loadCSV("/csv/version_groups.csv");
        for (CSVRecord csvRecord : parser) {
            if(csvRecord.get("identifier").equals(versionGroup)) {
                return csvRecord.get("id");
            }
        }
        return null;
    }

    public void createAbilities() throws Exception {
        // Load languages table
        String languageId = getLanguageId("en");
        String versionGroupId = getVerisonGroupId("omega-ruby-alpha-sapphire");

        // Load abilities id list
        CSVParser parser = loadCSV("/csv/abilities.csv");
        Map<String,Ability> abilityMap = new HashMap<>();
        for (CSVRecord csvRecord : parser) {
            if(csvRecord.get("is_main_series").equals("1")) {
                abilityMap.put(csvRecord.get("id"),new Ability());
            }
        }

        // Load ability names
        CSVParser nameParser = loadCSV("/csv/ability_names.csv");
        for (CSVRecord csvRecord : nameParser) {
            if(csvRecord.get("local_language_id").equals(languageId)) {
                if(abilityMap.get(csvRecord.get("ability_id"))==null) continue;
                abilityMap.get(csvRecord.get("ability_id")).setName(csvRecord.get("name"));
            }
        }

        // Load ability descriptions
        CSVParser descParser = loadCSV("/csv/ability_flavor_text.csv");
        for (CSVRecord csvRecord : descParser) {
            if(csvRecord.get("language_id").equals(languageId)) {
                if(csvRecord.get("version_group_id").equals(versionGroupId)) {
                    if(abilityMap.get(csvRecord.get("ability_id"))==null) continue;
                    abilityMap.get(csvRecord.get("ability_id")).setDescription(csvRecord.get("flavor_text"));
                }
            }
        }

        // Save all abilities
        for(Map.Entry<String,Ability> entry : abilityMap.entrySet()) {
            System.out.println(entry.getKey());
            dbSession.save(entry.getValue());
        }
    }
}

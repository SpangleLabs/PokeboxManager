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
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Quick and easy tool to import the list of pokeballs.
 * Hardcoded in from information from serebii list
 */
public class ImportIcons {
    Session dbSession;
    public final static List<Integer> listGenderless = Arrays.asList(81,82,100,101,120,121,137,233,292,337,338,343,344,374,375,376,436,437,462,474,479,489,490,599,600,601,615,622,623,703);
    public final static List<Integer> listUnbreedable = Arrays.asList(132,144,145,146,150,151,201,243,244,245,249,250,251,377,378,379,382,383,384,385,386,480,481,482,483,484,486,487,491,492,493,494,638,639,640,643,644,646,647,648,649,716,717,718,719,720,721);
    public final static Map<String,String> filenameReplace = new HashMap<>();
    static {
        filenameReplace.put("furfrou-natural.png","furfrou.png");
        filenameReplace.put("arceus-unknown.png","arceus.png");
        filenameReplace.put("pikachu-rock-star.png","pikachu-cool.png");
        filenameReplace.put("pikachu-belle.png","pikachu-beautiful.png");
        filenameReplace.put("pikachu-pop-star.png","pikachu-cute.png");
        filenameReplace.put("pikachu-phd.png","pikachu-clever.png");
        filenameReplace.put("pikachu-libre.png","pikachu-tough.png");
        filenameReplace.put("meowstic-male.png","meowstic.png");
        filenameReplace.put("meowstic-female.png","female/meowstic.png");
    }

    public static void main(String[] args) {
        ImportIcons imp = new ImportIcons();
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
        /*String languageId = getLanguageId("en");
        String versionGroupId = getVersionGroupId("omega-ruby-alpha-sapphire");
        String nationalDexId = getPokedexId("national");/**/

        // Load abilities id list
        CSVParser parser = loadCSV("pokemon_forms");
        ConcurrentHashMap<String,Pair<Integer,Integer>> fileHashes = new ConcurrentHashMap<>();
        int x = 0;
        int y = 0;
        BufferedImage bigImage = new BufferedImage(1000,2130,BufferedImage.TYPE_INT_ARGB);
        Graphics g = bigImage.getGraphics();
        String bigCSV = "\"pokemon_form_id\",\"normal-m-x\",\"normal-m-y\",\"normal-f-x\",\"normal-f-y\",\"shiny-m-x\",\"shiny-m-y\",\"shiny-f-x\",\"shiny-f-y\"\n";
        //Map<String,Ability> abilityMap = new HashMap<>();
        for (CSVRecord csvRecord : parser) {
            bigCSV += csvRecord.get("id")+",";
            String formName = csvRecord.get("form_identifier");
            CSVRecord pokemonRecord = getPokemonRecordById(csvRecord.get("pokemon_id"));
            String nationalDex = pokemonRecord.get("species_id");
            CSVRecord speciesRecord = getPokemonSpeciesRecordById(nationalDex);
            String speciesIdentifier = speciesRecord.get("identifier");
            String formExt = "";
            if(!formName.equals("")) formExt = "-"+formName;

            String fileName = speciesIdentifier+formExt+".png";
            if(filenameReplace.containsKey(fileName)) fileName = filenameReplace.get(fileName);
            boolean isDuplicate = false;
            String pokespriteIcon = "/pokesprite/icons/pokemon/regular/"+fileName;
            if(fileName.equals("pichu-spiky-eared.png")) pokespriteIcon = "/pichu-spiky-eared.png";
            File pokesprite;
            try {
                pokesprite = new File(getClass().getResource(pokespriteIcon).getFile());
            } catch (NullPointerException e) {
                pokespriteIcon = "/pokesprite/icons/pokemon/regular/duplicates/"+fileName;
                try {
                    pokesprite = new File(getClass().getResource(pokespriteIcon).getFile());
                    isDuplicate = true;
                } catch (NullPointerException ex) {
                    System.out.println("FAILED TO FIND: "+pokespriteIcon);
                    return;
                }
            }
            FileInputStream fis = new FileInputStream(pokesprite);
            String md5 = DigestUtils.md5Hex(fis);
            fis.close();
            if(fileHashes.get(md5)==null) {
                fileHashes.put(md5, new Pair<>(x,y));
                g.drawImage(ImageIO.read(pokesprite), x, y, null);
                bigCSV += x+","+y+",";
                x += 40;
                if (x == 1000) {
                    y += 30;
                    x = 0;
                }
            } else {
                int tempX = fileHashes.get(md5).getKey();
                int tempY = fileHashes.get(md5).getValue();
                bigCSV += tempX+","+tempY+",";
            }


            String pokespriteFemaleIcon = "/pokesprite/icons/pokemon/regular/female/"+fileName;
            File pokespriteFemale;
            try {
                pokespriteFemale = new File(getClass().getResource(pokespriteFemaleIcon).getFile());
            } catch (NullPointerException e) {
                pokespriteFemaleIcon = pokespriteIcon;
                pokespriteFemale = new File(getClass().getResource(pokespriteFemaleIcon).getFile());
            }
            FileInputStream fisf = new FileInputStream(pokespriteFemale);
            String md5f = DigestUtils.md5Hex(fisf);
            fisf.close();
            if(fileHashes.get(md5f)==null) {
                fileHashes.put(md5f, new Pair<>(x,y));
                g.drawImage(ImageIO.read(pokespriteFemale), x, y, null);
                bigCSV += x+","+y+",";
                x += 40;
                if (x == 1000) {
                    y += 30;
                    x = 0;
                }
            } else {
                int tempX = fileHashes.get(md5f).getKey();
                int tempY = fileHashes.get(md5f).getValue();
                bigCSV += tempX+","+tempY+",";
            }


            String pokespriteShinyIcon = "/pokesprite/icons/pokemon/shiny/"+fileName;
            if(fileName.equals("pichu-spiky-eared.png")) pokespriteShinyIcon = "/pichu-spiky-eared-shiny.png";
            if(isDuplicate) {
                pokespriteShinyIcon = "/pokesprite/icons/pokemon/shiny/"+speciesIdentifier+".png";
            }
            File pokespriteShiny;
            try {
                pokespriteShiny = new File(getClass().getResource(pokespriteShinyIcon).getFile());
            } catch (NullPointerException e) {
                System.out.println("FAILED TO FIND: "+pokespriteShinyIcon);
                return;
            }
            FileInputStream fiss = new FileInputStream(pokespriteShiny);
            String md5s = DigestUtils.md5Hex(fiss);
            fiss.close();
            if(fileHashes.get(md5s)==null) {
                fileHashes.put(md5s, new Pair<>(x,y));
                g.drawImage(ImageIO.read(pokespriteShiny), x, y, null);
                bigCSV += x+","+y+",";
                x += 40;
                if (x == 1000) {
                    y += 30;
                    x = 0;
                }
            } else {
                int tempX = fileHashes.get(md5f).getKey();
                int tempY = fileHashes.get(md5f).getValue();
                bigCSV += tempX+","+tempY+",";
            }


            String pokespriteShinyFemaleIcon = "/pokesprite/icons/pokemon/shiny/female/"+fileName;
            File pokespriteShinyFemale;
            try {
                pokespriteShinyFemale = new File(getClass().getResource(pokespriteShinyFemaleIcon).getFile());
            } catch (NullPointerException e) {
                pokespriteShinyFemaleIcon = pokespriteShinyIcon;
                pokespriteShinyFemale = new File(getClass().getResource(pokespriteShinyFemaleIcon).getFile());
            }
            FileInputStream fissf = new FileInputStream(pokespriteShinyFemale);
            String md5sf = DigestUtils.md5Hex(fissf);
            fissf.close();
            if(fileHashes.get(md5sf)==null) {
                fileHashes.put(md5sf, new Pair<>(x,y));
                g.drawImage(ImageIO.read(pokespriteShinyFemale), x, y, null);
                bigCSV += x+","+y+"\n";
                x += 40;
                if (x == 1000) {
                    y += 30;
                    x = 0;
                }
            } else {
                int tempX = fileHashes.get(md5f).getKey();
                int tempY = fileHashes.get(md5f).getValue();
                bigCSV += tempX+","+tempY+"\n";
            }


            /*System.out.println(csvRecord.get("form_identifier"));
            System.out.println(csvRecord.get("pokemon_id"));
            System.out.println(pokemonRecord.get("species_id"));
            System.out.println(speciesIdentifier);*/
        }
        System.out.println(fileHashes.size());
        ImageIO.write(bigImage,"png",new File("result.png"));
        PrintWriter out = new PrintWriter("result.csv");
        out.print(bigCSV);
        out.close();
    }
}

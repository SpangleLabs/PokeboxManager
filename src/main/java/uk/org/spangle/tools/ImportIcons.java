package uk.org.spangle.tools;

import javafx.util.Pair;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static uk.org.spangle.tools.ImportVeekun.*;

/**
 * Tool to load all pokemon forms, find the icons and merge them together into a big image and csv file
 */
public class ImportIcons {
    private final static Map<String,String> filenameReplace = new HashMap<>();
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

        // Try and create stuff
        try {
            createPokemonIcons();
            createPokeBalls();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static CSVRecord getPokemonRecordById(String pokemonId) throws Exception {
        CSVParser parser = loadCSV("pokemon");
        for (CSVRecord csvRecord : parser) {
            if(csvRecord.get("id").equals(pokemonId)) {
                return csvRecord;
            }
        }
        throw new IllegalArgumentException();
    }

    private static CSVRecord getPokemonSpeciesRecordById(String speciesId) throws Exception {
        CSVParser parser = loadCSV("pokemon_species");
        for (CSVRecord csvRecord : parser) {
            if(csvRecord.get("id").equals(speciesId)) {
                return csvRecord;
            }
        }
        throw new IllegalArgumentException();
    }

    private void createPokemonIcons() throws Exception {
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
                int tempX = fileHashes.get(md5sf).getKey();
                int tempY = fileHashes.get(md5sf).getValue();
                bigCSV += tempX+","+tempY+"\n";
            }

        }
        System.out.println(fileHashes.size());
        ImageIO.write(bigImage,"png",new File("pokemon-icons.png"));
        PrintWriter out = new PrintWriter("pokemon-icons.csv");
        out.print(bigCSV);
        out.close();
    }

    private void createPokeBalls() throws Exception {
        // Get pokeball pocket value
        CSVRecord pokeballRecord = getItemPocketRecordByIdentifier("pokeballs");
        // Get item categories in pocket
        List<CSVRecord> listCategories = getItemCategoryRecordsByPocketId(pokeballRecord.get("id"));
        int x = 0;
        int y = 0;
        BufferedImage bigImage = new BufferedImage(390,60,BufferedImage.TYPE_INT_ARGB);
        Graphics g = bigImage.getGraphics();
        String bigCSV = "\"item_id\",\"x\",\"y\"\n";
        for(CSVRecord itemCategory : listCategories) {
            // Get items in category
            List<CSVRecord> listItems = getItemRecordsByItemCategoryId(itemCategory.get("id"));
            for(CSVRecord item : listItems) {
                String pokeballImageUrl = "/pokedex-media/items/"+item.get("identifier")+".png";
                String pokeballId = item.get("id");
                g.drawImage(ImageIO.read(new File(getClass().getResource(pokeballImageUrl).getFile())),x,y,null);
                bigCSV += pokeballId+","+x+","+y+"\n";
                x += 30;
                if(x == 390) {
                    x = 0;
                    y += 30;
                }
            }
        }
        ImageIO.write(bigImage,"png",new File("pokeball-icons.png"));
        PrintWriter out = new PrintWriter("pokeball-icons.csv");
        out.print(bigCSV);
        out.close();
    }
}

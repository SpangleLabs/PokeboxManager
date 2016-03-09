package uk.org.spangle.tools;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import uk.org.spangle.data.Pokemon;
import uk.org.spangle.data.PokemonForm;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a very quick and dirty and ugly script to scrape the coordinates from raw_coords.txt into the database
 * raw_coords.txt is a specific subsection of https://github.com/msikma/pokesprite/blob/master/build/pokesprite.scss
 */
public class ImportPokemonAndForms {
    public final static String[] hyphenSpecies = new String[]{"nidoran-f","nidoran-m","mr-mime","ho-oh","mime-jr","porygon-z"};
    List<Pokemon> pokemonList;
    Session dbSession;

    public ImportPokemonAndForms() {
        pokemonList = new ArrayList<>();
    }

    public static void main(String[] args) {
        ImportPokemonAndForms imp = new ImportPokemonAndForms();
        imp.run();
        System.out.println(imp.pokemonList);
    }

    public void run() {
        // Create session
        SessionFactory sessionFactory = new org.hibernate.cfg.Configuration().configure(getClass().getResource("/hibernate.cfg.xml")).buildSessionFactory();
        dbSession = sessionFactory.openSession();

        // Check if pokemon are already imported
        List listGens = dbSession.createCriteria(Pokemon.class).list();
        if(listGens.size() > 0) {
            System.out.println("Pokemon have already been imported.");
            dbSession.close();
            sessionFactory.close();
            return;
        }

        // Load file
        File inputFile = new File(getClass().getResource("/raw_coords.txt").getFile());
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String remainLine = line.substring(7);
                String name = remainLine.split(" ")[0].substring(0,remainLine.split(" ")[0].length()-1);
                int x = Integer.parseInt(remainLine.split("\\(x: ")[1].split(",")[0]);
                int y = Integer.parseInt(remainLine.split(", y: ")[1].split(",")[0]);

                // Remove right facing sprites
                if(name.contains("-right")) {
                    continue;
                }

                // Set is_shiny
                boolean is_shiny = false;
                if(name.contains("-shiny")) is_shiny = true;

                // Set is_female
                boolean is_female = false;
                if(name.contains("-female")) is_female = true;

                // Set species name
                String species = name.split("-")[0];
                for(String hyphenName : hyphenSpecies) {
                    if(name.contains(hyphenName)) {
                        species = hyphenName;
                    }
                }

                // Set form name
                String form = "normal";
                if(!name.equals(species)) {
                    form = name.replace(species+"-","");
                }

                // Get pokemon
                Pokemon mon = getOrMakePokemon(species);

                PokemonForm monForm = getOrMakePokemonForm(mon,form);
                if(!is_female) {
                    monForm.setSpriteMaleX(x);
                    monForm.setSpriteMaleY(y);
                }
                monForm.setSpriteFemaleX(x);
                monForm.setSpriteFemaleY(y);
                monForm.setIsShiny(is_shiny);
                dbSession.update(monForm);
                dbSession.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        dbSession.close();
        sessionFactory.close();
    }

    public Pokemon getOrMakePokemon(String name) {
        int pokemon_id = 0;
        for(Pokemon mon : pokemonList) {
            // If found it, awesome
            if(mon.getName().equals(name)) {
                return mon;
            }
            pokemon_id++;
        }
        //Not found it, make it.
        Pokemon mon = new Pokemon();
        mon.setName(name);
        mon.setNationalDex(pokemon_id+1);
        mon.setPokemonForms(new ArrayList<PokemonForm>());
        pokemonList.add(mon);
        dbSession.saveOrUpdate(mon);
        return mon;
    }

    public PokemonForm getOrMakePokemonForm(Pokemon mon, String name) {
        for(PokemonForm monForm : mon.getPokemonForms()) {
            //If found it, awesome
            if(monForm.getName().equals(name)) {
                return monForm;
            }
        }
        PokemonForm monForm = new PokemonForm();
        monForm.setPokemon(mon);
        monForm.setName(name);
        mon.addPokemonForm(monForm);
        dbSession.saveOrUpdate(monForm);
        dbSession.saveOrUpdate(mon);
        return monForm;
    }
}

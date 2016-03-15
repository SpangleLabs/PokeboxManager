package uk.org.spangle.tools;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import uk.org.spangle.data.*;
import uk.org.spangle.model.Configuration;

import java.util.List;

/**
 * Quick and easy tool to import some test data
 */
public class ImportTestUserData {
    Session dbSession;

    public static void main(String[] args) {
        ImportTestUserData imp = new ImportTestUserData();
        imp.run();
    }

    public void run() {
        // Create connection
        SessionFactory sessionFactory = new org.hibernate.cfg.Configuration().configure(getClass().getResource("/hibernate.cfg.xml")).buildSessionFactory();
        dbSession = sessionFactory.openSession();

        // Check if user games table is empty
        List listGens = dbSession.createCriteria(UserGame.class).list();
        if(listGens.size() > 0) {
            System.out.println("User data has already been imported.");
            dbSession.close();
            sessionFactory.close();
            return;
        }

        // Try and create stuff
        try {
            createTestData();
        } finally {
            // Shut down cleanly
            dbSession.close();
            sessionFactory.close();
        }
    }

    public void createTestData() {
        // Get generations
        Generation genBank = (Generation) dbSession.createCriteria(Generation.class).add(Restrictions.eq("name","Pokebank")).list().get(0);
        Generation genSix = (Generation) dbSession.createCriteria(Generation.class).add(Restrictions.eq("name","Gen VI")).list().get(0);
        // Create games
        UserGame pokebank = new UserGame(genBank,"PokeBank",1);
        dbSession.save(pokebank);
        for(UserBox box : pokebank.getUserBoxes()) {
            dbSession.save(box);
        }
        pokebank.setCurrentBox(pokebank.getUserBoxes().get(0));
        dbSession.update(pokebank);
        UserGame ruby = new UserGame(genSix,"OmegaRuby",2);
        dbSession.save(ruby);
        for(UserBox box : ruby.getUserBoxes()) {
            dbSession.save(box);
        }
        ruby.setCurrentBox(ruby.getUserBoxes().get(0));
        dbSession.update(ruby);
        UserGame y = new UserGame(genSix,"Y",3);
        dbSession.save(y);
        for(UserBox box : y.getUserBoxes()) {
            dbSession.save(box);
        }
        y.setCurrentBox(y.getUserBoxes().get(0));
        dbSession.update(y);
        dbSession.flush();

        // Add config entries
        UserConfig currentGameConfig = new UserConfig();
        currentGameConfig.setKey(Configuration.CURRENT_GAME);
        currentGameConfig.setValue(Integer.toString(pokebank.getId()));
        dbSession.save(currentGameConfig);
        UserConfig hideEggsConfig = new UserConfig();
        hideEggsConfig.setKey(Configuration.HIDE_EGGS);
        hideEggsConfig.setValue("false");
        dbSession.save(hideEggsConfig);

        // Add some test pokemon
        UserBox testBox = ruby.getUserBoxes().get(1);
        Pokemon bulbasaur = (Pokemon) dbSession.createCriteria(Pokemon.class).add(Restrictions.eq("nationalDex",1)).list().get(0);
        Pokemon arbok = (Pokemon) dbSession.createCriteria(Pokemon.class).add(Restrictions.eq("nationalDex",24)).list().get(0);
        Pokemon persian = (Pokemon) dbSession.createCriteria(Pokemon.class).add(Restrictions.eq("nationalDex",53)).list().get(0);
        Pokemon dragonair = (Pokemon) dbSession.createCriteria(Pokemon.class).add(Restrictions.eq("nationalDex",148)).list().get(0);
        Pokemon meowstic = (Pokemon) dbSession.createCriteria(Pokemon.class).add(Restrictions.eq("nationalDex",678)).list().get(0);
        Pokemon unown = (Pokemon) dbSession.createCriteria(Pokemon.class).add(Restrictions.eq("nationalDex",201)).list().get(0);

        // Add pokemon with nicknames
        UserPokemon poke1 = new UserPokemon(testBox,1,bulbasaur);
        dbSession.save(poke1);
        UserPokemonNickname upn1 = new UserPokemonNickname(poke1,"Bulby");
        dbSession.save(upn1);

        UserPokemon poke2 = new UserPokemon(testBox,3,arbok);
        dbSession.save(poke2);
        UserPokemonNickname upn2 = new UserPokemonNickname(poke2,"Charbok");
        dbSession.save(upn2);


        // Add pokemon with natures
        UserPokemon poke3 = new UserPokemon(testBox,13,persian);
        dbSession.save(poke3);
        Nature jolly = (Nature) dbSession.createCriteria(Nature.class).add(Restrictions.eq("name","Jolly")).list().get(0);
        UserPokemonNature upn3 = new UserPokemonNature(poke3,jolly);
        dbSession.save(upn3);

        UserPokemon poke4 = new UserPokemon(testBox,8,dragonair);
        dbSession.save(poke4);
        Nature timid = (Nature) dbSession.createCriteria(Nature.class).add(Restrictions.eq("name","Timid")).list().get(0);
        UserPokemonNature upn4 = new UserPokemonNature(poke4,timid);
        dbSession.save(upn4);


        // Create meowstics for sexed sprites
        UserPokemon poke5 = new UserPokemon(testBox,20,meowstic);
        dbSession.save(poke5);
        UserPokemonSex ups5 = new UserPokemonSex(poke5,false);
        dbSession.save(ups5);

        UserPokemon poke6 = new UserPokemon(testBox,21,meowstic);
        dbSession.save(poke6);
        UserPokemonSex ups6 = new UserPokemonSex(poke6,true);
        dbSession.save(ups6);


        // Create unowns for different forms
        UserPokemon poke7 = new UserPokemon(testBox,26,unown);
        dbSession.save(poke7);
        PokemonForm unownB = (PokemonForm) dbSession.createCriteria(PokemonForm.class).add(Restrictions.eq("pokemon",unown)).add(Restrictions.eq("name","b")).list().get(0);
        UserPokemonForm upf7 = new UserPokemonForm(poke7,unownB);
        dbSession.save(upf7);

        UserPokemon poke8 = new UserPokemon(testBox,27,unown);
        dbSession.save(poke8);
        PokemonForm unownU = (PokemonForm) dbSession.createCriteria(PokemonForm.class).add(Restrictions.eq("pokemon",unown)).add(Restrictions.eq("name","u")).list().get(0);
        UserPokemonForm upf8 = new UserPokemonForm(poke8,unownU);
        dbSession.save(upf8);

        UserPokemon poke9 = new UserPokemon(testBox,28,unown);
        dbSession.save(poke9);
        PokemonForm unownT = (PokemonForm) dbSession.createCriteria(PokemonForm.class).add(Restrictions.eq("pokemon",unown)).add(Restrictions.eq("name","t")).list().get(0);
        UserPokemonForm upf9 = new UserPokemonForm(poke9,unownT);
        dbSession.save(upf9);

        UserPokemon poke10 = new UserPokemon(testBox,29,unown);
        dbSession.save(poke10);
        UserPokemonForm upf10 = new UserPokemonForm(poke10,unownT);
        dbSession.save(upf10);
        UserPokemonShiny ups10 = new UserPokemonShiny(poke10,true);
        dbSession.save(ups10);
        dbSession.flush();
    }
}

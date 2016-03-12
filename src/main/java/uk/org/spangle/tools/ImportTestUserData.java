package uk.org.spangle.tools;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import uk.org.spangle.data.*;

import java.sql.Timestamp;
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

        // Add config
        UserConfig currentGameConfig = new UserConfig();
        currentGameConfig.setKey("current_game");
        currentGameConfig.setValue(Integer.toString(pokebank.getId()));
        dbSession.save(currentGameConfig);

        // Add some test pokemon
        UserBox testBox = ruby.getUserBoxes().get(1);
        Pokemon bulbasaur = (Pokemon) dbSession.createCriteria(Pokemon.class).add(Restrictions.eq("nationalDex",1)).list().get(0);
        Pokemon arbok = (Pokemon) dbSession.createCriteria(Pokemon.class).add(Restrictions.eq("nationalDex",24)).list().get(0);
        Pokemon persian = (Pokemon) dbSession.createCriteria(Pokemon.class).add(Restrictions.eq("nationalDex",53)).list().get(0);
        Pokemon dragonair = (Pokemon) dbSession.createCriteria(Pokemon.class).add(Restrictions.eq("nationalDex",148)).list().get(0);
        Pokemon meowstic = (Pokemon) dbSession.createCriteria(Pokemon.class).add(Restrictions.eq("nationalDex",678)).list().get(0);
        Pokemon unown = (Pokemon) dbSession.createCriteria(Pokemon.class).add(Restrictions.eq("nationalDex",201)).list().get(0);
        UserPokemon poke1 = new UserPokemon();
        poke1.setNickname("Bulby");
        poke1.setPosition(1);
        poke1.setPokemon(bulbasaur);
        poke1.setUserBox(testBox);
        dbSession.save(poke1);
        UserPokemon poke2 = new UserPokemon();
        poke2.setNickname("Charbok");
        poke2.setPosition(3);
        poke2.setPokemon(arbok);
        poke2.setUserBox(testBox);
        dbSession.save(poke2);
        UserPokemon poke3 = new UserPokemon();
        poke3.setPosition(13);
        poke3.setPokemon(persian);
        poke3.setUserBox(testBox);
        dbSession.save(poke3);
        UserPokemon poke4 = new UserPokemon();
        poke4.setPosition(8);
        poke4.setPokemon(dragonair);
        poke4.setUserBox(testBox);
        dbSession.save(poke4);

        // Create meowstics for sexed sprites
        UserPokemon poke5 = new UserPokemon();
        poke5.setPosition(20);
        poke5.setPokemon(meowstic);
        poke5.setUserBox(testBox);
        dbSession.save(poke5);
        UserPokemonSex pokes5 = new UserPokemonSex();
        pokes5.setId(poke5.getId());
        pokes5.setIsMale(false);
        pokes5.setTimestamp(new Timestamp(0));
        dbSession.save(pokes5);
        UserPokemon poke6 = new UserPokemon();
        poke6.setPosition(21);
        poke6.setPokemon(meowstic);
        poke6.setUserBox(testBox);
        dbSession.save(poke6);
        UserPokemonSex pokes6 = new UserPokemonSex();
        pokes6.setId(poke6.getId());
        pokes6.setIsMale(true);
        pokes6.setTimestamp(new Timestamp(0));
        dbSession.save(pokes6);

        // Create unowns for different forms
        UserPokemon poke7 = new UserPokemon();
        poke7.setPosition(26);
        poke7.setPokemon(unown);
        poke7.setUserBox(testBox);
        dbSession.save(poke7);
        UserPokemonForm pokef7 = new UserPokemonForm();
        pokef7.setId(poke7.getId());
        PokemonForm unownb = (PokemonForm) dbSession.createCriteria(PokemonForm.class).add(Restrictions.eq("pokemon",unown)).add(Restrictions.eq("name","b")).list().get(0);
        pokef7.setPokemonForm(unownb);
        pokef7.setTimestamp(new Timestamp(0));
        dbSession.save(pokef7);
        UserPokemon poke8 = new UserPokemon();
        poke8.setPosition(27);
        poke8.setPokemon(unown);
        poke8.setUserBox(testBox);
        dbSession.save(poke8);
        UserPokemonForm pokef8 = new UserPokemonForm();
        pokef8.setId(poke8.getId());
        PokemonForm unownu = (PokemonForm) dbSession.createCriteria(PokemonForm.class).add(Restrictions.eq("pokemon",unown)).add(Restrictions.eq("name","u")).list().get(0);
        pokef8.setPokemonForm(unownu);
        pokef8.setTimestamp(new Timestamp(0));
        dbSession.save(pokef8);
        UserPokemon poke9 = new UserPokemon();
        poke9.setPosition(28);
        poke9.setPokemon(unown);
        poke9.setUserBox(testBox);
        dbSession.save(poke9);
        UserPokemonForm pokef9 = new UserPokemonForm();
        pokef9.setId(poke9.getId());
        PokemonForm unownt = (PokemonForm) dbSession.createCriteria(PokemonForm.class).add(Restrictions.eq("pokemon",unown)).add(Restrictions.eq("name","t")).list().get(0);
        pokef9.setPokemonForm(unownt);
        pokef9.setTimestamp(new Timestamp(0));
        dbSession.save(pokef9);
        UserPokemon poke10 = new UserPokemon();
        poke10.setPosition(29);
        poke10.setPokemon(unown);
        poke10.setUserBox(testBox);
        dbSession.save(poke10);
        UserPokemonForm pokef10 = new UserPokemonForm();
        pokef10.setId(poke10.getId());
        pokef10.setPokemonForm(unownt);
        pokef10.setTimestamp(new Timestamp(0));
        dbSession.save(pokef10);
        dbSession.flush();
    }
}

package uk.org.spangle.tools;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import uk.org.spangle.data.*;

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
        UserGame ruby = new UserGame(genSix,"OmegaRuby",2);
        dbSession.save(ruby);
        UserGame y = new UserGame(genSix,"Y",3);
        dbSession.save(y);

        // Add config
        UserConfig currentGameConfig = new UserConfig();
        currentGameConfig.setKey("current_game");
        currentGameConfig.setValue(Integer.toString(pokebank.getId()));
        dbSession.save(currentGameConfig);

        // Add some test pokemon
        UserBox testBox = ruby.getUserBoxes().get(1);
        Pokemon bulbasaur = (Pokemon) dbSession.createCriteria(Pokemon.class).add(Restrictions.eq("national_dex",1)).list().get(0);
        Pokemon arbok = (Pokemon) dbSession.createCriteria(Pokemon.class).add(Restrictions.eq("national_dex",24)).list().get(0);
        Pokemon persian = (Pokemon) dbSession.createCriteria(Pokemon.class).add(Restrictions.eq("national_dex",53)).list().get(0);
        Pokemon dragonair = (Pokemon) dbSession.createCriteria(Pokemon.class).add(Restrictions.eq("national_dex",148)).list().get(0);
        Pokemon meowstic = (Pokemon) dbSession.createCriteria(Pokemon.class).add(Restrictions.eq("national_dex",678)).list().get(0);
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
        UserPokemon poke5 = new UserPokemon();
        poke5.setPosition(20);
        poke5.setPokemon(meowstic);
        poke5.setUserBox(testBox);
        dbSession.save(poke5);
        UserPokemon poke6 = new UserPokemon();
        poke6.setPosition(21);
        poke6.setPokemon(meowstic);
        poke6.setUserBox(testBox);
        dbSession.save(poke6);
    }
}

package uk.org.spangle.tools;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import uk.org.spangle.data.PokeBall;

import java.util.List;

/**
 * Quick and easy tool to import the list of pokeballs.
 * Hardcoded in from information from serebii list
 */
@Deprecated
public class ImportPokeBalls {
    Session dbSession;

    public static void main(String[] args) {
        ImportPokeBalls imp = new ImportPokeBalls();
        imp.run();
    }

    public void run() {
        // Create connection
        SessionFactory sessionFactory = new org.hibernate.cfg.Configuration().configure(getClass().getResource("/hibernate.cfg.xml")).buildSessionFactory();
        dbSession = sessionFactory.openSession();

        // Check if pokeballs table is empty
        List listGens = dbSession.createCriteria(PokeBall.class).list();
        if(listGens.size() > 0) {
            System.out.println("PokeBalls have already been imported.");
            dbSession.close();
            sessionFactory.close();
            return;
        }

        // Try and create stuff
        try {
            createPokeBalls();
        } finally {
            // Shut down cleanly
            dbSession.close();
            sessionFactory.close();
        }
    }

    public void createPokeBalls() {
        PokeBall cherishBall = new PokeBall("Cherish Ball","A quite rare Poké Ball that has been crafted in order to commemorate a special occasion of some sort.",930,422);
        dbSession.save(cherishBall);
        PokeBall diveBall = new PokeBall("Dive Ball","A somewhat different Poké Ball that works especially well when catching Pokémon that live underwater.",960,422);
        dbSession.save(diveBall);
        PokeBall dreamBall = new PokeBall("Dream Ball","A special Poké Ball that appears in your Bag out of nowhere in the Entree Forest. It can catch any Pokémon.",990,422);
        dbSession.save(dreamBall);
        PokeBall duskBall = new PokeBall("Dusk Ball","A somewhat different Poké Ball that makes it easier to catch wild Pokémon at night or in dark places like caves.",1020,422);
        dbSession.save(duskBall);
        PokeBall fastBall = new PokeBall("Fast Ball","A Poké Ball that makes it easier to catch Pokémon that are usually very quick to run away.",1050,422);
        dbSession.save(fastBall);
        PokeBall friendBall = new PokeBall("Friend Ball","A strange Poké Ball that will make the wild Pokémon caught with it more friendly toward you immediately.",1080,422);
        dbSession.save(friendBall);
        PokeBall greatBall = new PokeBall("Great Ball","A good, high-performance Poké Ball that provides a higher Pokémon catch rate than a standard Poké Ball can.",1110,422);
        dbSession.save(greatBall);
        PokeBall healBall = new PokeBall("Heal Ball","A remedial Poké Ball that restores the HP of a Pokémon caught with it and eliminates any status conditions.",1140,422);
        dbSession.save(healBall);
        PokeBall heavyBall = new PokeBall("Heavy Ball","A Poké Ball that is better than usual at catching very heavy Pokémon.",1170,422);
        dbSession.save(heavyBall);
        PokeBall levelBall = new PokeBall("Level Ball","A Poké Ball that makes it easier to catch Pokémon that are at a lower level than your own Pokémon.",1200,422);
        dbSession.save(levelBall);
        PokeBall loveBall = new PokeBall("Love Ball","A Poké Ball that works best when catching a Pokémon that is of the opposite gender of your Pokémon.",1230,422);
        dbSession.save(loveBall);
        PokeBall lureBall = new PokeBall("Lure Ball","A Poké Ball that is good for catching Pokémon that you reel in with a Rod while out fishing.",0,452);
        dbSession.save(lureBall);
        PokeBall luxuryBall = new PokeBall("Luxury Ball","A particularly comfortable Poké Ball that makes a wild Pokémon quickly grow friendlier after being caught.",30,452);
        dbSession.save(luxuryBall);
        PokeBall masterBall = new PokeBall("Master Ball","The best Poké Ball with the ultimate level of performance. With it, you will catch any wild Pokémon without fail.",60,452);
        dbSession.save(masterBall);
        PokeBall moonBall = new PokeBall("Moon Ball","A Poké Ball that will make it easier to catch Pokémon that can evolve using a Moon Stone.",90,452);
        dbSession.save(moonBall);
        PokeBall nestBall = new PokeBall("Nest Ball","A somewhat different Poké Ball that becomes more effective the lower the level of the wild Pokémon.",120,452);
        dbSession.save(nestBall);
        PokeBall netBall = new PokeBall("Net Ball","A somewhat different Poké Ball that is more effective when attempting to catch Water- or Bug-type Pokémon.",150,452);
        dbSession.save(netBall);
        PokeBall parkBall = new PokeBall("Park Ball","A special Poké Ball for the Pal Park.",180,452);
        dbSession.save(parkBall);
        PokeBall pokeBall = new PokeBall("Poké Ball","A device for catching wild Pokémon. It's thrown like a ball at a Pokémon, comfortably encapsulating its target.",210,452);
        dbSession.save(pokeBall);
        PokeBall premierBall = new PokeBall("Premier Ball","A somewhat rare Poké Ball that was made as a commemorative item used to celebrate an event of some sort.",240,452);
        dbSession.save(premierBall);
        PokeBall quickBall = new PokeBall("Quick Ball","A somewhat different Poké Ball that has a more successful catch rate if used at the start of a wild encounter.",270,452);
        dbSession.save(quickBall);
        PokeBall repeatBall = new PokeBall("Repeat Ball","A somewhat different Poké Ball that works especially well on a Pokémon species that has been caught before.",300,452);
        dbSession.save(repeatBall);
        PokeBall safariBall = new PokeBall("Safari Ball","A special Poké Ball that is used only in the Great Marsh. It is recognizable by the camouflage pattern decorating it.",330,452);
        dbSession.save(safariBall);
        PokeBall sportBall = new PokeBall("Sport Ball","A special Poké Ball that is used during the Bug-Catching Contest.",360,452);
        dbSession.save(sportBall);
        PokeBall timerBall = new PokeBall("Timer Ball","A somewhat different Poké Ball that becomes progressively more effective the more turns that are taken in battle.",390,452);
        dbSession.save(timerBall);
        PokeBall ultraBall = new PokeBall("Ultra Ball","An ultra-high-performance Poké Ball that provides a higher success rate for catching Pokémon than a Great Ball.",360,452);
        dbSession.save(ultraBall);
    }
}

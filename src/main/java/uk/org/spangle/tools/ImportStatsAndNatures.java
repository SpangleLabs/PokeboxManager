package uk.org.spangle.tools;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import uk.org.spangle.data.Nature;
import uk.org.spangle.data.Stat;

import java.util.List;

/**
 * Quick and easy tool to import the list of natures and stats.
 */
public class ImportStatsAndNatures {
    Session dbSession;

    public static void main(String[] args) {
        ImportStatsAndNatures imp = new ImportStatsAndNatures();
        imp.run();
    }

    public void run() {
        // Create connection
        SessionFactory sessionFactory = new org.hibernate.cfg.Configuration().configure(getClass().getResource("/hibernate.cfg.xml")).buildSessionFactory();
        dbSession = sessionFactory.openSession();

        // Check if stats table is empty
        List listGens = dbSession.createCriteria(Stat.class).list();
        if(listGens.size() > 0) {
            System.out.println("Stats have already been imported.");
            dbSession.close();
            sessionFactory.close();
            return;
        }

        // Try and create stuff
        try {
            createStatsAndNatures();
        } finally {
            // Shut down cleanly
            dbSession.close();
            sessionFactory.close();
        }
    }

    public void createStatsAndNatures() {
        // Add stats
        Stat atk = new Stat("Attack","Atk");
        dbSession.save(atk);
        Stat def = new Stat("Defense","Def");
        dbSession.save(def);
        Stat hp = new Stat("HP","HP");
        dbSession.save(hp);
        Stat spa = new Stat("Sp. Atk","SpA");
        dbSession.save(spa);
        Stat spd = new Stat("Sp. Def","SpD");
        dbSession.save(spd);
        Stat spe = new Stat("Speed","Spe");
        dbSession.save(spe);
        dbSession.flush();

        Nature adamant = new Nature("Adamant",atk,spa);
        dbSession.save(adamant);
        Nature bashful = new Nature("Bashful",spa,spa);
        dbSession.save(bashful);
        Nature bold = new Nature("Bold",def,atk);
        dbSession.save(bold);
        Nature brave = new Nature("Brave",atk,spe);
        dbSession.save(brave);
        Nature calm = new Nature("Calm",spd,atk);
        dbSession.save(calm);
        Nature careful = new Nature("Careful",spd,spa);
        dbSession.save(careful);
        Nature docile = new Nature("Docile",def,def);
        dbSession.save(docile);
        Nature gentle = new Nature("Gentle",spd,def);
        dbSession.save(gentle);
        Nature hardy = new Nature("Hardy",atk,atk);
        dbSession.save(hardy);
        Nature hasty = new Nature("Hasty",spe,def);
        dbSession.save(hasty);
        Nature impish = new Nature("Impish",def,spa);
        dbSession.save(impish);
        Nature jolly = new Nature("Jolly",spe,spa);
        dbSession.save(jolly);
        Nature lax = new Nature("Lax",def,spd);
        dbSession.save(lax);
        Nature lonely = new Nature("Lonely",atk,def);
        dbSession.save(lonely);
        Nature mild = new Nature("Mild",spa,def);
        dbSession.save(mild);
        Nature modest = new Nature("Modest",spa,atk);
        dbSession.save(modest);
        Nature naive = new Nature("Naive",spe,spd);
        dbSession.save(naive);
        Nature naughty = new Nature("Naughty",atk,spd);
        dbSession.save(naughty);
        Nature quiet = new Nature("Quiet",spa,spe);
        dbSession.save(quiet);
        Nature quirky = new Nature("Quirky",spd,spd);
        dbSession.save(quirky);
        Nature rash = new Nature("Rash",spa,spd);
        dbSession.save(rash);
        Nature relaxed = new Nature("Relaxed",def,spe);
        dbSession.save(relaxed);
        Nature sassy = new Nature("Sassy",spd,spe);
        dbSession.save(sassy);
        Nature serious = new Nature("Serious",spe,spe);
        dbSession.save(serious);
        Nature timid = new Nature("Timid",spe,atk);
        dbSession.save(timid);
        dbSession.flush();
    }
}

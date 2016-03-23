package uk.org.spangle.tools;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import uk.org.spangle.data.Language;

import java.util.List;

/**
 * Quick and easy tool to import the list of languages.
 */
public class ImportLanguages {
    private Session dbSession;

    public static void main(String[] args) {
        ImportLanguages imp = new ImportLanguages();
        imp.run();
    }

    public void run() {
        // Create connection
        SessionFactory sessionFactory = new org.hibernate.cfg.Configuration().configure(getClass().getResource("/hibernate.cfg.xml")).buildSessionFactory();
        dbSession = sessionFactory.openSession();

        // Check if generations table is empty
        List listGens = dbSession.createCriteria(Language.class).list();
        if(listGens.size() > 0) {
            System.out.println("Languages have already been imported.");
            dbSession.close();
            sessionFactory.close();
            return;
        }

        // Try and create stuff
        try {
            createGenerations();
        } finally {
            // Shut down cleanly
            try {
                dbSession.close();
            } catch (HibernateException e) { e.printStackTrace(); }
            try {
                sessionFactory.close();
            } catch (HibernateException e) { e.printStackTrace(); }
        }
    }

    private void createGenerations() {
        Language english = new Language("English","ENG");
        dbSession.save(english);
        Language japanese = new Language("Japanese","JPN");
        dbSession.save(japanese);
        Language korean = new Language("Korean","KOR");
        dbSession.save(korean);
        Language spanish = new Language("Spanish","SPA");
        dbSession.save(spanish);
        Language french = new Language("French","FRE");
        dbSession.save(french);
        Language german = new Language("German","GER");
        dbSession.save(german);
        Language italian = new Language("Italian","ITA");
        dbSession.save(italian);
        dbSession.flush();
    }
}

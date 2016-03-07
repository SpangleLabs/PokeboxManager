package uk.org.spangle.model;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import uk.org.spangle.data.UserConfig;
import uk.org.spangle.data.UserGame;

import java.util.List;

public class Configuration {

    private String getUserConfigValue(String key) {
        SessionFactory sessionFactory = new org.hibernate.cfg.Configuration().configure(getClass().getResource("/hibernate.cfg.xml")).buildSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from UserConfig where key = :key");
        query.setString("key",key);
        List list = query.list();

        String value = null;
        for (Object obj : list) {
            UserConfig keyPair = (UserConfig) obj;
            value = keyPair.getValue();
        }

        session.getTransaction().commit();
        session.close();
        return value;
    }

    public Integer getCurrentGameId() {
        String gameId = this.getUserConfigValue("current_game");
        if(gameId != null) {
            return Integer.parseInt(gameId);
        }
        return null;
    }

    public void setCurrentGameId(int currentGameId) {
        return;
        //TODO:
    }

    public UserGame getCurrentGame() {
        Integer currentGameId = this.getCurrentGameId();
        if(currentGameId == null) return null;

        SessionFactory sessionFactory = new org.hibernate.cfg.Configuration().configure(getClass().getResource("/hibernate.cfg.xml")).buildSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from UserGame where id = :id");
        query.setInteger("id",currentGameId);
        List list = query.list();

        String value = null;
        UserGame userGame = null;
        for (Object obj : list) {
            userGame = (UserGame) obj;
        }

        session.getTransaction().commit();
        session.close();
        return userGame;
    }

    public Integer getCurrentBoxId() {
        String boxId = this.getUserConfigValue("current_box");
        if(boxId != null) {
            return Integer.parseInt(boxId);
        }
        return null;
    }

    public void setCurrentBoxId(int currentBoxId) {
        return;
        //TODO:
    }
}

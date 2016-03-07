package uk.org.spangle.model;

import org.hibernate.Query;
import org.hibernate.Session;
import uk.org.spangle.data.UserConfig;
import uk.org.spangle.data.UserGame;

import java.util.List;

public class Configuration {
    Session session;

    public Configuration(Session session) {
        this.session = session;
    }

    private UserConfig getUserConfig(String key) {
        session.beginTransaction();
        Query query = session.createQuery("from UserConfig where key = :key");
        query.setString("key",key);
        List list = query.list();

        UserConfig userConfig = null;
        for (Object obj : list) {
            userConfig = (UserConfig) obj;
        }

        session.getTransaction().commit();
        return userConfig;
    }

    private String getUserConfigValue(String key) {
        UserConfig userConfig = this.getUserConfig(key);
        if(userConfig == null) return null;
        return userConfig.getValue();
    }

    public Integer getCurrentGameId() {
        String gameId = this.getUserConfigValue("current_game");
        if(gameId == null) return null;
        return Integer.parseInt(gameId);
    }

    public void setCurrentGameId(int currentGameId) throws Exception {
        UserConfig currentGameConfig = this.getUserConfig("current_game");
        if(currentGameConfig == null) throw new Exception();

        session.beginTransaction();
        currentGameConfig.setValue(String.valueOf(currentGameId));
        session.getTransaction().commit();
    }

    public UserGame getCurrentGame() {
        Integer currentGameId = this.getCurrentGameId();
        if(currentGameId == null) return null;

        session.beginTransaction();
        Query query = session.createQuery("from UserGame where id = :id");
        query.setInteger("id",currentGameId);
        List list = query.list();

        UserGame userGame = null;
        for (Object obj : list) {
            userGame = (UserGame) obj;
        }

        session.getTransaction().commit();
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

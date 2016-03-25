package uk.org.spangle.model;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import uk.org.spangle.data.UserConfig;
import uk.org.spangle.data.UserGame;

import java.util.List;

public class Configuration {

    public final static String CURRENT_GAME = "current_game";
    public final static String HIDE_EGGS = "hide_eggs";

    public final static String ABILITY_SLOT_1 = "slot_1";
    public final static String ABILITY_SLOT_2 = "slot_2";
    public final static String ABILITY_SLOT_HIDDEN = "hidden";

    public final static String STAT_ATK = "Atk";
    public final static String STAT_DEF = "Def";
    public final static String STAT_HP = "HP";
    public final static String STAT_SPA = "SpA";
    public final static String STAT_SPD = "SpD";
    public final static String STAT_SPE = "Spe";

    public final static String MOVE_1 = "move_1";
    public final static String MOVE_2 = "move_2";
    public final static String MOVE_3 = "move_3";
    public final static String MOVE_4 = "move_4";
    public final static String RELEARN_1 = "relearn_1";
    public final static String RELEARN_2 = "relearn_2";
    public final static String RELEARN_3 = "relearn_3";
    public final static String RELEARN_4 = "relearn_4";

    Session session;

    public Configuration(Session session) {
        this.session = session;
    }

    private UserConfig getUserConfig(String key) {
        List list = session.createCriteria(UserConfig.class).add(Restrictions.eq("key",key)).list();
        if(list.size() == 0) return null;
        return (UserConfig) list.get(0);
    }

    private String getUserConfigValue(String key) {
        UserConfig userConfig = this.getUserConfig(key);
        if(userConfig == null) return null;
        return userConfig.getValue();
    }

    public Integer getCurrentGameId() {
        String gameId = this.getUserConfigValue(CURRENT_GAME);
        if(gameId == null) return null;
        return Integer.parseInt(gameId);
    }

    public void setCurrentGameId(int currentGameId) {
        UserConfig currentGameConfig = this.getUserConfig(CURRENT_GAME);
        if(currentGameConfig == null) {
            UserConfig userConfig = new UserConfig(CURRENT_GAME,String.valueOf(currentGameId));
            session.save(userConfig);
            return;
        }
        currentGameConfig.setValue(String.valueOf(currentGameId));
        session.update(currentGameConfig);
    }

    public UserGame getCurrentGame() {
        Integer currentGameId = this.getCurrentGameId();
        if(currentGameId == null) return null;
        return (UserGame) session.createCriteria(UserGame.class).add(Restrictions.eq("id",currentGameId)).list().get(0);
    }

    public void setCurrentGame(UserGame currentGame) {
        int currentGameId = currentGame.getId();
        this.setCurrentGameId(currentGameId);
    }

    public boolean getHideEggs() {
        String hideEggs = this.getUserConfigValue(HIDE_EGGS);
        if(hideEggs == null) return false;
        return Boolean.parseBoolean(hideEggs);
    }

    public void setHideEggs(boolean hideEggs) {
        UserConfig currentGameConfig = this.getUserConfig(HIDE_EGGS);
        if(currentGameConfig == null) {
            UserConfig userConfig = new UserConfig(HIDE_EGGS,Boolean.toString(hideEggs));
            session.save(userConfig);
            return;
        }

        currentGameConfig.setValue(Boolean.toString(hideEggs));
        session.update(currentGameConfig);
    }
}

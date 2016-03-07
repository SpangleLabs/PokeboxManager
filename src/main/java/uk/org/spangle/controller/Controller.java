package uk.org.spangle.controller;

import org.hibernate.Session;
import uk.org.spangle.data.UserBox;
import uk.org.spangle.data.UserGame;
import uk.org.spangle.model.Configuration;
import uk.org.spangle.view.App;

public class Controller {
    Session session;
    Configuration conf;
    App app;

    public Controller(Session session, Configuration conf, App app) {
        this.session = session;
        this.conf = conf;
        this.app = app;
    }

    public void updateGame(UserGame value) {
        conf.setCurrentGame(value);
        app.getSideBar().setGame(value);
    }

    public void updateBox(UserGame currentGame, int gameNum) {
        UserBox userBox = currentGame.getUserBoxes().get(gameNum);
        session.beginTransaction();
        currentGame.setCurrentBox(userBox);
        session.getTransaction().commit();
    }
}

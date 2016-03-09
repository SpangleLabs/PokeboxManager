package uk.org.spangle.controller;

import org.hibernate.Session;
import uk.org.spangle.data.UserBox;
import uk.org.spangle.data.UserGame;
import uk.org.spangle.model.Configuration;
import uk.org.spangle.view.App;

import java.util.List;

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
        app.getSideBar().updateBoxCanvas();
    }

    public void prevBox(UserGame currentGame) {
        UserBox currentBox = currentGame.getCurrentBox();
        List<UserBox> listBoxes = currentGame.getUserBoxes();
        int index = listBoxes.indexOf(currentBox);
        // Remove 1 from index and wrap it around
        int newIndex = ((index-1) % listBoxes.size() + listBoxes.size()) % listBoxes.size();
        UserBox newBox = listBoxes.get(newIndex);
        session.beginTransaction();
        currentGame.setCurrentBox(newBox);
        session.getTransaction().commit();
        app.getSideBar().updateBoxDropdown();
        app.getSideBar().updateBoxCanvas();
    }

    public void nextBox(UserGame currentGame) {
        UserBox currentBox = currentGame.getCurrentBox();
        List<UserBox> listBoxes = currentGame.getUserBoxes();
        int index = listBoxes.indexOf(currentBox);
        int newIndex = (index+1) % listBoxes.size();
        UserBox newBox = listBoxes.get(newIndex);
        session.beginTransaction();
        currentGame.setCurrentBox(newBox);
        session.getTransaction().commit();
        app.getSideBar().updateBoxDropdown();
        app.getSideBar().updateBoxCanvas();
    }
}

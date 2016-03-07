package uk.org.spangle.controller;

import org.hibernate.Session;
import uk.org.spangle.data.UserGame;
import uk.org.spangle.model.Configuration;

public class Controller {
    Session session;
    Configuration conf;

    public Controller(Session session, Configuration conf) {
        this.session = session;
        this.conf = conf;
    }

    public void updateGame(UserGame value) {
        conf.setCurrentGame(value);
    }
}

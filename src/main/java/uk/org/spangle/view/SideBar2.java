package uk.org.spangle.view;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import uk.org.spangle.data.UserGame;

import java.util.ArrayList;
import java.util.List;

public class SideBar {
    Pane sideBarPane;
    ChoiceBox<String> gameDropdown;
    ChoiceBox<String> boxDropdown;
    UserGame currentGame = null;

    public SideBar(Pane sideBarPane) {
        this.sideBarPane = sideBarPane;

        // Get current game
        uk.org.spangle.model.Configuration conf = new uk.org.spangle.model.Configuration();
        currentGame = conf.getCurrentGame();

        // Game selector and titles
        Text titleText = new Text("Game");
        gameDropdown = createGameDropdown();

        // Boxes selector and buttons
        Text boxText = new Text("Box");
        Button boxLeft = new Button("<");
        boxDropdown = new ChoiceBox<>();
        boxDropdown.setItems(FXCollections.observableArrayList("A","B","C"));
        Button boxRight = new Button(">");
        HBox boxButtons = new HBox();
        boxButtons.getChildren().addAll(boxLeft,boxDropdown,boxRight);

        // Add everything to the sidebar
        VBox sideBarVBox = new VBox();
        sideBarVBox.getChildren().addAll(titleText,gameDropdown,boxText,boxButtons);

        sideBarPane.getChildren().add(sideBarVBox);
    }

    public ChoiceBox<String> createGameDropdown() {

        SessionFactory sessionFactory = new Configuration().configure(getClass().getResource("/hibernate.cfg.xml")).buildSessionFactory();
        Session session = sessionFactory.openSession();

        // now lets pull events from the database and list them
        session.beginTransaction();
        List result = session.createQuery("from UserGame").list();
        List<String> nameList = new ArrayList<>();
        for (Object obj : result) {
            UserGame event = (UserGame) obj;
            nameList.add(event.getName());
            if(currentGame == null) currentGame = event;
        }
        session.getTransaction().commit();
        session.close();

        sessionFactory.close();

        ChoiceBox<String> gameDropdown = new ChoiceBox<>();
        gameDropdown.setItems(FXCollections.observableArrayList(nameList));

        gameDropdown.setValue(currentGame.getName());
        return gameDropdown;
    }

    public void setGame(int gameId) {
        // Update box dropdown
    }

    public void setBox(int userBoxId) {
        // Update box canvas and such
    }
}

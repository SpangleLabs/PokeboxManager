package uk.org.spangle.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.hibernate.Session;
import uk.org.spangle.controller.Controller;
import uk.org.spangle.data.UserGame;
import uk.org.spangle.model.Configuration;

import java.util.ArrayList;
import java.util.List;

public class SideBar {
    Pane sideBarPane;
    ChoiceBox<String> gameDropdown;
    ChoiceBox<String> boxDropdown;
    UserGame currentGame = null;
    Session session;
    Configuration conf;
    Controller controller;

    public SideBar(Pane sideBarPane, Session session, Configuration conf, Controller controller) {
        this.sideBarPane = sideBarPane;
        this.session = session;
        this.conf = conf;
        this.controller = controller;

        // Get current game
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

        // now lets pull events from the database and list them
        session.beginTransaction();
        List result = session.createQuery("from UserGame").list();
        List<String> nameList = new ArrayList<>();
        final List<UserGame> gameList = new ArrayList<>();
        for (Object obj : result) {
            UserGame event = (UserGame) obj;
            nameList.add(event.getName());
            gameList.add(event);
            if(currentGame == null) currentGame = event;
        }
        session.getTransaction().commit();

        ChoiceBox<String> gameDropdown = new ChoiceBox<>();
        gameDropdown.setItems(FXCollections.observableArrayList(nameList));
        gameDropdown.setValue(currentGame.getName());
        gameDropdown.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number old_value, Number new_value) {
                UserGame selectedGame = gameList.get(new_value.intValue());
                controller.updateGame(selectedGame);
            }
        });
        return gameDropdown;
    }

    public void setGame(UserGame userGame) {
        // Update box dropdown
    }

    public void setBox(int userBoxId) {
        // Update box canvas and such
    }
}

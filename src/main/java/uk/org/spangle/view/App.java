package uk.org.spangle.view;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import uk.org.spangle.data.Generation;
import uk.org.spangle.data.UserGame;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    HBox root;
    VBox sideBar;
    Pane infoPanel;


    @Override
    public void start(Stage primaryStage) throws Exception{
        // Update side bar
        updateSideBar();

        // Update info panel
        updateInfoPanel();

        // Root HBox
        root = new HBox();
        root.getChildren().addAll(sideBar,infoPanel);

        // Construct window and show.
        primaryStage.setTitle("Hello World");
        Scene primaryScene = new Scene(root, 300, 275);
        primaryScene.getStylesheets().add("sample.css");
        primaryStage.setScene(primaryScene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void updateSideBar() {
        Text titleText = new Text("Game");
        ChoiceBox<String> gameDropdown = createGameDropdown();
        Text boxText = new Text("Box");

        Button boxLeft = new Button("<");
        ChoiceBox<String> boxDropdown = new ChoiceBox<>();
        boxDropdown.setItems(FXCollections.<String>observableArrayList("A","B","C"));
        Button boxRight = new Button(">");
        HBox boxButtons = new HBox();
        boxButtons.getChildren().addAll(boxLeft,boxDropdown,boxRight);

        // Add everything to the sidebar
        sideBar = new VBox();
        sideBar.getChildren().addAll(titleText,gameDropdown,boxText,boxButtons);
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
            System.out.println(event.getName());
            nameList.add(event.getName());
        }
        session.getTransaction().commit();
        session.close();

        sessionFactory.close();

        ChoiceBox<String> gameDropdown = new ChoiceBox<>();
        gameDropdown.setItems(FXCollections.<String>observableArrayList(nameList));
        gameDropdown.setValue(nameList.get(0));
        return gameDropdown;
    }

    public void updateInfoPanel() {
        Text pokemonName = new Text("Pokemon");
        Text pokemonDesc = new Text("Some descriptor");

        VBox infoColumns = new VBox();
        infoColumns.getChildren().addAll(pokemonName,pokemonDesc);
        HBox infoBoxContents = new HBox();
        infoBoxContents.getChildren().addAll(infoColumns);

        infoPanel = new Pane();
        infoPanel.getChildren().add(infoBoxContents);
    }
}

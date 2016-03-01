package uk.org.spangle;

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
        ChoiceBox<String> gameDropdown = new ChoiceBox<>();
        gameDropdown.setItems(FXCollections.observableArrayList(
                "Pokebank", "Omega Ruby ", "Alpha Sapphire", "Save as")
        );
        Text boxText = new Text("Box");

        Button boxLeft = new Button("<");
        Button boxRight = new Button(">");
        HBox boxButtons = new HBox();
        boxButtons.getChildren().addAll(boxLeft,boxRight);

        // Add everything to the sidebar
        sideBar = new VBox();
        sideBar.getChildren().addAll(titleText,gameDropdown,boxText,boxButtons);
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

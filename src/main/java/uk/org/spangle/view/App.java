package uk.org.spangle.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App extends Application {

    HBox root;
    SideBar sideBar;
    InfoBox infoBox;


    @Override
    public void start(Stage primaryStage) throws Exception{
        // Update side bar
        Pane sideBarPane = new Pane();
        sideBar = new SideBar(sideBarPane);
        sideBar.update();

        // Update info panel
        Pane infoPanelPane = new Pane();
        infoBox = new InfoBox(infoPanelPane);
        infoBox.update();

        // Root HBox
        root = new HBox();
        root.getChildren().addAll(sideBarPane,infoPanelPane);

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
}

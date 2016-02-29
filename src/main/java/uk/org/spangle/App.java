package uk.org.spangle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
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

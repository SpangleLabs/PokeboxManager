package uk.org.spangle.view;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import uk.org.spangle.controller.Controller;
import uk.org.spangle.model.Configuration;

public class App extends Application {

    HBox root;
    SideBar sideBar;
    InfoBox infoBox;
    Session dbSession;
    Configuration conf;


    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create database connection
        final SessionFactory sessionFactory = new org.hibernate.cfg.Configuration().configure(getClass().getResource("/hibernate.cfg.xml")).buildSessionFactory();
        dbSession = sessionFactory.openSession();

        // Create configuration
        conf = new Configuration(dbSession);

        // Create controller
        Controller controller = new Controller(dbSession,conf,this);

        // Create side bar
        Pane sideBarPane = new Pane();
        sideBar = new SideBar(sideBarPane,dbSession,conf,controller);

        // Create info panel
        Pane infoPanelPane = new Pane();
        infoBox = new InfoBox(infoPanelPane,dbSession,conf,controller);

        // Root HBox
        root = new HBox();
        root.getChildren().addAll(sideBarPane,infoPanelPane);

        // Construct window and show.
        primaryStage.setTitle("Hello World");
        Scene primaryScene = new Scene(root, 400, 275);
        primaryScene.getStylesheets().add("sample.css");
        primaryStage.setScene(primaryScene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                dbSession.close();
                sessionFactory.close();
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }

    public SideBar getSideBar() {
        return sideBar;
    }

    public InfoBox getInfoBox() {
        return infoBox;
    }
}

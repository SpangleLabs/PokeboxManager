package uk.org.spangle.view;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.hibernate.Session;
import uk.org.spangle.controller.Controller;
import uk.org.spangle.model.Configuration;

public class InfoBox {
    Pane infoBoxPane;
    Session session;
    Configuration conf;
    Controller controller;

    public InfoBox(Pane infoBoxPane, Session session, Configuration conf, Controller controller) {
        this.infoBoxPane = infoBoxPane;
        this.session = session;
        this.conf = conf;
        this.controller = controller;

        // Construct infobox
        Text pokemonName = new Text("Pokemon");
        Text pokemonDesc = new Text("Some descriptor");

        VBox infoColumns = new VBox();
        infoColumns.getChildren().addAll(pokemonName,pokemonDesc);
        HBox infoBoxContents = new HBox();
        infoBoxContents.getChildren().addAll(infoColumns);

        infoBoxPane.getChildren().add(infoBoxContents);
    }
}

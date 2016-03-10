package uk.org.spangle.view;

import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import uk.org.spangle.controller.Controller;
import uk.org.spangle.data.Pokemon;
import uk.org.spangle.data.UserBox;
import uk.org.spangle.model.Configuration;

import java.util.List;

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

    public void addNewPokemon(UserBox userBox, int position) {
        Text title = new Text("Add new pokemon");

        Text speciesText = new Text("Choose species:");
        ChoiceBox<String> speciesBox = new ChoiceBox<>();
        List listSpecies = session.createCriteria(Pokemon.class).addOrder(Order.asc("nationalDex")).list();
        for(Object species : listSpecies) {
            speciesBox.getItems().add(((Pokemon)species).getName());
        }
        HBox species = new HBox();
        species.getChildren().addAll(speciesText,speciesBox);


        VBox formRows = new VBox();
        formRows.getChildren().addAll(title,species);

        infoBoxPane.getChildren().setAll(formRows);
    }
}

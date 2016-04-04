package uk.org.spangle.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import uk.org.spangle.controller.Controller;
import uk.org.spangle.data.*;
import uk.org.spangle.model.Configuration;
import uk.org.spangle.model.UserPokemonStat;

import java.util.*;

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

    public void addNewPokemon(final UserBox userBox, final int position) {
        Text title = new Text("Add new pokemon");

        Text speciesText = new Text("Choose species:");
        //ChoiceBox<String> speciesBox = new ChoiceBox<>();
        final AutoCompleteTextField speciesBox = new AutoCompleteTextField();
        List listSpecies = session.createCriteria(Pokemon.class).addOrder(Order.asc("nationalDex")).list();
        for(Object species : listSpecies) {
            speciesBox.getEntries().add((Pokemon)species);
        }
        HBox species = new HBox();
        species.getChildren().addAll(speciesText,speciesBox);

        Button addPokemon = new Button("Add pokemon");
        addPokemon.setOnAction(new EventHandler<ActionEvent>() {
                                   @Override
                                   public void handle(ActionEvent actionEvent) {
                                       controller.addPokemon(speciesBox,userBox,position);
                                   }
                               });

        VBox formRows = new VBox();
        formRows.getChildren().addAll(title,species,addPokemon);

        infoBoxPane.getChildren().setAll(formRows);
    }

    public void displayPokemon(final UserPokemon userPokemon) {
        Text pokemonTitle = new Text(userPokemon.getPokemon().getName());

        Text pokemonImage = new Text("Image would be here");
        GridPane grid = new GridPane();

        Text labelBall = new Text("Pokeball:");
        Text pokemonBall = new Text("Unknown");
        if(userPokemon.getUserPokemonBall() != null) {
            pokemonBall.setText(userPokemon.getUserPokemonBall().getPokeBall().getName());
        }
        grid.add(labelBall,0,0);
        grid.add(pokemonBall,1,0);

        Text labelEgg = new Text("Egg:");
        Text pokemonEgg = new Text("Unknown");
        if(userPokemon.getUserPokemonEgg() != null) {
            pokemonEgg.setText(userPokemon.getUserPokemonEgg().getIsEgg() ? "Is an egg" : "Not an egg");
        }
        grid.add(labelEgg,0,1);
        grid.add(pokemonEgg,1,1);

        Text labelESV = new Text("ESV:");
        Text pokemonESV = new Text("Unknown");
        if(userPokemon.getUserPokemonESV() != null) {
            pokemonESV.setText(Integer.toString(userPokemon.getUserPokemonESV().getESV()));
        }
        grid.add(labelESV,0,2);
        grid.add(pokemonESV,1,2);

        Text labelForm = new Text("Form:");
        Text pokemonForm = new Text("Unknown");
        if(userPokemon.getUserPokemonForm() != null) {
            pokemonForm.setText(userPokemon.getUserPokemonForm().getPokemonForm().getName());
        }
        grid.add(labelForm,0,3);
        grid.add(pokemonForm,1,3);

        Text labelLang = new Text("Language:");
        Text pokemonLang = new Text("Unknown");
        if(userPokemon.getUserPokemonLanguage() != null) {
            pokemonLang.setText(userPokemon.getUserPokemonLanguage().getLanguage().getName());
        }
        grid.add(labelLang,0,4);
        grid.add(pokemonLang,1,4);

        Text labelNature = new Text("Nature:");
        Text pokemonNature = new Text("Unknown");
        if(userPokemon.getUserPokemonNature() != null) {
            pokemonNature.setText(userPokemon.getUserPokemonNature().getNature().getName());
        }
        grid.add(labelNature,0,5);
        grid.add(pokemonNature,1,5);

        Text labelNick = new Text("Nickname:");
        Text pokemonNick = new Text("Unknown");
        if(userPokemon.getUserPokemonNickname() != null) {
            pokemonNick.setText(userPokemon.getUserPokemonNickname().getNickname());
        }
        grid.add(labelNick,0,6);
        grid.add(pokemonNick,1,6);

        Text labelRus = new Text("Pokerus:");
        Text pokemonRus = new Text("Unknown");
        if(userPokemon.getUserPokemonPokerus() != null) {
            pokemonRus.setText(userPokemon.getUserPokemonPokerus().getHasPokerus() ? "Has pokerus" : "Doesn't have pokerus");
        }
        grid.add(labelRus,0,7);
        grid.add(pokemonRus,1,7);

        Text labelSex = new Text("Sex:");
        Text pokemonSex = new Text("Unknown");
        if(userPokemon.getPokemon().getIsGenderless()) {
            pokemonSex.setText("Genderless");
        }
        if (userPokemon.getUserPokemonSex() != null) {
            pokemonSex.setText(userPokemon.getUserPokemonSex().getIsMale() ? "Male" : "Female");
        }
        grid.add(labelSex,0,8);
        grid.add(pokemonSex,1,8);

        Text labelShiny = new Text("Shiny:");
        Text pokemonShiny = new Text("Unknown");
        if (userPokemon.getUserPokemonShiny() != null) {
            pokemonShiny.setText(userPokemon.getUserPokemonShiny().getIsShiny() ? "Is shiny" : "Not shiny");
        }
        grid.add(labelShiny,0,9);
        grid.add(pokemonShiny,1,9);

        Text labelAbility = new Text("Ability:");
        Text pokemonAbility = new Text("Unknown");
        if(userPokemon.getAbility() != null) {
            pokemonAbility.setText(userPokemon.getAbility().getName());
        }
        grid.add(labelAbility,0,10);
        grid.add(pokemonAbility,1,10);

        Text labelLevel = new Text("Level:");
        Text pokemonLevel = new Text("Unknown");
        if(userPokemon.getUserPokemonLevel() != null) {
            pokemonLevel.setText(Integer.toString(userPokemon.getUserPokemonLevel().getLevel()));
        }
        grid.add(labelLevel,0,11);
        grid.add(pokemonLevel,1,11);

        // Create IVs and EVs table
        TableView table = new TableView();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setEditable(true);
        TableColumn attrCol = new TableColumn("Stat");
        attrCol.setCellValueFactory(new PropertyValueFactory<UserPokemonStat,String>("name"));
        TableColumn ivCol = new TableColumn("IV");
        ivCol.setCellValueFactory(new PropertyValueFactory<UserPokemonStat,String>("IV"));
        ivCol.setCellFactory(TextFieldTableCell.forTableColumn());
        ivCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<UserPokemonStat,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<UserPokemonStat,String> cellEditEvent) {
                UserPokemonStat ups = cellEditEvent.getRowValue();
                session.refresh(ups.getUserPokemon());
                UserPokemonIV upi = ups.setIV(cellEditEvent.getNewValue());
                session.saveOrUpdate(upi);
            }
        });
        TableColumn evCol = new TableColumn("EV");
        evCol.setCellValueFactory(new PropertyValueFactory<UserPokemonStat,String>("EV"));
        evCol.setCellFactory(TextFieldTableCell.forTableColumn());
        evCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<UserPokemonStat,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<UserPokemonStat,String> cellEditEvent) {
                UserPokemonStat ups = cellEditEvent.getRowValue();
                session.refresh(ups.getUserPokemon());
                UserPokemonEV upe = ups.setEV(cellEditEvent.getNewValue());
                session.saveOrUpdate(upe);
            }
        });
        TableColumn baseCol = new TableColumn("Base");
        baseCol.setCellValueFactory(new PropertyValueFactory<UserPokemonStat,String>("BaseStat"));
        TableColumn statCol = new TableColumn("Stat value");
        statCol.setCellValueFactory(new PropertyValueFactory<UserPokemonStat,String>("Value"));

        List<Stat> listStats = session.createCriteria(Stat.class).list();
        ObservableList<UserPokemonStat> data = FXCollections.observableArrayList(userPokemon.getUserPokemonStats(listStats));
        table.setItems(data);
        table.setPrefHeight(data.size()*24 + 27);

        table.getColumns().setAll(attrCol, ivCol, evCol, baseCol, statCol);


        VBox rows = new VBox();
        rows.getChildren().addAll(pokemonTitle,pokemonImage,grid,table);

        infoBoxPane.getChildren().setAll(rows);
    }
}

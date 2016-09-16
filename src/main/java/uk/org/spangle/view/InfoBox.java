package uk.org.spangle.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import uk.org.spangle.controller.Controller;
import uk.org.spangle.data.*;
import uk.org.spangle.model.Configuration;

import java.util.List;

public class InfoBox {
    private Pane infoBoxPane;
    private Session session;
    private Configuration conf;
    private Controller controller;

    InfoBox(Pane infoBoxPane, Session session, Configuration conf, Controller controller) {
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
        final AutoCompleteTextField speciesBox = new AutoCompleteTextField(conf);
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
        Pane abilityPane = new HBox();

        addPokeBallRow(grid, 0, userPokemon);
        addEggRow(grid, 1, userPokemon);

        Text labelESV = new Text("ESV:");
        Text pokemonESV = new Text("Unknown");
        if(userPokemon.getUserPokemonESV() != null) {
            pokemonESV.setText(Integer.toString(userPokemon.getUserPokemonESV().getESV()));
        }
        grid.add(labelESV,0,2);
        grid.add(pokemonESV,1,2);

        addFormRow(grid, 3, userPokemon, abilityPane);
        addLanguageRow(grid, 4, userPokemon);
        addNatureRow(grid, 5, userPokemon);

        Text labelNick = new Text("Nickname:");
        Text pokemonNick = new Text("Unknown");
        if(userPokemon.getUserPokemonNickname() != null) {
            pokemonNick.setText(userPokemon.getUserPokemonNickname().getNickname());
        }
        grid.add(labelNick,0,6);
        grid.add(pokemonNick,1,6);

        addPokerusRow(grid, 7, userPokemon);
        addSexRow(grid, 8, userPokemon);
        addShinyRow(grid, 9, userPokemon);
        addAbilityRow(grid, 10, userPokemon, abilityPane);

        Text labelLevel = new Text("Level:");
        Text pokemonLevel = new Text("Unknown");
        if(userPokemon.getUserPokemonLevel() != null) {
            pokemonLevel.setText(Integer.toString(userPokemon.getUserPokemonLevel().getLevel()));
        }
        grid.add(labelLevel,0,11);
        grid.add(pokemonLevel,1,11);

        // Create IVs and EVs table
        //TableView table = createTable(userPokemon);

        VBox rows = new VBox();
        rows.getChildren().addAll(pokemonTitle,pokemonImage,grid);//,table);

        infoBoxPane.getChildren().setAll(rows);
    }

    private void addPokeBallRow(GridPane grid, int row, final UserPokemon userPokemon) {
        Text labelBall = new Text("Pokeball:");
        ComboBox<PokeBall> ballDropdown = new ComboBox<>();
        ballDropdown.setItems(FXCollections.observableArrayList((PokeBall)null));
        for (Object pokeball : session.createCriteria(PokeBall.class).addOrder(Order.asc("name")).list()){
            ballDropdown.getItems().add(((PokeBall)pokeball));
        }
        ballDropdown.setCellFactory(new Callback<ListView<PokeBall>, ListCell<PokeBall>>() {
            @Override
            public ListCell<PokeBall> call(ListView<PokeBall> param) {
                return new ListCell<PokeBall>() {
                    @Override public void updateItem(PokeBall item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item == null) {
                            setGraphic(null);
                            setText("Unknown");
                        } else {
                            Image ballImage = conf.getImagePokeballIcons();
                            ImageView view = new ImageView();
                            view.setImage(ballImage);
                            view.setViewport(new Rectangle2D(item.getSpriteX(),item.getSpriteY(),30,30));
                            setGraphic(view);
                            setText(item.getName());
                        }
                    }
                };
            }
        });
        UserPokemonBall upp = userPokemon.getUserPokemonBall();
        if(upp == null) {
            ballDropdown.setValue(null);
        } else {
            ballDropdown.setValue(upp.getPokeBall());
        }
        ballDropdown.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PokeBall>() {
            @Override
            public void changed(ObservableValue<? extends PokeBall> observableValue, PokeBall old_val, PokeBall new_val) {
                controller.updatePokemonBall(userPokemon, old_val, new_val);
            }
        });
        grid.add(labelBall,0,row);
        grid.add(ballDropdown,1,row);
    }

    private void addEggRow(GridPane grid, int row, final UserPokemon userPokemon) {
        Text labelEgg = new Text("Egg:");
        ChoiceBox<String> eggDropdown = new ChoiceBox<>();
        eggDropdown.setItems(FXCollections.observableArrayList(UserPokemonEgg.UNKNOWN, UserPokemonEgg.IS_EGG, UserPokemonEgg.NOT_EGG));
        eggDropdown.setValue(UserPokemonEgg.UNKNOWN);
        if(userPokemon.getUserPokemonEgg() != null) {
            eggDropdown.setValue(userPokemon.getUserPokemonEgg().getIsEgg() ? UserPokemonEgg.IS_EGG : UserPokemonEgg.NOT_EGG);
        }
        eggDropdown.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old_val, String new_val) {
                controller.updatePokemonEgg(userPokemon, old_val, new_val);
            }
        });
        grid.add(labelEgg,0,row);
        grid.add(eggDropdown,1,row);
    }

    private void addFormRow(GridPane grid, int row, final UserPokemon userPokemon, final Pane abilityPane) {
        Text labelForm = new Text("Form:");
        grid.add(labelForm,0,row);
        // Check if only 1 form exists.
        List<PokemonForm> listForms = userPokemon.getPokemon().getPokemonForms();
        UserPokemonForm upf = userPokemon.getUserPokemonForm();
        if(listForms.size() == 1) {
            Text pokemonForm = new Text(listForms.get(0).getName());
            grid.add(pokemonForm,1,row);
            //Make sure that's set to the truth
            controller.updatePokemonForm(userPokemon, upf==null ? null : upf.getPokemonForm(), listForms.get(0));
            return;
        }
        // Otherwise dropdown
        ComboBox<PokemonForm> formDropdown = new ComboBox<>();
        formDropdown.setItems(FXCollections.observableArrayList((PokemonForm) null));
        formDropdown.getItems().addAll(listForms);
        final Image formImage = conf.getImagePokemonIcons();
        formDropdown.setCellFactory(new Callback<ListView<PokemonForm>, ListCell<PokemonForm>>() {
            @Override
            public ListCell<PokemonForm> call(ListView<PokemonForm> param) {
                return new ListCell<PokemonForm>() {
                    @Override public void updateItem(PokemonForm form, boolean empty) {
                        super.updateItem(form, empty);
                        if(form == null) {
                            setGraphic(null);
                            setText("Unknown");
                        } else {
                            ImageView view = new ImageView();
                            view.setImage(formImage);
                            //Shiny sex
                            UserPokemonShiny ups = userPokemon.getUserPokemonShiny();
                            UserPokemonSex upse = userPokemon.getUserPokemonSex();
                            if(ups != null && ups.getIsShiny()) {
                                if(upse != null && upse.getIsMale()) {
                                    view.setViewport(new Rectangle2D(form.getSpriteShinyMaleX(), form.getSpriteShinyMaleY(), 40, 30));
                                } else {
                                    view.setViewport(new Rectangle2D(form.getSpriteShinyFemaleX(), form.getSpriteShinyFemaleY(), 40, 30));
                                }
                            } else {
                                if(upse != null && upse.getIsMale()) {
                                    view.setViewport(new Rectangle2D(form.getSpriteMaleX(), form.getSpriteMaleY(), 40, 30));
                                } else {
                                    view.setViewport(new Rectangle2D(form.getSpriteFemaleX(), form.getSpriteFemaleY(), 40, 30));
                                }
                            }
                            setGraphic(view);
                            setText(form.getName());
                        }
                    }
                };
            }
        });
        if(upf == null) {
            formDropdown.setValue(null);
        } else {
            formDropdown.setValue(upf.getPokemonForm());
        }
        formDropdown.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PokemonForm>() {
            @Override
            public void changed(ObservableValue<? extends PokemonForm> observableValue, PokemonForm old_val, PokemonForm new_val) {
                //Update ability dropdown
                updateAbilityDropdown(userPokemon,abilityPane);
                controller.updatePokemonForm(userPokemon, old_val, new_val);
            }
        });
        grid.add(formDropdown,1,row);
    }

    private void addLanguageRow(GridPane grid, int row, final UserPokemon userPokemon) {
        Text labelLang = new Text("Language:");
        List languageList = session.createCriteria(Language.class).list();
        ChoiceBox<Language> langDropdown = new ChoiceBox<>(FXCollections.observableArrayList((Language)null));
        for(Object language : languageList) {
            langDropdown.getItems().add((Language) language);
        }
        langDropdown.setValue(null);
        if(userPokemon.getUserPokemonLanguage() != null) {
            langDropdown.setValue(userPokemon.getUserPokemonLanguage().getLanguage());
        }
        langDropdown.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Language>() {
            @Override
            public void changed(ObservableValue<? extends Language> observableValue, Language old_val, Language new_val) {
                controller.updatePokemonLanguage(userPokemon, old_val, new_val);
            }
        });
        grid.add(labelLang,0,row);
        grid.add(langDropdown,1,row);
    }

    private void addNatureRow(GridPane grid, int row, final UserPokemon userPokemon) {
        Text labelNature = new Text("Nature:");
        List natureList = session.createCriteria(Nature.class).list();
        ChoiceBox<Nature> natureDropdown = new ChoiceBox<>(FXCollections.observableArrayList((Nature)null));
        for(Object nature : natureList) {
            natureDropdown.getItems().add((Nature) nature);
        }
        natureDropdown.setValue(null);
        if(userPokemon.getUserPokemonNature() != null) {
            natureDropdown.setValue(userPokemon.getUserPokemonNature().getNature());
        }
        natureDropdown.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Nature>() {
            @Override
            public void changed(ObservableValue<? extends Nature> observableValue, Nature old_val, Nature new_val) {
                controller.updatePokemonNature(userPokemon, old_val, new_val);
            }
        });
        grid.add(labelNature,0,row);
        grid.add(natureDropdown,1,row);
    }

    private void addPokerusRow(GridPane grid, int row, final UserPokemon userPokemon) {

        Text labelRus = new Text("Pokerus:");
        ChoiceBox<String> rusDropdown = new ChoiceBox<>();
        rusDropdown.setItems(FXCollections.observableArrayList(UserPokemonPokerus.UNKNOWN, UserPokemonPokerus.HAS_POKERUS, UserPokemonPokerus.NOT_POKERUS));
        rusDropdown.setValue(UserPokemonPokerus.UNKNOWN);
        if(userPokemon.getUserPokemonPokerus() != null) {
            rusDropdown.setValue(userPokemon.getUserPokemonPokerus().getHasPokerus() ? UserPokemonPokerus.HAS_POKERUS : UserPokemonPokerus.NOT_POKERUS);
        }
        rusDropdown.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old_val, String new_val) {
                controller.updatePokemonPokerus(userPokemon, old_val, new_val);
            }
        });
        grid.add(labelRus,0,row);
        grid.add(rusDropdown,1,row);
    }

    private void addSexRow(GridPane grid, int row, final UserPokemon userPokemon) {

        Text labelSex = new Text("Sex:");
        grid.add(labelSex,0,row);
        if(userPokemon.getPokemon().getIsGenderless()) {
            Text pokemonSex = new Text(UserPokemonSex.GENDERLESS);
            grid.add(pokemonSex,1,row);
            return;
        }
        ChoiceBox<String> sexDropdown = new ChoiceBox<>();
        sexDropdown.setItems(FXCollections.observableArrayList(UserPokemonSex.UNKNOWN, UserPokemonSex.MALE, UserPokemonSex.FEMALE));
        sexDropdown.setValue(UserPokemonSex.UNKNOWN);
        if(userPokemon.getUserPokemonSex() != null) {
            sexDropdown.setValue(userPokemon.getUserPokemonSex().getIsMale() ? UserPokemonSex.MALE : UserPokemonSex.FEMALE);
        }
        sexDropdown.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old_val, String new_val) {
                controller.updatePokemonSex(userPokemon, old_val, new_val);
            }
        });
        grid.add(sexDropdown,1,row);
    }

    private void addShinyRow(GridPane grid, int row, final UserPokemon userPokemon) {

        Text labelShiny = new Text("Shiny:");
        ChoiceBox<String> shinyDropdown = new ChoiceBox<>();
        shinyDropdown.setItems(FXCollections.observableArrayList(UserPokemonShiny.UNKNOWN, UserPokemonShiny.IS_SHINY, UserPokemonShiny.NOT_SHINY));
        shinyDropdown.setValue(UserPokemonShiny.UNKNOWN);
        if(userPokemon.getUserPokemonShiny() != null) {
            shinyDropdown.setValue(userPokemon.getUserPokemonShiny().getIsShiny() ? UserPokemonShiny.IS_SHINY : UserPokemonShiny.NOT_SHINY);
        }
        shinyDropdown.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old_val, String new_val) {
                controller.updatePokemonShiny(userPokemon, old_val, new_val);
            }
        });
        grid.add(labelShiny,0,row);
        grid.add(shinyDropdown,1,row);
    }

    private void addAbilityRow(GridPane grid, int row, final UserPokemon userPokemon, Pane abilityPane) {
        Text labelAbility = new Text("Ability:");
        grid.add(labelAbility,0,row);
        grid.add(abilityPane,1,row);
        updateAbilityDropdown(userPokemon, abilityPane);
    }

    private void updateAbilityDropdown(final UserPokemon userPokemon, final Pane abilityPane) {

        // If form unknown, output unknown
        UserPokemonForm upf = userPokemon.getUserPokemonForm();
        if(upf == null) {
            Text pokemonAbility = new Text("Unknown");
            abilityPane.getChildren().setAll(pokemonAbility);
            return;
        }

        // If only one ability for this form, display that.
        List<PokemonFormAbility> listAbilities = upf.getPokemonForm().getPokemonFormAbilities();
        if(listAbilities.size() == 1) {
            //Ensure that current ability is this.
            controller.updatePokemonAbility(userPokemon, null, listAbilities.get(0));
            Text pokemonAbility = new Text(listAbilities.get(0).getAbility().getName());
            abilityPane.getChildren().setAll(pokemonAbility);
            return;
        }

        // Dropdown for ability
        ChoiceBox<PokemonFormAbility> abilityDropdown = new ChoiceBox<>();
        abilityDropdown.setItems(FXCollections.observableArrayList((PokemonFormAbility)null));
        abilityDropdown.getItems().addAll(listAbilities);
        if(userPokemon.getUserPokemonAbilitySlot() == null) {
            abilityDropdown.setValue(null);
        } else {
            for(PokemonFormAbility pfAbility : listAbilities) {
                if(pfAbility.getAbilitySlot() == userPokemon.getUserPokemonAbilitySlot().getAbilitySlot()) {
                    abilityDropdown.setValue(pfAbility);
                }
            }
        }
        abilityDropdown.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PokemonFormAbility>() {
            @Override
            public void changed(ObservableValue<? extends PokemonFormAbility> observable, PokemonFormAbility oldVal, PokemonFormAbility newval) {
                controller.updatePokemonAbility(userPokemon, oldVal, newval);
            }
        });
        abilityPane.getChildren().setAll(abilityDropdown);
    }

    /*

    private TableView createTable(UserPokemon userPokemon) {
        TableView table = new TableView();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setEditable(true);
        TableColumn attrCol = new TableColumn("Stat");
        attrCol.setCellValueFactory(new PropertyValueFactory<UserPokemonStat,String>("name"));
        // Set up IV column
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
                cellEditEvent.getTableView().getColumns().get(0).setVisible(false);
                cellEditEvent.getTableView().getColumns().get(0).setVisible(true);
            }
        });
        // Set up EV column
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
                cellEditEvent.getTableView().getColumns().get(0).setVisible(false);
                cellEditEvent.getTableView().getColumns().get(0).setVisible(true);
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
        return table;
    }
    */
}

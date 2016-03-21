package uk.org.spangle.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import uk.org.spangle.data.Pokemon;
import uk.org.spangle.data.PokemonForm;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is a TextField which implements an "autocomplete" functionality,
 * based on a supplied list of entries.<p>
 *
 * If the entered text matches a part of any of the supplied entries these are
 * going to be displayed in a popup.
 * .<p></p>
 *
 * @author Caleb Brinkman
 * @author Fabian Ochmann
 */
public class AutoCompleteTextField extends TextField {

    /**
     * The existing autocomplete entries.
     */
    private final SortedSet<Pokemon> entries;

    /**
     * The popup used to select an entry.
     */
    private ContextMenu entriesPopup;

    /**
     * The maximum Number of entries displayed in the popup.<br>
     * Default: 10
     */
    private static final int MAX_ENTRIES = 10;

    private Pokemon selectedPokemon;

    /**
     * Construct a new AutoCompleteTextField.
     */
    public AutoCompleteTextField() {
        super();
        this.entries = new TreeSet<>();
        entriesPopup = new ContextMenu();
        textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                if (getText().length() == 0) {
                    entriesPopup.hide();
                } else {
                    LinkedList<Pokemon> searchResult = new LinkedList<>();

                    //Check if the entered Text is part of some entry
                    String text = getText();
                    Pattern pattern = Pattern.compile(".*" + text + ".*", Pattern.CASE_INSENSITIVE);

                    for (Pokemon entry : entries) {
                        Matcher matcher = pattern.matcher(entry.getName());
                        if (matcher.matches()) {
                            searchResult.add(entry);
                        }
                    }

                    if (entries.size() > 0) {
                        populatePopup(searchResult);
                        if (!entriesPopup.isShowing()) {
                            entriesPopup.show(AutoCompleteTextField.this, Side.BOTTOM, 0, 0);
                        }
                    } else {
                        entriesPopup.hide();
                    }
                }
            }
        });

        focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
                entriesPopup.hide();
            }
        });

    }

    /**
     * Get the existing set of autocomplete entries.
     *
     * @return The existing autocomplete entries.
     */
    public SortedSet<Pokemon> getEntries() {
        return entries;
    }

    /**
     * Populate the entry set with the given search results. Display is limited
     * to 10 entries, for performance.
     *
     * @param searchResult The set of matching strings.
     */
    private void populatePopup(List<Pokemon> searchResult) {
        List<CustomMenuItem> menuItems = new LinkedList<>();
        int count = Math.min(searchResult.size(), MAX_ENTRIES);
        File imageFile = new File("pokemon-icons.png");
        Image image = new Image(imageFile.toURI().toString());
        for (int i = 0; i < count; i++) {
            final Pokemon pokemon = searchResult.get(i);
            PokemonForm form = pokemon.getPokemonForms().get(0);
            final String result = pokemon.getName();
            Label entryLabel = new Label(result);
            Canvas entrySprite = new Canvas(30,30);
            GraphicsContext ctx = entrySprite.getGraphicsContext2D();
            ctx.drawImage(image,form.getSpriteFemaleX(),form.getSpriteFemaleY(),40,30,-5,0,40,30);
            HBox entry = new HBox();
            entry.getChildren().setAll(entrySprite,entryLabel);
            CustomMenuItem item = new CustomMenuItem(entry, true);
            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    setText(result);
                    selectedPokemon = pokemon;
                    entriesPopup.hide();
                }
            });
            menuItems.add(item);
        }
        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);

    }

    public Pokemon getSelectedPokemon() {
        return selectedPokemon;
    }

}
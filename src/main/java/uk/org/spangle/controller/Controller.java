package uk.org.spangle.controller;

import javafx.scene.input.MouseEvent;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import uk.org.spangle.data.*;
import uk.org.spangle.model.Configuration;
import uk.org.spangle.view.App;
import uk.org.spangle.view.AutoCompleteTextField;

import java.util.List;

public class Controller {
    Session session;
    Configuration conf;
    App app;

    public Controller(Session session, Configuration conf, App app) {
        this.session = session;
        this.conf = conf;
        this.app = app;
    }

    public void updateGame(UserGame value) {
        conf.setCurrentGame(value);
        app.getSideBar().setGame(value);
    }

    public void updateBox(UserGame currentGame, UserBox currentBox) {
        currentGame.setCurrentBox(currentBox);
        session.update(currentGame);
        session.flush();
        app.getSideBar().updateBoxCanvas();
    }

    public void prevBox(UserGame currentGame) {
        UserBox currentBox = currentGame.getCurrentBox();
        List<UserBox> listBoxes = currentGame.getUserBoxes();
        int index = listBoxes.indexOf(currentBox);
        // Remove 1 from index and wrap it around
        int newIndex = ((index-1) % listBoxes.size() + listBoxes.size()) % listBoxes.size();
        UserBox newBox = listBoxes.get(newIndex);
        currentGame.setCurrentBox(newBox);
        session.update(currentGame);
        app.getSideBar().updateBoxDropdown();
        app.getSideBar().updateBoxCanvas();
    }

    public void nextBox(UserGame currentGame) {
        UserBox currentBox = currentGame.getCurrentBox();
        List<UserBox> listBoxes = currentGame.getUserBoxes();
        int index = listBoxes.indexOf(currentBox);
        int newIndex = (index+1) % listBoxes.size();
        UserBox newBox = listBoxes.get(newIndex);
        currentGame.setCurrentBox(newBox);
        session.update(currentGame);
        app.getSideBar().updateBoxDropdown();
        app.getSideBar().updateBoxCanvas();
    }

    public void clickCanvas(MouseEvent t) {
        int x = (int) t.getX();
        int y = (int) t.getY();
        int box_x = x/30;
        int box_y = y/30;
        UserBox userBox = conf.getCurrentGame().getCurrentBox();
        int position = box_y*(userBox.getColumns()) + box_x + 1;
        List list = session.createCriteria(UserPokemon.class).add(Restrictions.eq("userBox",userBox)).add(Restrictions.eq("position",position)).list();
        if(list.size() == 0) {
            System.out.println("No pokemon here");
            clickCanvasEmpty(position);
        } else {
            UserPokemon userPokemon = (UserPokemon) list.get(0);
            UserPokemonNickname upn = userPokemon.getUserPokemonNickname();
            if(upn == null) {
                System.out.println("No nickname.");
            } else {
                System.out.println(upn.getNickname());
            }
            clickCanvasPokemon(userPokemon);
        }

    }

    public void clickCanvasEmpty(int position) {
        UserBox userBox = conf.getCurrentGame().getCurrentBox();
        app.getInfoBox().addNewPokemon(userBox,position);
    }

    public void clickCanvasPokemon(UserPokemon userPokemon) {
        app.getInfoBox().displayPokemon(userPokemon);
    }

    public void addPokemon(AutoCompleteTextField speciesBox, UserBox userBox, int position) {
        Pokemon pokemon = speciesBox.getSelectedPokemon();
        if(pokemon == null) {
            String pokemonName = speciesBox.getText();
            pokemon = (Pokemon) session.createCriteria(Pokemon.class).add(Restrictions.eq("name",pokemonName)).uniqueResult();
            if(pokemon == null) {
                System.out.println("Invalid!");
                return;
            }
        }
        UserPokemon userPokemon = new UserPokemon(userBox,position,pokemon);
        session.save(userPokemon);
        session.refresh(userBox);
        app.getSideBar().updateBoxCanvas();
        app.getInfoBox().displayPokemon(userPokemon);
    }

    public void updatePokemonBall(UserPokemon userPokemon, PokeBall old_val, PokeBall new_val) {
        if(new_val == null) {
            userPokemon.setUserPokemonBall(null);
        } else {
            UserPokemonBall upb = new UserPokemonBall(userPokemon, new_val);
            userPokemon.setUserPokemonBall(upb);
        }
    }

    public void updatePokemonEgg(UserPokemon userPokemon, String old_val, String new_val) {
        if(new_val.equals(UserPokemonEgg.UNKNOWN)) {
            userPokemon.setUserPokemonEgg(null);
        } else {
            UserPokemonEgg upe = new UserPokemonEgg(userPokemon, new_val.equals(UserPokemonEgg.IS_EGG));
            userPokemon.setUserPokemonEgg(upe);
        }
        if(conf.getHideEggs()) return;
        if(new_val.equals(UserPokemonEgg.IS_EGG) != old_val.equals(UserPokemonEgg.IS_EGG)) app.getSideBar().updateBoxCanvas();
    }
}

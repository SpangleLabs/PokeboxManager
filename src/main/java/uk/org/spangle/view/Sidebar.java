package uk.org.spangle.view;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import uk.org.spangle.data.UserGame;

import java.util.ArrayList;
import java.util.List;

public class SideBar {
    Pane sideBarPane;

    public SideBar(Pane sideBarPane) {
        this.sideBarPane = sideBarPane;
    }

    public void update() {
        Text titleText = new Text("Game");
        ChoiceBox<String> gameDropdown = createGameDropdown();
        Text boxText = new Text("Box");

        Button boxLeft = new Button("<");
        ChoiceBox<String> boxDropdown = new ChoiceBox<>();
        boxDropdown.setItems(FXCollections.observableArrayList("A","B","C"));
        Button boxRight = new Button(">");
        HBox boxButtons = new HBox();
        boxButtons.getChildren().addAll(boxLeft,boxDropdown,boxRight);

        // Add everything to the sidebar
        VBox sideBarVBox = new VBox();
        sideBarVBox.getChildren().addAll(titleText,gameDropdown,boxText,boxButtons);

        sideBarPane.getChildren().add(sideBarVBox);
    }

    public ChoiceBox<String> createGameDropdown() {

        SessionFactory sessionFactory = new Configuration().configure(getClass().getResource("/hibernate.cfg.xml")).buildSessionFactory();
        Session session = sessionFactory.openSession();

        // now lets pull events from the database and list them
        session.beginTransaction();
        List result = session.createQuery("from UserGame").list();
        List<String> nameList = new ArrayList<>();
        for (Object obj : result) {
            UserGame event = (UserGame) obj;
            System.out.println(event.getName());
            nameList.add(event.getName());
        }
        session.getTransaction().commit();
        session.close();

        sessionFactory.close();

        ChoiceBox<String> gameDropdown = new ChoiceBox<>();
        gameDropdown.setItems(FXCollections.observableArrayList(nameList));
        gameDropdown.setValue(nameList.get(0));
        return gameDropdown;
    }
}

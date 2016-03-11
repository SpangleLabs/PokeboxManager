package uk.org.spangle.data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_games")
public class UserGame {

    private int id;
    private int generationId;
    private String name;
    private int ordinal;
    private List<UserBox> userBoxes;
    private UserBox currentBox;

    public UserGame() {
        // this form used by Hibernate
    }

    public UserGame(int id, int generationId, String name, int ordinal) {
        this.id = id;
        this.generationId = generationId;
        this.name = name;
        this.ordinal = ordinal;
    }

    public UserGame(Generation gen, String name, int ordinal) {
        this.generationId = gen.getId();
        this.name = name;
        this.ordinal = ordinal;
        this.userBoxes = new ArrayList<UserBox>();
        for(GenerationBox box : gen.getGenerationBoxes()) {
            UserBox newBox = new UserBox(this,box,box.getOrdinal());
            this.userBoxes.add(newBox);
        }
        this.currentBox = this.userBoxes.get(0);
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="user_game_id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "generation_id")
    public int getGenerationId() {
        return generationId;
    }

    public void setGenerationId(int generationId) {
        this.generationId = generationId;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "ordinal")
    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    @OneToMany(mappedBy="userGame")
    @OrderBy("ordinal")
    public List<UserBox> getUserBoxes() {
        return userBoxes;
    }

    public void setUserBoxes(List<UserBox> userBoxes) {
        this.userBoxes = userBoxes;
    }

    @OneToOne()
    @JoinColumn(name="current_box_id")
    public UserBox getCurrentBox() {
        return currentBox;
    }

    public void setCurrentBox(UserBox currentBox) {
        this.currentBox = currentBox;
    }
}
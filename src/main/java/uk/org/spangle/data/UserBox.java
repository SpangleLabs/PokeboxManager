package uk.org.spangle.data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_boxes")
public class UserBox {

    private int id;
    //private int userGameId;
    private UserGame userGame;
    private int ordinal;
    private int size;
    private int columns;
    private String name;
    private List<UserPokemon> userPokemons;

    public UserBox() {
        // this form used by Hibernate
    }

    public UserBox(int id, int userGameId, int ordinal, int size, int columns, String name) {
        this.id = id;
        //this.userGameId = userGameId;
        this.ordinal = ordinal;
        this.size = size;
        this.columns = columns;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="user_box_id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name="user_game_id", updatable=false)
    public UserGame getUserGame() {
        return userGame;
    }

    public void setUserGame(UserGame userGame) {
        this.userGame = userGame;
    }

    @Column(name = "ordinal")
    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    @Column(name = "size")
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Column(name = "columns")
    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy="userPokemon")
    @OrderBy("position")
    public List<UserPokemon> getUserPokemons() {
        return userPokemons;
    }

    public void setUserPokemons(List<UserPokemon> userPokemons) {
        this.userPokemons = userPokemons;
    }
}
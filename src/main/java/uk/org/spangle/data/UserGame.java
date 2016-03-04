package uk.org.spangle.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_games")
public class UserGame {

    private int id;
    private int generationId;
    private String name;
    private int ordinal;

    public UserGame() {
        // this form used by Hibernate
    }

    public UserGame(int id, int generationId, String name, int ordinal) {
        this.id = id;
        this.generationId = generationId;
        this.name = name;
        this.ordinal = ordinal;
    }

    @Id
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
}
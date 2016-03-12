package uk.org.spangle.data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "user_pokemon_esv")
public class UserPokemonESV {

    private int id;
    private UserPokemon userPokemon;
    private int esv;
    private Timestamp timestamp;

    public UserPokemonESV() {
        // this form used by Hibernate
    }

    public UserPokemonESV(UserPokemon userPokemon, int esv) {
        this.id = userPokemon.getId();
        this.userPokemon = userPokemon;
        this.esv = esv;
        Date date = new Date();
        this.timestamp = new Timestamp(date.getTime());
    }

    @Id
    //@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="user_pokemon_id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @OneToOne
    @JoinColumn(name="user_pokemon_id")
    public UserPokemon getUserPokemon() {
        return userPokemon;
    }

    public void setUserPokemon(UserPokemon userPokemon) {
        this.userPokemon = userPokemon;
    }

    @Column(name = "esv")
    public int getESV() {
        return esv;
    }

    public void setESV(int esv) {
        this.esv = esv;
    }

    @Column(name = "timestamp")
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
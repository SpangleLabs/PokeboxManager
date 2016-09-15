package uk.org.spangle.data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "user_pokemon_egg")
public class UserPokemonEgg {

    public static final String UNKNOWN = "Unknown";
    public static final String IS_EGG = "Is an egg";
    public static final String NOT_EGG = "Not an egg";

    private int id;
    private UserPokemon userPokemon;
    private boolean isEgg;
    private Timestamp timestamp;

    public UserPokemonEgg() {
        // this form used by Hibernate
    }

    public UserPokemonEgg(UserPokemon userPokemon, boolean isEgg) {
        this.id = userPokemon.getId();
        this.userPokemon = userPokemon;
        this.isEgg = isEgg;
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

    @Column(name = "is_egg")
    public boolean getIsEgg() {
        return isEgg;
    }

    public void setIsEgg(boolean isEgg) {
        this.isEgg = isEgg;
    }

    @Column(name = "timestamp")
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
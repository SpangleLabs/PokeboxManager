package uk.org.spangle.data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "user_pokemon_shiny")
public class UserPokemonShiny {

    public static final String UNKNOWN = "Unknown";
    public static final String IS_SHINY = "Is shiny";
    public static final String NOT_SHINY = "Not shiny";

    private int id;
    private UserPokemon userPokemon;
    private boolean isShiny;
    private Timestamp timestamp;

    public UserPokemonShiny() {
        // this form used by Hibernate
    }

    public UserPokemonShiny(UserPokemon userPokemon, boolean isShiny) {
        this.id = userPokemon.getId();
        this.userPokemon = userPokemon;
        this.isShiny = isShiny;
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

    @Column(name = "is_shiny")
    public boolean getIsShiny() {
        return isShiny;
    }

    public void setIsShiny(boolean isShiny) {
        this.isShiny = isShiny;
    }

    @Column(name = "timestamp")
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
package uk.org.spangle.data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "user_pokemon_pokerus")
public class UserPokemonPokerus {

    public static final String UNKNOWN = "Unknown";
    public static final String HAS_POKERUS = "Has pokerus";
    public static final String NOT_POKERUS = "Doesn't have pokerus";

    private int id;
    private UserPokemon userPokemon;
    private boolean hasPokerus;
    private Timestamp timestamp;

    public UserPokemonPokerus() {
        // this form used by Hibernate
    }

    public UserPokemonPokerus(UserPokemon userPokemon, boolean hasPokerus) {
        this.id = userPokemon.getId();
        this.userPokemon = userPokemon;
        this.hasPokerus = hasPokerus;
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

    @Column(name = "has_pokerus")
    public boolean getHasPokerus() {
        return hasPokerus;
    }

    public void setHasPokerus(boolean hasPokerus) {
        this.hasPokerus = hasPokerus;
    }

    @Column(name = "timestamp")
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
package uk.org.spangle.data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "user_pokemon_ball")
public class UserPokemonBall {

    private int id;
    private UserPokemon userPokemon;
    private PokeBall pokeBall;
    private Timestamp timestamp;

    public UserPokemonBall() {
        // this form used by Hibernate
    }

    public UserPokemonBall(UserPokemon userPokemon, PokeBall pokeBall) {
        this.id = userPokemon.getId();
        this.userPokemon = userPokemon;
        this.pokeBall = pokeBall;
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

    @ManyToOne
    @JoinColumn(name="pokeball_id")
    public PokeBall getPokeBall() {
        return pokeBall;
    }

    public void setPokeBall(PokeBall pokeBall) {
        this.pokeBall = pokeBall;
    }

    @Column(name = "timestamp")
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
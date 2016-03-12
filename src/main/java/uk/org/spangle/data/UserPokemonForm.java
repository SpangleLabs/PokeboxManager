package uk.org.spangle.data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "user_pokemon_form")
public class UserPokemonForm {

    private int id;
    private UserPokemon userPokemon;
    private PokemonForm pokemonForm;
    private Timestamp timestamp;

    public UserPokemonForm() {
        // this form used by Hibernate
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
    @JoinColumn(name="pokemon_form_id")
    public PokemonForm getPokemonForm() {
        return pokemonForm;
    }

    public void setPokemonForm(PokemonForm pokemonForm) {
        this.pokemonForm = pokemonForm;
    }

    @Column(name = "timestamp")
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
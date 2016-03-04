package uk.org.spangle.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_pokemon")
public class UserPokemon {

    private int id;
    private int userBoxId;
    private int position;
    private int pokemonId;
    private String nickname;

    public UserPokemon() {
        // this form used by Hibernate
    }

    public UserPokemon(int id, int userBoxId, int position, int pokemonId, String nickname) {
        this.id = id;
        this.userBoxId = userBoxId;
        this.position = position;
        this.pokemonId = pokemonId;
        this.nickname = nickname;
    }

    @Id
    @Column(name="user_pokemon_id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "user_box_id")
    public int getUserBoxId() {
        return userBoxId;
    }

    public void setUserBoxId(int userBoxId) {
        this.userBoxId = userBoxId;
    }

    @Column(name = "position")
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Column(name = "pokemon_id")
    public int getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(int pokemonId) {
        this.pokemonId = pokemonId;
    }

    @Column(name = "nickname")
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
package uk.org.spangle.data;

import javax.persistence.*;

@Entity
@Table(name = "user_pokemon")
public class UserPokemon {

    private int id;
    private UserBox userBox;
    private int position;
    private int pokemonId;
    private String nickname;

    public UserPokemon() {
        // this form used by Hibernate
    }

    public UserPokemon(int id, int position, int pokemonId, String nickname) {
        this.id = id;
        this.position = position;
        this.pokemonId = pokemonId;
        this.nickname = nickname;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="user_pokemon_id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name="user_box_id")
    public UserBox getUserBox() {
        return userBox;
    }

    public void setUserBox(UserBox userBox) {
        this.userBox = userBox;
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
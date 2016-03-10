package uk.org.spangle.data;

import javax.persistence.*;

@Entity
@Table(name = "user_pokemon")
public class UserPokemon {

    private int id;
    private UserBox userBox;
    private int position;
    private Pokemon pokemon;
    private String nickname;
    private UserPokemonForm userPokemonForm;

    public UserPokemon() {
        // this form used by Hibernate
    }

    public UserPokemon(int id, int position, String nickname) {
        this.id = id;
        this.position = position;
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

    @ManyToOne
    @JoinColumn(name="pokemon_id")
    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    @Column(name = "nickname")
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @OneToOne(mappedBy="userPokemon")
    public UserPokemonForm getUserPokemonForm() {
        return userPokemonForm;
    }

    public void setUserPokemonForm(UserPokemonForm userPokemonForm) {
        this.userPokemonForm = userPokemonForm;
    }
}
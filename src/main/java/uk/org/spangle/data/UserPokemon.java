package uk.org.spangle.data;

import javax.persistence.*;

@Entity
@Table(name = "user_pokemon")
public class UserPokemon {

    private int id;
    private UserBox userBox;
    private int position;
    private Pokemon pokemon;
    private UserPokemonForm userPokemonForm;
    private UserPokemonNature userPokemonNature;
    private UserPokemonNickname userPokemonNickname;
    private UserPokemonSex userPokemonSex;

    public UserPokemon() {
        // this form used by Hibernate
    }

    public UserPokemon(int id, UserBox userBox, int position, Pokemon pokemon) {
        this.id = id;
        this.userBox = userBox;
        this.position = position;
        this.pokemon = pokemon;
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

    @OneToOne(mappedBy="userPokemon")
    public UserPokemonForm getUserPokemonForm() {
        return userPokemonForm;
    }

    public void setUserPokemonForm(UserPokemonForm userPokemonForm) {
        this.userPokemonForm = userPokemonForm;
    }

    @OneToOne(mappedBy="userPokemon")
    public UserPokemonNature getUserPokemonNature() {
        return userPokemonNature;
    }

    public void setUserPokemonNature(UserPokemonNature userPokemonNature) {
        this.userPokemonNature = userPokemonNature;
    }

    @OneToOne(mappedBy="userPokemon")
    public UserPokemonNickname getUserPokemonNickname() {
        return userPokemonNickname;
    }

    public void setUserPokemonNickname(UserPokemonNickname userPokemonNickname) {
        this.userPokemonNickname = userPokemonNickname;
    }

    @OneToOne(mappedBy="userPokemon")
    public UserPokemonSex getUserPokemonSex() {
        return userPokemonSex;
    }

    public void setUserPokemonSex(UserPokemonSex userPokemonSex) {
        this.userPokemonSex = userPokemonSex;
    }
}
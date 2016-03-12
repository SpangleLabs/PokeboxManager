package uk.org.spangle.data;

import javax.persistence.*;

@Entity
@Table(name = "user_pokemon")
public class UserPokemon {

    private int id;
    private UserBox userBox;
    private int position;
    private Pokemon pokemon;
    private UserPokemonBall userPokemonBall;
    private UserPokemonEgg userPokemonEgg;
    private UserPokemonESV userPokemonESV;
    private UserPokemonForm userPokemonForm;
    private UserPokemonNature userPokemonNature;
    private UserPokemonNickname userPokemonNickname;
    private UserPokemonPokerus userPokemonPokerus;
    private UserPokemonSex userPokemonSex;

    public UserPokemon() {
        // this form used by Hibernate
    }

    public UserPokemon(UserBox userBox, int position, Pokemon pokemon) {
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
    public UserPokemonBall getUserPokemonBall() {
        return userPokemonBall;
    }

    public void setUserPokemonBall(UserPokemonBall userPokemonBall) {
        this.userPokemonBall = userPokemonBall;
    }

    @OneToOne(mappedBy="userPokemon")
    public UserPokemonEgg getUserPokemonEgg() {
        return userPokemonEgg;
    }

    public void setUserPokemonEgg(UserPokemonEgg userPokemonEgg) {
        this.userPokemonEgg = userPokemonEgg;
    }

    @OneToOne(mappedBy="userPokemon")
    public UserPokemonESV getUserPokemonESV() {
        return userPokemonESV;
    }

    public void setUserPokemonESV(UserPokemonESV userPokemonESV) {
        this.userPokemonESV = userPokemonESV;
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
    public UserPokemonPokerus getUserPokemonPokerus() {
        return userPokemonPokerus;
    }

    public void setUserPokemonPokerus(UserPokemonPokerus userPokemonPokerus) {
        this.userPokemonPokerus = userPokemonPokerus;
    }

    @OneToOne(mappedBy="userPokemon")
    public UserPokemonSex getUserPokemonSex() {
        return userPokemonSex;
    }

    public void setUserPokemonSex(UserPokemonSex userPokemonSex) {
        this.userPokemonSex = userPokemonSex;
    }
}
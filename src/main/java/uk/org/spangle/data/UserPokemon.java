package uk.org.spangle.data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_pokemon")
public class UserPokemon {

    private int id;
    private UserBox userBox;
    private int position;
    private Pokemon pokemon;
    private UserPokemonAbilitySlot userPokemonAbilitySlot;
    private UserPokemonBall userPokemonBall;
    private UserPokemonEgg userPokemonEgg;
    private UserPokemonESV userPokemonESV;
    private List<UserPokemonEV> userPokemonEVs;
    private UserPokemonForm userPokemonForm;
    private List<UserPokemonIV> userPokemonIVs;
    private UserPokemonLanguage userPokemonLanguage;
    private UserPokemonLevel userPokemonLevel;
    private List<UserPokemonMove> userPokemonMoves;
    private UserPokemonNature userPokemonNature;
    private UserPokemonNickname userPokemonNickname;
    private UserPokemonNote userPokemonNote;
    private UserPokemonPokerus userPokemonPokerus;
    private UserPokemonSex userPokemonSex;
    private UserPokemonShiny userPokemonShiny;
    private List<UserPokemonTag> userPokemonTags;

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
    public UserPokemonAbilitySlot getUserPokemonAbilitySlot() {
        return userPokemonAbilitySlot;
    }

    public void setUserPokemonAbilitySlot(UserPokemonAbilitySlot userPokemonAbilitySlot) {
        this.userPokemonAbilitySlot = userPokemonAbilitySlot;
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

    @OneToMany(mappedBy="userPokemon")
    public List<UserPokemonEV> getUserPokemonEVs() {
        return userPokemonEVs;
    }

    public void setUserPokemonEVs(List<UserPokemonEV> userPokemonEVs) {
        this.userPokemonEVs = userPokemonEVs;
    }

    @OneToOne(mappedBy="userPokemon")
    public UserPokemonForm getUserPokemonForm() {
        return userPokemonForm;
    }

    public void setUserPokemonForm(UserPokemonForm userPokemonForm) {
        this.userPokemonForm = userPokemonForm;
    }

    @OneToMany(mappedBy="userPokemon")
    public List<UserPokemonIV> getUserPokemonIVs() {
        return userPokemonIVs;
    }

    public void setUserPokemonIVs(List<UserPokemonIV> userPokemonIVs) {
        this.userPokemonIVs = userPokemonIVs;
    }

    @OneToOne(mappedBy="userPokemon")
    public UserPokemonLanguage getUserPokemonLanguage() {
        return userPokemonLanguage;
    }

    public void setUserPokemonLanguage(UserPokemonLanguage userPokemonLanguage) {
        this.userPokemonLanguage = userPokemonLanguage;
    }

    @OneToOne(mappedBy="userPokemon")
    public UserPokemonLevel getUserPokemonLevel() {
        return userPokemonLevel;
    }

    public void setUserPokemonLevel(UserPokemonLevel userPokemonLevel) {
        this.userPokemonLevel = userPokemonLevel;
    }

    @OneToMany(mappedBy="userPokemon")
    public List<UserPokemonMove> getUserPokemonMoves() {
        return userPokemonMoves;
    }

    public void setUserPokemonMoves(List<UserPokemonMove> userPokemonMoves) {
        this.userPokemonMoves = userPokemonMoves;
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
    public UserPokemonNote getUserPokemonNote() {
        return userPokemonNote;
    }

    public void setUserPokemonNote(UserPokemonNote userPokemonNote) {
        this.userPokemonNote = userPokemonNote;
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

    @OneToOne(mappedBy="userPokemon")
    public UserPokemonShiny getUserPokemonShiny() {
        return userPokemonShiny;
    }

    public void setUserPokemonShiny(UserPokemonShiny userPokemonShiny) {
        this.userPokemonShiny = userPokemonShiny;
    }

    @OneToMany(mappedBy="userPokemon")
    public List<UserPokemonTag> getUserPokemonTags() {
        return userPokemonTags;
    }

    public void setUserPokemonTags(List<UserPokemonTag> userPokemonTags) {
        this.userPokemonTags = userPokemonTags;
    }



    // NON-HIBERNATE METHODS BELOW
    @Transient
    public int getSpriteX() {
        // Get form
        PokemonForm form = pokemon.getPokemonForms().get(0);
        if(userPokemonForm != null) {
            form = userPokemonForm.getPokemonForm();
        }
        // Figure out the right sprite X coord to show
        if(userPokemonShiny != null && userPokemonShiny.getIsShiny()) {
            if(userPokemonSex != null && userPokemonSex.getIsMale()) {
                return form.getSpriteShinyMaleX();
            } else {
                return form.getSpriteShinyFemaleX();
            }
        } else {
            if(userPokemonSex != null && userPokemonSex.getIsMale()) {
                return form.getSpriteMaleX();
            } else {
                return form.getSpriteFemaleX();
            }
        }
    }

    @Transient
    public int getSpriteY() {
        // Get form
        PokemonForm form = pokemon.getPokemonForms().get(0);
        if(userPokemonForm != null) {
            form = userPokemonForm.getPokemonForm();
        }
        // Figure out the right sprite X coord to show
        if(userPokemonShiny != null && userPokemonShiny.getIsShiny()) {
            if(userPokemonSex != null && userPokemonSex.getIsMale()) {
                return form.getSpriteShinyMaleY();
            } else {
                return form.getSpriteShinyFemaleY();
            }
        } else {
            if(userPokemonSex != null && userPokemonSex.getIsMale()) {
                return form.getSpriteMaleY();
            } else {
                return form.getSpriteFemaleY();
            }
        }
    }

    @Transient
    public UserPokemonEV getUserPokemonEV(Stat stat) {
        List<UserPokemonEV> userPokemonEVs = getUserPokemonEVs();
        for(UserPokemonEV userPokemonEV : userPokemonEVs) {
            if(userPokemonEV.getStat() == stat) {
                return userPokemonEV;
            }
        }
        return null;
    }

    @Transient
    public UserPokemonIV getUserPokemonIV(Stat stat) {
        List<UserPokemonIV> userPokemonIVs = getUserPokemonIVs();
        for(UserPokemonIV userPokemonIV : userPokemonIVs) {
            if(userPokemonIV.getStat() == stat) {
                return userPokemonIV;
            }
        }
        return null;
    }

    @Transient
    public UserPokemonMove getUserPokemonMove(MoveSlot moveSlot) {
        List<UserPokemonMove> userPokemonMoves = getUserPokemonMoves();
        for(UserPokemonMove userPokemonMove : userPokemonMoves) {
            if(userPokemonMove.getMoveSlot() == moveSlot) {
                return userPokemonMove;
            }
        }
        return null;
    }
}
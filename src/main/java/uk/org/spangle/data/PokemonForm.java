package uk.org.spangle.data;

import javax.persistence.*;

@Entity
@Table(name = "pokemon_forms")
public class PokemonForm {
    private int id;
    private Pokemon pokemon;
    private String name;
    private int spriteMaleX;
    private int spriteMaleY;
    private int spriteFemaleX;
    private int spriteFemaleY;
    private boolean isShiny;

    public PokemonForm() {
        // this form used by Hibernate
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="pokemon_form_id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name="pokemon_id", updatable=false)
    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    @Column(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name="sprite_male_x")
    public int getSpriteMaleX() {
        return spriteMaleX;
    }

    public void setSpriteMaleX(int spriteMaleX) {
        this.spriteMaleX = spriteMaleX;
    }

    @Column(name="sprite_male_y")
    public int getSpriteMaleY() {
        return spriteMaleY;
    }

    public void setSpriteMaleY(int spriteMaleY) {
        this.spriteMaleY = spriteMaleY;
    }

    @Column(name="sprite_female_x")
    public int getSpriteFemaleX() {
        return spriteFemaleX;
    }

    public void setSpriteFemaleX(int spriteFemaleX) {
        this.spriteFemaleX = spriteFemaleX;
    }

    @Column(name="sprite_female_y")
    public int getSpriteFemaleY() {
        return spriteFemaleY;
    }

    public void setSpriteFemaleY(int spriteFemaleY) {
        this.spriteFemaleY = spriteFemaleY;
    }

    @Column(name="is_shiny")
    public boolean getIsShiny() {
        return isShiny;
    }

    public void setIsShiny(boolean isShiny) {
        this.isShiny = isShiny;
    }
}

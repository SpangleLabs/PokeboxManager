package uk.org.spangle.data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "pokemon_form")
public class PokemonForm {
    private int id;
    private Pokemon pokemon;
    private String name;
    private int spriteMaleX;
    private int spriteMaleY;
    private int spriteFemaleX;
    private int spriteFemaleY;
    private int spriteShinyMaleX;
    private int spriteShinyMaleY;
    private int spriteShinyFemaleX;
    private int spriteShinyFemaleY;
    private List<PokemonFormAbility> pokemonFormAbilities;
    private List<PokemonFormBaseStat> pokemonFormBaseStats;
    private List<PokemonFormMove> pokemonFormMoves;

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

    @Column(name="sprite_shiny_male_x")
    public int getSpriteShinyMaleX() {
        return spriteShinyMaleX;
    }

    public void setSpriteShinyMaleX(int spriteShinyMaleX) {
        this.spriteShinyMaleX = spriteShinyMaleX;
    }

    @Column(name="sprite_shiny_male_y")
    public int getSpriteShinyMaleY() {
        return spriteShinyMaleY;
    }

    public void setSpriteShinyMaleY(int spriteShinyMaleY) {
        this.spriteShinyMaleY = spriteShinyMaleY;
    }

    @Column(name="sprite_shiny_female_x")
    public int getSpriteShinyFemaleX() {
        return spriteShinyFemaleX;
    }

    public void setSpriteShinyFemaleX(int spriteShinyFemaleX) {
        this.spriteShinyFemaleX = spriteShinyFemaleX;
    }

    @Column(name="sprite_shiny_female_y")
    public int getSpriteShinyFemaleY() {
        return spriteShinyFemaleY;
    }

    public void setSpriteShinyFemaleY(int spriteShinyFemaleY) {
        this.spriteShinyFemaleY = spriteShinyFemaleY;
    }

    @OneToMany(mappedBy="pokemonForm")
    public List<PokemonFormAbility> getPokemonFormAbilities() {
        return pokemonFormAbilities;
    }

    public void setPokemonFormAbilities(List<PokemonFormAbility> pokemonFormAbilities) {
        this.pokemonFormAbilities = pokemonFormAbilities;
    }

    @OneToMany(mappedBy="pokemonForm")
    public List<PokemonFormBaseStat> getPokemonFormBaseStats() {
        return pokemonFormBaseStats;
    }

    public void setPokemonFormBaseStats(List<PokemonFormBaseStat> pokemonFormBaseStats) {
        this.pokemonFormBaseStats = pokemonFormBaseStats;
    }

    @OneToMany(mappedBy="pokemonForm")
    @OrderBy("ordinal")
    public List<PokemonFormMove> getPokemonFormMoves() {
        return pokemonFormMoves;
    }

    public void setPokemonFormMoves(List<PokemonFormMove> pokemonFormMoves) {
        this.pokemonFormMoves = pokemonFormMoves;
    }

    @Transient
    public PokemonFormBaseStat getPokemonFormBaseStat(Stat stat) {
        List<PokemonFormBaseStat> pokemonFormBaseStats = getPokemonFormBaseStats();
        for(PokemonFormBaseStat baseStat : pokemonFormBaseStats) {
            if(baseStat.getStat() == stat) {
                return baseStat;
            }
        }
        return null;
    }

    public String toString() {
        return getName();
    }
}

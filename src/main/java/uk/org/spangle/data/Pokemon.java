package uk.org.spangle.data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "pokemon")
public class Pokemon implements Comparable {

    private int id;
    private String name;
    private int nationalDex;
    private boolean isGenderless;
    private List<PokemonForm> pokemonForms;

    public Pokemon() {
        // this form used by Hibernate
    }

    public Pokemon(int id, String name, int nationalDex) {
        this.id = id;
        this.name = name;
        this.nationalDex = nationalDex;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="pokemon_id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "national_dex")
    public int getNationalDex() {
        return nationalDex;
    }

    public void setNationalDex(int nationDex) {
        this.nationalDex = nationDex;
    }

    @Column(name = "is_genderless")
    public boolean getIsGenderless() {
        return isGenderless;
    }

    public void setIsGenderless(boolean isGenderless) {
        this.isGenderless = isGenderless;
    }

    @OneToMany(mappedBy="pokemon")
    @OrderBy("id")
    public List<PokemonForm> getPokemonForms() {
        return pokemonForms;
    }

    public void setPokemonForms(List<PokemonForm> pokemonForms) {
        this.pokemonForms = pokemonForms;
    }

    public void addPokemonForm(PokemonForm pokemonForm) {
        this.pokemonForms.add(pokemonForm);
    }

    public String toString() {
        return Integer.toString(id);
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof Pokemon) {
            Pokemon other = (Pokemon) o;
            return this.getName().compareTo(other.getName());
        }
        return 0;
    }
}
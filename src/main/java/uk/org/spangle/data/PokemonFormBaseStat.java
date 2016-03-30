package uk.org.spangle.data;

import javax.persistence.*;

@Entity
@Table(
        name = "pokemon_form_base_stat",
        uniqueConstraints = @UniqueConstraint(columnNames={"pokemon_form_id", "stat_id"})
)
public class PokemonFormBaseStat {

    private int id;
    private PokemonForm pokemonForm;
    private Stat stat;
    private int value;

    public PokemonFormBaseStat() {
        // this form used by Hibernate
    }

    public PokemonFormBaseStat(PokemonForm pokemonForm, Stat stat, int value) {
        this.pokemonForm = pokemonForm;
        this.stat = stat;
        this.value = value;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="pokemon_form_ability_id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name="pokemon_form_id")
    public PokemonForm getPokemonForm() {
        return pokemonForm;
    }

    public void setPokemonForm(PokemonForm pokemonForm) {
        this.pokemonForm = pokemonForm;
    }

    @ManyToOne
    @JoinColumn(name="stat_id")
    public Stat getStat() {
        return stat;
    }

    public void setStat(Stat stat) {
        this.stat = stat;
    }

    @Column(name="value")
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
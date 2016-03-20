package uk.org.spangle.data;

import javax.persistence.*;

@Entity
@Table(name = "pokemon_form_ability")
public class PokemonFormAbility {

    private int id;
    private PokemonForm pokemonForm;
    private AbilitySlot abilitySlot;
    private Ability ability;

    public PokemonFormAbility() {
        // this form used by Hibernate
    }

    public PokemonFormAbility(PokemonForm pokemonForm, AbilitySlot abilitySlot, Ability ability) {
        this.pokemonForm = pokemonForm;
        this.abilitySlot = abilitySlot;
        this.ability = ability;
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
    @JoinColumn(name="ability_slot_id")
    public AbilitySlot getAbilitySlot() {
        return abilitySlot;
    }

    public void setAbilitySlot(AbilitySlot abilitySlot) {
        this.abilitySlot = abilitySlot;
    }

    @ManyToOne
    @JoinColumn(name="ability_id")
    public Ability getAbility() {
        return ability;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }
}
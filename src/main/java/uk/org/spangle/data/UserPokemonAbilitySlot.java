package uk.org.spangle.data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "user_pokemon_ability_slot")
public class UserPokemonAbilitySlot {

    private int id;
    private UserPokemon userPokemon;
    private AbilitySlot abilitySlot;
    private Timestamp timestamp;

    public UserPokemonAbilitySlot() {
        // this form used by Hibernate
    }

    public UserPokemonAbilitySlot(UserPokemon userPokemon, AbilitySlot abilitySlot) {
        this.id = userPokemon.getId();
        this.userPokemon = userPokemon;
        this.abilitySlot = abilitySlot;
        Date date = new Date();
        this.timestamp = new Timestamp(date.getTime());
    }

    @Id
    //@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="user_pokemon_id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @OneToOne
    @JoinColumn(name="user_pokemon_id")
    public UserPokemon getUserPokemon() {
        return userPokemon;
    }

    public void setUserPokemon(UserPokemon userPokemon) {
        this.userPokemon = userPokemon;
    }

    @ManyToOne
    @JoinColumn(name="ability_slot_id")
    public AbilitySlot getAbilitySlot() {
        return abilitySlot;
    }

    public void setAbilitySlot(AbilitySlot abilitySlot) {
        this.abilitySlot = abilitySlot;
    }

    @Column(name = "timestamp")
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
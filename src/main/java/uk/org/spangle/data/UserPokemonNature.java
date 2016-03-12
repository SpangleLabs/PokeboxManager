package uk.org.spangle.data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "user_pokemon_nature")
public class UserPokemonNature {

    private int id;
    private UserPokemon userPokemon;
    private Nature nature;
    private Timestamp timestamp;

    public UserPokemonNature() {
        // this form used by Hibernate
    }

    public UserPokemonNature(UserPokemon userPokemon, Nature nature) {
        this.userPokemon = userPokemon;
        this.nature = nature;
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
    @JoinColumn(name="nature_id")
    public Nature getNature() {
        return nature;
    }

    public void setNature(Nature nature) {
        this.nature = nature;
    }

    @Column(name = "timestamp")
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
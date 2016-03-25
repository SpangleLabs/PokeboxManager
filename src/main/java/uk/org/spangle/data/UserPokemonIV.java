package uk.org.spangle.data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(
        name = "user_pokemon_iv",
        uniqueConstraints = @UniqueConstraint(columnNames={"user_pokemon_id", "stat_id"})
)
public class UserPokemonIV {

    private int id;
    private UserPokemon userPokemon;
    private Stat stat;
    private int value;
    private Timestamp timestamp;

    public UserPokemonIV() {
        // this form used by Hibernate
    }

    public UserPokemonIV(UserPokemon userPokemon, Stat stat, int value) {
        this.userPokemon = userPokemon;
        this.stat = stat;
        this.value = value;
        Date date = new Date();
        this.timestamp = new Timestamp(date.getTime());
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="user_pokemon_iv_id")
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

    @Column(name = "timestamp")
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
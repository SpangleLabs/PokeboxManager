package uk.org.spangle.data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "user_pokemon_sex")
public class UserPokemonSex {

    private int id;
    private UserPokemon userPokemon;
    private boolean isMale;
    private Timestamp timestamp;

    public UserPokemonSex() {
        // this form used by Hibernate
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

    @OneToOne
    @JoinColumn(name="user_pokemon_id")
    public UserPokemon getUserPokemon() {
        return userPokemon;
    }

    public void setUserPokemon(UserPokemon userPokemon) {
        this.userPokemon = userPokemon;
    }

    @Column(name = "is_male")
    public boolean getIsMale() {
        return isMale;
    }

    public void setIsMale(boolean isMale) {
        this.isMale = isMale;
    }

    @Column(name = "timestamp")
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
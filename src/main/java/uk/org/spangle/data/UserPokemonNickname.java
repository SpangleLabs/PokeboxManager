package uk.org.spangle.data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "user_pokemon_nickname")
public class UserPokemonNickname {

    private int id;
    private UserPokemon userPokemon;
    private String nickname;
    private Timestamp timestamp;

    public UserPokemonNickname() {
        // this form used by Hibernate
    }

    public UserPokemonNickname(UserPokemon userPokemon, String nickname) {
        this.userPokemon = userPokemon;
        this.nickname = nickname;
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

    @Column(name = "nickname")
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Column(name = "timestamp")
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
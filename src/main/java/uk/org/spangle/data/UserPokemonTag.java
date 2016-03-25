package uk.org.spangle.data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(
        name = "user_pokemon_tag",
        uniqueConstraints = @UniqueConstraint(columnNames={"user_pokemon_id", "user_tag_id"})
)
public class UserPokemonTag {

    private int id;
    private UserPokemon userPokemon;
    private UserTag userTag;
    private Timestamp timestamp;

    public UserPokemonTag() {
        // this form used by Hibernate
    }

    public UserPokemonTag(UserPokemon userPokemon, UserTag userTag) {
        this.userPokemon = userPokemon;
        this.userTag = userTag;
        Date date = new Date();
        this.timestamp = new Timestamp(date.getTime());
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="user_pokemon_tag_id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name="user_pokemon_id")
    public UserPokemon getUserPokemon() {
        return userPokemon;
    }

    public void setUserPokemon(UserPokemon userPokemon) {
        this.userPokemon = userPokemon;
    }

    @ManyToOne
    @JoinColumn(name="user_tag_id")
    public UserTag getUserTag() {
        return userTag;
    }

    public void setUserTag(UserTag userTag) {
        this.userTag = userTag;
    }

    @Column(name = "timestamp")
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
package uk.org.spangle.data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(
        name = "user_pokemon_move",
        uniqueConstraints = @UniqueConstraint(columnNames={"user_pokemon_id", "move_slot_id"})
)
public class UserPokemonMove {

    private int id;
    private UserPokemon userPokemon;
    private MoveSlot moveSlot;
    private Move move;
    private Timestamp timestamp;

    public UserPokemonMove() {
        // this form used by Hibernate
    }

    public UserPokemonMove(UserPokemon userPokemon, MoveSlot moveSlot, Move move) {
        this.userPokemon = userPokemon;
        this.moveSlot = moveSlot;
        this.move = move;
        Date date = new Date();
        this.timestamp = new Timestamp(date.getTime());
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="user_pokemon_move_id")
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
    @JoinColumn(name="move_slot_id")
    public MoveSlot getMoveSlot() {
        return moveSlot;
    }

    public void setMoveSlot(MoveSlot moveSlot) {
        this.moveSlot = moveSlot;
    }

    @ManyToOne
    @JoinColumn(name="move_id")
    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    @Column(name = "timestamp")
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
package uk.org.spangle.data;

import javax.persistence.*;

@Entity
@Table(name = "pokemon_form_move")
public class PokemonFormMove {

    private int id;
    private PokemonForm pokemonForm;
    private Move move;
    private MoveMethod moveMethod;
    private Integer level;
    private Integer ordinal;

    public PokemonFormMove() {
        // this form used by Hibernate
    }

    public PokemonFormMove(PokemonForm pokemonForm, Move move, MoveMethod moveMethod) {
        this.pokemonForm = pokemonForm;
        this.move = move;
        this.moveMethod = moveMethod;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="pokemon_form_move_id")
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
    @JoinColumn(name="move_id")
    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    @ManyToOne
    @JoinColumn(name="move_method_id")
    public MoveMethod getMoveMethod() {
        return moveMethod;
    }

    public void setMoveMethod(MoveMethod moveMethod) {
        this.moveMethod = moveMethod;
    }

    @Column(name="level")
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Column(name="ordinal")
    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }
}

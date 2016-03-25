package uk.org.spangle.data;

import javax.persistence.*;

@Entity
@Table(name = "move_slot")
public class MoveSlot {
    private int id;
    private String code;

    public MoveSlot() {
        // this form used by Hibernate
    }

    public MoveSlot(String code) {
        this.code = code;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="move_slot_id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name="code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

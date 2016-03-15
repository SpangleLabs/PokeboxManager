package uk.org.spangle.data;

import javax.persistence.*;

@Entity
@Table(name = "ability_slot")
public class AbilitySlot {
    private int id;
    private String code;

    public AbilitySlot() {
        // this form used by Hibernate
    }

    public AbilitySlot(String code) {
        this.code = code;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ability_slot_id")
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

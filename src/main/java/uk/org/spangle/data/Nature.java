package uk.org.spangle.data;

import javax.persistence.*;

@Entity
@Table(name = "nature")
public class Nature {
    private int id;
    private String name;
    private Stat statUp;
    private Stat statDown;

    public Nature() {
        // this form used by Hibernate
    }

    public Nature(String name, Stat statUp, Stat statDown) {
        this.name = name;
        this.statUp = statUp;
        this.statDown = statDown;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="nature_id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne
    @JoinColumn(name="stat_up_id")
    public Stat getStatUp() {
        return statUp;
    }

    public void setStatUp(Stat statUp) {
        this.statUp = statUp;
    }

    @ManyToOne
    @JoinColumn(name="stat_down_id")
    public Stat getStatDown() {
        return statDown;
    }

    public void setStatDown(Stat statDown) {
        this.statDown = statDown;
    }

    public String toString() {
        return getName();
    }
}

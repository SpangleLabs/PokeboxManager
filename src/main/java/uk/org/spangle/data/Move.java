package uk.org.spangle.data;

import javax.persistence.*;

@Entity
@Table(name = "move")
public class Move {
    private int id;
    private String name;
    private String description;

    public Move() {
        // this form used by Hibernate
    }

    public Move(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="move_id")
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

    @Column(name="description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

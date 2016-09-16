package uk.org.spangle.data;

import javax.persistence.*;

@Entity
@Table(name = "ability")
public class Ability {
    private int id;
    private String name;
    private String description;

    public Ability() {
        // this form used by Hibernate
    }

    public Ability(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ability_id")
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

    public String toString() {
        return getName();
    }
}

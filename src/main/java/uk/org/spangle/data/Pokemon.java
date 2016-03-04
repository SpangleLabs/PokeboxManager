package uk.org.spangle.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pokemon")
public class Pokemon {

    private int id;
    private String name;
    private int nationalDex;

    public Pokemon() {
        // this form used by Hibernate
    }

    public Pokemon(int id, String name, int nationalDex) {
        this.id = id;
        this.name = name;
        this.nationalDex = nationalDex;
    }

    @Id
    @Column(name="pokemon_id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "national_dex")
    public int getNationalDex() {
        return nationalDex;
    }

    public void setNationalDex(int nationDex) {
        this.nationalDex = nationDex;
    }
}
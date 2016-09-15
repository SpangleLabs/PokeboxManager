package uk.org.spangle.data;

import javax.persistence.*;

@Entity
@Table(name = "language")
public class Language {
    private int id;
    private String name;
    private String abbr;

    public Language() {
        // this form used by Hibernate
    }

    public Language(String name, String abbr) {
        this.name = name;
        this.abbr = abbr;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="language_id")
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

    @Column(name="abbr")
    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String toString() {
        return getName();
    }
}

package uk.org.spangle.data;

import javax.persistence.*;

@Entity
@Table(name = "user_tag")
public class UserTag {
    private int id;
    private String name;
    private String description;

    public UserTag() {
        // this form used by Hibernate
    }

    public UserTag(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="user_tag_id")
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

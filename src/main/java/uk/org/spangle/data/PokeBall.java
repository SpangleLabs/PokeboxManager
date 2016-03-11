package uk.org.spangle.data;

import javax.persistence.*;

@Entity
@Table(name = "pokeballs")
public class PokeBall {
    private int id;
    private String name;
    private String description;
    private int spriteX;
    private int spriteY;

    public PokeBall() {
        // this form used by Hibernate
    }

    public PokeBall(String name, String description, int spriteX, int spriteY) {
        this.name = name;
        this.description = description;
        this.spriteX = spriteX;
        this.spriteY = spriteY;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="pokeball_id")
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

    @Column(name="sprite_x")
    public int getSpriteX() {
        return spriteX;
    }

    public void setSpriteX(int spriteX) {
        this.spriteX = spriteX;
    }

    @Column(name="sprite_y")
    public int getSpriteY() {
        return spriteY;
    }

    public void setSpriteY(int spriteY) {
        this.spriteY = spriteY;
    }
}

package uk.org.spangle.data;

import javax.persistence.*;

@Entity
@Table(name = "generation_boxes")
public class GenerationBox {

    private int id;
    private Generation generation;
    private int ordinal;
    private int size;
    private int columns;
    private String name;

    public GenerationBox() {
        // this form used by Hibernate
    }

    public GenerationBox(int id, int ordinal, int size, int columns, String name) {
        this.id = id;
        this.ordinal = ordinal;
        this.size = size;
        this.columns = columns;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="generation_box_id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name="generation_id", updatable=false)
    public Generation getGeneration() {
        return generation;
    }

    public void setGeneration(Generation generation) {
        this.generation = generation;
    }

    @Column(name = "ordinal")
    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    @Column(name = "size")
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Column(name = "columns")
    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
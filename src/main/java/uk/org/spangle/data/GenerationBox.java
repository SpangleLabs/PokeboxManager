package uk.org.spangle.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "generation_boxes")
public class GenerationBox {

    private int id;
    private int generationId;
    private int ordinal;
    private int size;
    private int columns;
    private String name;

    public GenerationBox() {
        // this form used by Hibernate
    }

    public GenerationBox(int id, int generationId, int ordinal, int size, int columns, String name) {
        this.id = id;
        this.generationId = generationId;
        this.ordinal = ordinal;
        this.size = size;
        this.columns = columns;
        this.name = name;
    }

    @Id
    @Column(name="generation_box_id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "generation_id")
    public int getGenerationId() {
        return generationId;
    }

    public void setGenerationId(int generationId) {
        this.generationId = generationId;
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
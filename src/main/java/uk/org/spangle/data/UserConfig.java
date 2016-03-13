package uk.org.spangle.data;

import javax.persistence.*;

@Entity
@Table(name = "user_config")
public class UserConfig {

    private int id;
    private String key;
    private String value;

    public UserConfig() {
        // this form used by Hibernate
    }

    public UserConfig(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="user_config_id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "key")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Column(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
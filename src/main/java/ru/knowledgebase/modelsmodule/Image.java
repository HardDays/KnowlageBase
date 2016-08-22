package ru.knowledgebase.modelsmodule;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by root on 10.08.16.
 */

@Entity
public class Image {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;

    @Column
    private String path;

    public Image() {}

    public Image(String path) {
        this.path = path;
    }

    @ManyToOne
    private Article article;


    //BEGIN SG METHODS

    public String getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    //END SG METHODS

}

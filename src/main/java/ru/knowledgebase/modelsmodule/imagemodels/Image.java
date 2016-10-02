package ru.knowledgebase.modelsmodule.imagemodels;

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

    @Column
    private String name;

    public Image() {}

    public Image(String path, String filename) {
        setPath(path);
        setName(filename);
    }


    //BEGIN SG METHODS

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

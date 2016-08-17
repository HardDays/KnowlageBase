package ru.knowledgebase.modelsmodule;

import javax.persistence.*;

/**
 * Created by root on 10.08.16.
 */

@Entity
public class Image {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;

    @Column
    private byte[] content;

}

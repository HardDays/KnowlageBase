package ru.knowledgebase.modelsmodule;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by root on 17.08.16.
 */
@Entity
public class Token {

    @Id
    @SequenceGenerator(name="token_id_seq",
            sequenceName="token_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="token_id_seq")
    private int id;

    @OneToOne(cascade = {CascadeType.MERGE})
    private User user;

    @Column
    private String token;

    @Column
    private Date endDate;

    public Token(User user, String token, Date endDate) {
        this.user = user;
        this.token = token;
        this.endDate = endDate;
    }
    public Token() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void copy(Token second){
        this.id = second.id;
        this.token = second.token;
        this.endDate = second.endDate;
        this.user = second.user;
    }

}

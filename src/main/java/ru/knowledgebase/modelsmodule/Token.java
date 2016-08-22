package ru.knowledgebase.modelsmodule;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by root on 17.08.16.
 */
@Entity(name="user_token")
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


}

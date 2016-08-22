package ru.knowledgebase.modelsmodule.usermodels;

import ru.knowledgebase.modelsmodule.Article;
import ru.knowledgebase.modelsmodule.rolemodels.UserArticleRole;
import ru.knowledgebase.modelsmodule.rolemodels.UserGlobalRole;

import javax.persistence.*;

/**
 * Created by root on 09.08.16.
 */
@Entity(name = "users")
public class User {
    @Id
    @SequenceGenerator(name="user_id_seq",
            sequenceName="user_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="user_id_seq")
    @Column(name = "id")
    private int id;

    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToOne(mappedBy = "user", cascade = {CascadeType.REMOVE})
    private Token token;

    @OneToOne(mappedBy = "user", cascade = {CascadeType.REMOVE})
    private UserGlobalRole userGlobalRole;

    @OneToOne(mappedBy = "user", cascade = {CascadeType.REMOVE})
    private UserArticleRole userArticleRole;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User(String str) {
        login = str;
        password = str;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User(int id){
        this.id = id;
    }

    public User(){
    }

    @Basic
    @Column(name = "login")
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User users = (User) o;

        if (id != users.id) return false;
        if (login != null ? !login.equals(users.login) : users.login != null) return false;
        if (password != null ? !password.equals(users.password) : users.password != null) return false;

        return true;
    }

    public void copy(User second){
        this.id = second.id;
        this.login = second.login;
        this.password = second.password;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}

package ru.knowledgebase.modelsmodule.usermodels;

import org.hibernate.annotations.*;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.commentmodels.Comment;
import ru.knowledgebase.modelsmodule.rolemodels.UserArticleRole;
import ru.knowledgebase.modelsmodule.rolemodels.UserGlobalRole;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import java.sql.Timestamp;
import java.util.List;

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
    private int id;

    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_1")
    private String phone1;

    @Column(name = "phone_2")
    private String phone2;

    @Column(name = "office")
    private String office;

    @Column(name = "recruitment_date")
    private Timestamp recruitmentDate;

    @Column(name = "dismissal_date")
    private Timestamp dismissalDate;

    @OneToOne(mappedBy = "user", cascade = {CascadeType.REMOVE})
    private Token token;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE})
    private List<UserGlobalRole> userGlobalRoles;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE})
    private List<UserArticleRole> userArticleRoles;

    @OneToMany(mappedBy = "commentator", cascade = {CascadeType.REMOVE})
    private List<Comment> comments;

    @OneToMany(mappedBy = "admin", cascade = {CascadeType.REMOVE})
    private List<Comment> adminComments;

    @OneToMany(mappedBy = "author")
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private List<Article> article;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User(String login, String password, String email,
                String firstName, String middleName, String lastName,
                String office, String phone1, String phone2,
                Timestamp recruitmentDate, Timestamp dismissalDate) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.office = office;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.recruitmentDate = recruitmentDate;
        this.dismissalDate = dismissalDate;
    }

    public User(){}

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Article> getArticle() {
        return article;
    }

    public void setArticle(List<Article> article) {
        this.article = article;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public List<UserGlobalRole> getUserGlobalRoles() {
        return userGlobalRoles;
    }

    public void setUserGlobalRoles(List<UserGlobalRole> userGlobalRoles) {
        this.userGlobalRoles = userGlobalRoles;
    }

    public List<UserArticleRole> getUserArticleRoles() {
        return userArticleRoles;
    }

    public void setUserArticleRoles(List<UserArticleRole> userArticleRoles) {
        this.userArticleRoles = userArticleRoles;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public Timestamp getRecruitmentDate() {
        return recruitmentDate;
    }

    public void setRecruitmentDate(Timestamp recruitmentDate) {
        this.recruitmentDate = recruitmentDate;
    }

    public Timestamp getDismissalDate() {
        return dismissalDate;
    }

    public void setDismissalDate(Timestamp dismissalDate) {
        this.dismissalDate = dismissalDate;
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

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}

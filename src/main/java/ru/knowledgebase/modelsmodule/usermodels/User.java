package ru.knowledgebase.modelsmodule.usermodels;

import org.hibernate.annotations.*;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.commentmodels.Comment;
import ru.knowledgebase.modelsmodule.rolemodels.UserSectionRole;

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

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
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

    @Column
    private boolean hasEmailNotifications;

    @Column
    private boolean hasSiteNotifications;

    @Column
    private Integer superVisorId;

    @OneToOne(mappedBy = "user", cascade = {CascadeType.REMOVE})
    private Token token;

   // @OnDelete(action=OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE})
    private List<UserSectionRole> userSectionRoles;

   // @OnDelete(action=OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "commentator", cascade = {CascadeType.REMOVE})
    private List<Comment> comments;

  //  @OnDelete(action=OnDeleteAction.CASCADE)
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
                Timestamp recruitmentDate, Timestamp dismissalDate,
                boolean hasEmailNotifications, boolean hasSiteNotifications, Integer superVisorId) {
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
        this.hasEmailNotifications = hasEmailNotifications;
        this.hasSiteNotifications = hasSiteNotifications;
        this.superVisorId = superVisorId;
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

    public List<UserSectionRole> getUserSectionRoles() {
        return userSectionRoles;
    }

    public void setUserSectionRoles(List<UserSectionRole> userSectionRoles) {
        this.userSectionRoles = userSectionRoles;
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

    public String getFullName(){
        return firstName + " " + middleName + " " + lastName;
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

    public boolean isHasEmailNotifications() {
        return hasEmailNotifications;
    }

    public void setHasEmailNotifications(boolean hasEmailNotifications) {
        this.hasEmailNotifications = hasEmailNotifications;
    }

    public boolean isHasSiteNotifications() {
        return hasSiteNotifications;
    }

    public void setHasSiteNotifications(boolean hasSiteNotifications) {
        this.hasSiteNotifications = hasSiteNotifications;
    }

    public Integer getSuperVisorId() {
        return superVisorId;
    }

    public void setSuperVisorId(Integer superVisorId) {
        this.superVisorId = superVisorId;
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

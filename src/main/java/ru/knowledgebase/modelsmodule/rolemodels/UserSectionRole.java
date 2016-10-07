package ru.knowledgebase.modelsmodule.rolemodels;

import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.usermodels.User;

import javax.persistence.*;

/**
 * Created by vova on 07.10.16.
 */
@Entity
public class UserSectionRole {

    @Id
    @SequenceGenerator(name="user_section_role_id_seq",
            sequenceName="user_section_role_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="user_section_role_id_seq")
    private int id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Article article;

    @ManyToOne
    private Role role;

    public UserSectionRole(){

    }

    public UserSectionRole(User user, Article article, Role role){
        this.user = user;
        this.article = article;
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}

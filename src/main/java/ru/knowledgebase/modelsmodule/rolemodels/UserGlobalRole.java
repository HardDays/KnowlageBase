package ru.knowledgebase.modelsmodule.rolemodels;

import ru.knowledgebase.modelsmodule.usermodels.User;

import javax.persistence.*;

@Entity
public class UserGlobalRole {

    @Id
    @SequenceGenerator(name="user_global_role_id_seq",
            sequenceName="user_global_role_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="user_global_role_id_seq")
    private int id;

    @ManyToOne
    private User user;

    @ManyToOne
    private GlobalRole globalRole;

    public UserGlobalRole(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GlobalRole getGlobalRole() {
        return globalRole;
    }

    public void setGlobalRole(GlobalRole globalRole) {
        this.globalRole = globalRole;
    }

    public UserGlobalRole(User user, GlobalRole globalRole){
        this.user = user;
        this.globalRole = globalRole;
    }

}

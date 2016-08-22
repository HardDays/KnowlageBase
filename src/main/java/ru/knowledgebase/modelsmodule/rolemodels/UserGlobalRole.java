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

    @OneToOne
    private User user;

    @OneToOne
    private GlobalRole globalRole;

    public UserGlobalRole(){

    }

    public UserGlobalRole(User user, GlobalRole globalRole){
        this.user = user;
        this.globalRole = globalRole;
    }

}

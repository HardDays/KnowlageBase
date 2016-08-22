package ru.knowledgebase.modelsmodule.rolemodels;

import javax.persistence.*;

/**
 * Created by vova on 19.08.16.
 */
@Entity
public class GlobalRole {
    @Id
    @SequenceGenerator(name="global_role_id_seq",
            sequenceName="global_role_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="global_role_id_seq")
    private int id;

    @OneToOne(mappedBy = "globalRole", cascade = {CascadeType.REMOVE})
    private UserGlobalRole userGlobalRole;

    @Column
    private String name;

    @Column
    private boolean canAddUser;

    @Column
    private boolean canEditUser;

    @Column
    private boolean canDeleteUser;

    @Column
    private boolean canEditUserRoles;

    public GlobalRole(){

    }

    public GlobalRole(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCanAddUser() {
        return canAddUser;
    }

    public void setCanAddUser(boolean canAddUser) {
        this.canAddUser = canAddUser;
    }

    public boolean isCanEditUser() {
        return canEditUser;
    }

    public void setCanEditUser(boolean canEditUser) {
        this.canEditUser = canEditUser;
    }

    public boolean isCanDeleteUser() {
        return canDeleteUser;
    }

    public void setCanDeleteUser(boolean canDeleteUser) {
        this.canDeleteUser = canDeleteUser;
    }

    public boolean isCanEditUserRoles() {
        return canEditUserRoles;
    }

    public void setCanEditUserRoles(boolean canEditUserRoles) {
        this.canEditUserRoles = canEditUserRoles;
    }

    public GlobalRole(String name){
        this.name = name;
    }

}

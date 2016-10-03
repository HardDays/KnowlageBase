package ru.knowledgebase.modelsmodule.rolemodels;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(mappedBy = "globalRole", cascade = {CascadeType.REMOVE})
    private List<UserGlobalRole> userGlobalRole;

    @Column(unique = true)
    private String name;

    @Column
    private boolean canAddUser;

    @Column
    private boolean canEditUser;

    @Column
    private boolean canViewUser;

    @Column
    private boolean canDeleteUser;

    @Column
    private boolean canEditUserRole;

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

    public boolean isCanEditUserRole() {
        return canEditUserRole;
    }

    public void setCanEditUserRole(boolean canEditUserRoles) {
        this.canEditUserRole = canEditUserRoles;
    }

    public GlobalRole(String name){
        this.name = name;
    }

    public boolean isCanViewUser() {
        return canViewUser;
    }

    public void setCanViewUser(boolean canViewUser) {
        this.canViewUser = canViewUser;
    }
    //KOSTILI DETECTED
    public boolean isSuperUser(){
        return canViewUser && canDeleteUser && canEditUser && canEditUserRole && canViewUser;
    }
}

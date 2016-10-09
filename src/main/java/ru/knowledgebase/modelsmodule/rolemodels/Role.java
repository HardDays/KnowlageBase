package ru.knowledgebase.modelsmodule.rolemodels;

import javax.persistence.*;
import java.util.List;

/**
 * Created by vova on 07.10.16.
 */

@Entity
public class Role {
    @Id
    @SequenceGenerator(name="role_id_seq",
            sequenceName="role_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="role_id_seq")
    private int id;

    @OneToMany(mappedBy = "role", cascade = {CascadeType.REMOVE})
    private List<UserSectionRole> userSectionRoles;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private int roleId;

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

    @Column
    boolean baseRole;

    public Role(){

    }

    public Role(int id){
        this.id = id;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
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

    public Role(String name){
        this.name = name;
    }

    public boolean isCanViewUser() {
        return canViewUser;
    }

    public void setCanViewUser(boolean canViewUser) {
        this.canViewUser = canViewUser;
    }

    //KOSTILI DETECTED
    public boolean isBaseRole(){
        return baseRole;
    }

    public void setBaseRole(boolean baseUser){
        this.baseRole = baseUser;
    }

    @Column
    private boolean canAddArticle;

    @Column
    private boolean canEditArticle;

    @Column
    private boolean canDeleteArticle;

    @Column
    private boolean canViewArticle;

    @Column
    private boolean canAddNews;

    @Column
    private boolean canOnOffNotifications;

    @Column
    private boolean canGetNotifications;

    @Column
    private boolean canGetSystemActionsReports;

    @Column
    private boolean canGetSearchOperationsReports;

    @Column
    private boolean canGetEmployeesActionsReports;

    @Column
    private boolean canViewMistakes;

    @Column
    private boolean canAddMistakes;

    @Column
    private boolean canSearch;

    public boolean isCanAddArticle() {
        return canAddArticle;
    }

    public void setCanAddArticle(boolean canAddArticles) {
        this.canAddArticle = canAddArticles;
    }

    public boolean isCanEditArticle() {
        return canEditArticle;
    }

    public void setCanEditArticle(boolean canEditArticle) {
        this.canEditArticle = canEditArticle;
    }

    public boolean isCanDeleteArticle() {
        return canDeleteArticle;
    }

    public void setCanDeleteArticle(boolean canDeleteArticle) {
        this.canDeleteArticle = canDeleteArticle;
    }

    public boolean isCanViewArticle() {
        return canViewArticle;
    }

    public void setCanViewArticle(boolean canViewArticle) {
        this.canViewArticle = canViewArticle;
    }

    public boolean isCanAddNews() {
        return canAddNews;
    }

    public void setCanAddNews(boolean canAddNews) {
        this.canAddNews = canAddNews;
    }

    public boolean isCanOnOffNotifications() {
        return canOnOffNotifications;
    }

    public void setCanOnOffNotifications(boolean canOnOffNotifications) {
        this.canOnOffNotifications = canOnOffNotifications;
    }

    public boolean isCanGetSystemActionsReports() {
        return canGetSystemActionsReports;
    }

    public void setCanGetSystemActionsReports(boolean canGetReports) {
        this.canGetSystemActionsReports = canGetReports;
    }

    public boolean isCanViewMistakes() {
        return canViewMistakes;
    }

    public void setCanViewMistakes(boolean canViewMistakes) {
        this.canViewMistakes = canViewMistakes;
    }

    public boolean isCanAddMistakes() {
        return canAddMistakes;
    }

    public void setCanAddMistakes(boolean canAddMistakes) {
        this.canAddMistakes = canAddMistakes;
    }

    public boolean isCanSearch() {
        return canSearch;
    }

    public void setCanSearch(boolean canSearch) {
        this.canSearch = canSearch;
    }

    public boolean isCanGetNotifications() {
        return canGetNotifications;
    }

    public void setCanGetNotifications(boolean canGetNotifications) {
        this.canGetNotifications = canGetNotifications;
    }

    public boolean isCanGetSearchOperationsReports() {
        return canGetSearchOperationsReports;
    }

    public void setCanGetSearchOperationsReports(boolean canGetSearchReports) {
        this.canGetSearchOperationsReports = canGetSearchReports;
    }

    public boolean isCanGetEmployeesActionsReports() {
        return canGetEmployeesActionsReports;
    }

    public void setCanGetEmployeesActionsReports(boolean canGetHistoryReports) {
        this.canGetEmployeesActionsReports = canGetHistoryReports;
    }
}

package ru.knowledgebase.modelsmodule.rolemodels;

import javax.persistence.*;
import java.util.List;

/**
 * Created by vova on 19.08.16.
 */
@Entity
public class ArticleRole {

    @Id
    @SequenceGenerator(name="section_role_id_seq",
            sequenceName="section_role_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="section_role_id_seq")
    private int id;

    @OneToMany(mappedBy = "articleRole", cascade = {CascadeType.REMOVE})
    private List<UserArticleRole> userArticleRoles;

    @Column(unique = true)
    private String name;

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

    public ArticleRole(){

    }

    public ArticleRole(int id){
        this.id = id;
    }

    public ArticleRole(String name){
        this.name = name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }


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

    public List<UserArticleRole> getUserArticleRoles() {
        return userArticleRoles;
    }

    public void setUserArticleRoles(List<UserArticleRole> userArticleRoles) {
        this.userArticleRoles = userArticleRoles;
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

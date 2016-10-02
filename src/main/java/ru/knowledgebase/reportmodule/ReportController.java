package ru.knowledgebase.reportmodule;

import javafx.util.Pair;
import ru.knowledgebase.analyticsmodule.Analyser;
import ru.knowledgebase.analyticsmodule.rank.ArticleRank;
import ru.knowledgebase.analyticsmodule.rank.RequestRank;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.exceptionmodule.reportexception.UnableToCreateReportExeption;
import ru.knowledgebase.loggermodule.Log.LogReader;
import ru.knowledgebase.loggermodule.LogRecord.ALogRecord;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.rolemodels.UserArticleRole;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Мария on 01.10.2016.
 */
public class ReportController {

    private static volatile ReportController instance;

    private ReportBuilder reportBuilder = new ReportBuilder();
    private Analyser analyser = new Analyser();
    private LogReader logReader = new LogReader();
    private DataCollector dataCollector = new DataCollector();

    private LinkedList<ALogRecord> logRecords = null;

    /**
     * Get instance of a class
     * @return instance of a class
     */
    public static ReportController getInstance() {
        ReportController localInstance = instance;
        if (localInstance == null) {
            synchronized (ReportController.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ReportController();
                }
            }
        }
        return localInstance;
    }


    /**
     • Количество выгруженных элементов (статей)
     • Разбивка статей в соответствии с разделами 1 уровня и ниже (сортировка)
     • Название статьи
     • Дата создания, автор
     • Дата последнего изменения, (автор)
     • (Версия)
     • Размер

     * @param from
     * @param to
     * @param sections
     * @return
     */
    public String getSystemActionsReport(int userID, Timestamp from, Timestamp to,
                                         List<Integer> sections) throws Exception {
        HashSet<Integer> articlesIDs = getIDsOfArticlesCreatedDuringPeriod(from, to);
        Map<Integer, Article> sectionArticle = new HashMap<>();
        for( Integer id : articlesIDs){
            Article article = getArticleByID(id);
            Integer section = article.getSectionID();
            if(sections.contains(section))
                sectionArticle.put(section, article);
        }
        Map<Integer, List<Integer>> levelsections = getListOfSectionsForEachLeavel();

        Map<Integer, List<Article>> levelArticle = new HashMap<>();
        // TODO: sort articles by level

        return reportBuilder.buildSystemActionsReport(userID, from, to, levelArticle);
    }

    private HashSet<Integer> getIDsOfArticlesCreatedDuringPeriod(Timestamp from, Timestamp to) {
        HashSet<Integer> articlesIDs = null;
        try {
            articlesIDs = analyser.getUploadedArticles(
                    getRecordsFromLog(),
                    from,
                    to);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return articlesIDs;
    }

    /**
     • Дата
     • ФИО оператора
     • Название статей, просмотренных оператором
     • Количество просмотров за день одной и той же статьи

     * @return
     * @param from
     * @param to
     * @param sections
     */
    public String getEmployeesActionsReport(int userID, Timestamp from, Timestamp to,
                                            List<Integer> sections) throws Exception {

        List<UserArticleRole> users = getUsersOfEachSection(sections);

        return reportBuilder.buildEmployeesActionsReport(
                userID, from, to,
                getArticlesEachUserViewedAndNumberOfViews(from, to, users));

    }

    private Map<String, Map<String, Integer>> getArticlesEachUserViewedAndNumberOfViews(
            Timestamp from,
            Timestamp to,
            List<UserArticleRole> users)
            throws Exception {
        Map<String, Map<String, Integer>> usersArticlesNumViews = new HashMap<>();
        for (UserArticleRole userRole: users) {
            usersArticlesNumViews.put(
                    getUsersFullName(userRole),
                    getArticlesUserViewedWithNumOfViews(userRole, from, to));
        }
        return usersArticlesNumViews;
    }

    private List<UserArticleRole> getUsersOfEachSection(List<Integer> sections) throws Exception {
        List <UserArticleRole> users = new LinkedList<>();
        for (Integer section : sections) {
            users.addAll(dataCollector.findUserArticleRoleByArticle(section));
        }
        return users;
    }

    private Map<String, Integer> getArticlesUserViewedWithNumOfViews(UserArticleRole userRole,
                                                                     Timestamp from, Timestamp to)
                                                                        throws Exception {
        List<ArticleRank> articlesViewed = analyser.getUserViews(
                getRecordsFromLog(),
                userRole.getUser().getId(),
                from, to);
        Map<String, Integer> articlesUserViewedWithNumOfViews = new HashMap<>();
        for(ArticleRank article : articlesViewed){
            articlesUserViewedWithNumOfViews.put(
                    getArticleByID(article.getId()).getTitle(),
                    article.getRank());
        }
        return articlesUserViewedWithNumOfViews;
    }

    private Article getArticleByID(int articleID) {
        return dataCollector.findArticle(articleID);
    }

    private LinkedList<ALogRecord> getRecordsFromLog() throws Exception {
        if(logRecords == null)
            logRecords = logReader.getRecordsFromLog();
        return logRecords;
    }

    /**
     • Количество раз использования функции поиска
     • «Часто ищут» в разбивке по разделам 1 уровня
     • Разбивка поисковых запросов по разделам 1 уровня и пользователям с их запросами

     * @param from
     * @param to
     * @param sections
     * @return
     */
    public String getSearchActionsReport(Timestamp from, Timestamp to, List<Integer> sections)
            throws Exception {
        Integer numSearchActions = numTimesSearchWasMade(from, to);

        //Popular Search requests grouped by first level sections
        //Search requests grouped by first level sections and people who mede them
        HashMap<Integer, LinkedList<String>> allUsersWithRequestsForPeriod = analyser.getUserRequests(
                getRecordsFromLog(), from, to);
        List<RequestRank> popularRequests = analyser.getPopularRequests(getRecordsFromLog(), from, to);
        Map<Integer, LinkedList<RequestRank>> levelPopularRequests = new HashMap<>();


        Map<Integer, LinkedList<Integer>> allFirstLevelSectionsWithSubsections = getListOfSectionsForEachLeavel();
        // Making map with all first level sections and users in those sections
        Map<Integer, Map<String, LinkedList<String>>> levelUsersRequests = new HashMap<>();
        for(Integer firstLevelSection : allFirstLevelSectionsWithSubsections.keySet()){
            // Checking that all given sections are first level and form a list of all needed first level sections
            if(!sections.contains(firstLevelSection)){
                allFirstLevelSectionsWithSubsections.remove(firstLevelSection);
            }else{
                levelPopularRequests.put(
                        firstLevelSection,
                        new LinkedList<>()
                );
                //Form list of all sections of that level
                List<Integer> sectionsOnLevel = getAllSectionsOnLevel(allFirstLevelSectionsWithSubsections, firstLevelSection);
                //Checks if those users made search requests during the period and gets those requests
                Map<String, LinkedList<String>> usersRequests = new HashMap<>();
                for(UserArticleRole userOfSection : getListOfUsersOnLevel(sectionsOnLevel)){
                    Integer userId = userOfSection.getUser().getId();
                    if(allUsersWithRequestsForPeriod.keySet().contains(userId)){
                        LinkedList<String> requests = allUsersWithRequestsForPeriod.get(userOfSection.getUser().getId());
                        usersRequests.put(
                                getUsersFullName(userOfSection),
                                requests);
                        // Checks for popular requests on that level
                        LinkedList<RequestRank> popularRequestsOnLevel = new LinkedList<>();
                        for(RequestRank popReq : popularRequests){
                            if(requests.contains(popReq.getRequest())){
                                popularRequestsOnLevel.add(popReq);
                            }
                        }
                        levelPopularRequests.get(firstLevelSection).addAll(popularRequestsOnLevel);
                    }
                }
                levelUsersRequests.put(firstLevelSection, usersRequests);
            }
        }



        HashMap<Integer, Pair<LinkedList<Integer>, Map<Integer, LinkedList<String>>>> sectionsUserRequests = new HashMap<>();


        String path = reportBuilder.buildSearchActionsReport(userID, from, to, sections);
        return path;
    }

    private String getUsersFullName(UserArticleRole userOfSection) {
        return userOfSection.getUser().getFullName();
    }

    private LinkedList<UserArticleRole> getListOfUsersOnLevel(List<Integer> sectionsOnLevel) throws UnableToCreateReportExeption {
        return new LinkedList<>(
                getUsersOfGivenSections(sectionsOnLevel));
    }

    private List<Integer> getAllSectionsOnLevel(Map<Integer, LinkedList<Integer>> allFirstLevelSectionsWithSubsections, Integer firstLevelSection) {
        List<Integer> sectionsOnLevel = new LinkedList<>();
        sectionsOnLevel.add(firstLevelSection);
        sectionsOnLevel.addAll(allFirstLevelSectionsWithSubsections.get(firstLevelSection));
        return sectionsOnLevel;
    }

    private Integer numTimesSearchWasMade(Timestamp from, Timestamp to) throws Exception {
        Integer numSearchActions = analyser.getSearchUsage(getRecordsFromLog(), from, to);
        return numSearchActions;
    }

    private Map<Integer, List<UserArticleRole>> getSectionsWithAllUsers(List<Integer> sections) {
        Map<Integer, List<UserArticleRole>> sectionsUsers = new HashMap<>();
        for (Integer section : sections) {
            sectionsUsers.put(
                    section,
                    dataCollector.findUserArticleRoleByArticle(section)
            );
        }
        return sectionsUsers;
    }

    public List<UserArticleRole> getUsersOfGivenSections(List<Integer> sections)
            throws UnableToCreateReportExeption {
        List<UserArticleRole> usersOfSections = new HashMap<>();
        try {
            for(UserArticleRole user : getUsersOfEachSection(sections)){
                usersOfSectionsIDs.put(
                        user.getUser().getId(),
                        user.getArticle().getId());
            }
        } catch (Exception e) {
            throw new UnableToCreateReportExeption();
        }
        return usersOfSectionsIDs;
    }

    public Map<Integer,LinkedList<Integer>> getListOfSectionsForEachLeavel() {
        // TODO: get list of sections by each level
        return null;
    }
    
    //TODO: check all exceptions
    //TODO: Check maps
}

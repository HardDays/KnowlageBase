package ru.knowledgebase.reportmodule;

import ru.knowledgebase.analyticsmodule.Analyser;
import ru.knowledgebase.analyticsmodule.rank.ArticleRank;
import ru.knowledgebase.analyticsmodule.rank.RequestRank;
import ru.knowledgebase.dbmodule.DataCollector;
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
     * Составляет отчет о действия в системе за данный период. Создает xlsx файл и размечает поля в соответствии
     * со структурой отчетв.
     * В отчете:
     *    • Количество выгруженных элементов (статей)
     *    • Разбивка статей в соответствии с разделами 1 уровня и ниже (сортировка)
     *    • Название статьи
     *    • Дата создания, автор
     *    • Дата последнего изменения, автор
     *    • Версия
     *    • Размер
     * @param from - начальная дата
     * @param to - конечная дата
     * @param sections - разделы по которым хотят получить отчет
     * @return путь к созданному отчету
     * @throws Exception
     */
    public String getSystemActionsReport(int userID, Timestamp from, Timestamp to,
                                         List<Integer> sections) throws Exception {
        HashSet<Integer> articlesIDs = getIDsOfArticlesCreatedDuringPeriod(from, to);
        Map<String, List<Article>> sectionArticle = new HashMap<>();
        for( Integer id : articlesIDs){
            Article article = getArticleByID(id);
            Integer section = article.getSectionID();
            if(sections.contains(section)){
                if(sectionArticle.containsKey(getArticleTitle(section))){
                    sectionArticle.get(getArticleTitle(section)).add(article);
                }else{
                    List<Article> articles = new LinkedList<>();
                    articles.add(article);
                    sectionArticle.put(getArticleTitle(section), articles);
                }
            }
        }
        return reportBuilder.buildSystemActionsReport(userID, from, to, sectionArticle);
    }

    /**
     * Возвращает название секции
     * @param section
     * @return
     */
    private String getArticleTitle(Integer section) {
        return getArticleByID(section).getTitle();
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
     * Составляет отчет о действия подчиненных супервизора за данный период. Создает xlsx файл и размечает поля в соответствии
     * со структурой отчетв.
     * В отчете:
     *    • Период
     *    • ФИО оператора
     *    • Название статей, просмотренных оператором
     *    • Количество просмотров за период одной и той же статьи
     * @param userID
     * @param from - начальная дата
     * @param to - конечная дата
     * @param sections - разделы по которым хотят получить отчет
     * @return Путь к созданному отчету
     * @throws Exception
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

    /**
     * Находит пользователь которые работают с данными разделами
     * @param sections
     * @return
     * @throws Exception
     */
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
                    getArticleTitle(article.getId()),
                    article.getRank());
        }
        return articlesUserViewedWithNumOfViews;
    }

    private Article getArticleByID(int articleID) {
        return dataCollector.findArticle(articleID);
    }

    /**
     * Достает все записи из лога
     * @return Список записей в логе
     * @throws Exception
     */
    private LinkedList<ALogRecord> getRecordsFromLog() throws Exception {
        if(logRecords == null)
            logRecords = logReader.getRecordsFromLog();
        return logRecords;
    }

    /**
     * Составляет отчет о поиске в системе за данный период. Создает xlsx файл и размечает поля в соответствии
     * со структурой отчетв.
     * В отчете:
     *   • Количество раз использования функции поиска
     *   • «Часто ищут» в разбивке по разделам 1 уровня
     *   • Разбивка поисковых запросов по разделам 1 уровня и пользователям с их запросами
     * @param from - начальная дата
     * @param to - конечная дата
     * @param sections - разделы по которым хотят получить отчет
     * @return Путь к созданному отчету
     * @throws Exception
     */
    public String getSearchActionsReport(int userID, Timestamp from, Timestamp to, List<Integer> sections)
            throws Exception {
        Integer numSearchActions = numTimesSearchWasMade(from, to);

        //Popular Search requests grouped by first level sections
        //Search requests grouped by first level sections and people who mede them
        HashMap<Integer, LinkedList<String>> allUsersWithRequestsForPeriod = analyser.getUserRequests(
                getRecordsFromLog(), from, to);
        List<RequestRank> popularRequests = analyser.getPopularRequests(getRecordsFromLog(), from, to);
        Map<String, LinkedList<RequestRank>> levelPopularRequests = new HashMap<>();


        Map<Integer, LinkedList<Integer>> allFirstLevelSectionsWithSubsections = getListOfSectionsForEachLeavel();
        // Making map with all first level sections and users in those sections
        Map<String, Map<String, LinkedList<String>>> levelUsersRequests = new HashMap<>();
        for(Integer firstLevelSection : allFirstLevelSectionsWithSubsections.keySet()){
            // Checking that all given sections are first level and form a list of all needed first level sections
            if(!sections.contains(firstLevelSection)){
                allFirstLevelSectionsWithSubsections.remove(firstLevelSection);
            }else{
                levelPopularRequests.put(
                        getArticleTitle(firstLevelSection),
                        new LinkedList<>()
                );
                //Form list of all sections of that level
                List<Integer> sectionsOnLevel = getAllSectionsOnLevel(allFirstLevelSectionsWithSubsections, firstLevelSection);
                //Checks if those users made search requests during the period and gets those requests
                Map<String, LinkedList<String>> usersRequests = new HashMap<>();
                for(UserArticleRole userOfSection : getUsersOfEachSection(sectionsOnLevel)){
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
                        levelPopularRequests.get(getArticleTitle(firstLevelSection)).addAll(popularRequestsOnLevel);
                    }
                }
                levelUsersRequests.put(getArticleTitle(firstLevelSection), usersRequests);
            }
        }

        return reportBuilder.buildSearchActionsReport(userID, from, to, numSearchActions,
                levelUsersRequests, levelPopularRequests);
    }

    private String getUsersFullName(UserArticleRole userOfSection) {
        return userOfSection.getUser().getFullName();
    }

    /**
     * Добавлет в список все статьи находящиеся на уровнях ниже данной и ее саму и создает список статей первого уровня
     * с соответствующим ему списком сттей нижних уровней
     * @param allFirstLevelSectionsWithSubsections
     * @param firstLevelSection
     * @return
     */
    private List<Integer> getAllSectionsOnLevel(Map<Integer, LinkedList<Integer>> allFirstLevelSectionsWithSubsections,
                                                Integer firstLevelSection) {
        List<Integer> sectionsOnLevel = new LinkedList<>();
        sectionsOnLevel.add(firstLevelSection);
        sectionsOnLevel.addAll(allFirstLevelSectionsWithSubsections.get(firstLevelSection));
        return sectionsOnLevel;
    }

    private Integer numTimesSearchWasMade(Timestamp from, Timestamp to) throws Exception {
        Integer numSearchActions = analyser.getSearchUsage(getRecordsFromLog(), from, to);
        return numSearchActions;
    }

    public Map<Integer,LinkedList<Integer>> getListOfSectionsForEachLeavel() {
        // TODO: get list of sections by each level
        return null;
    }
}

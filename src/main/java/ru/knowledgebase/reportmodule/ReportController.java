package ru.knowledgebase.reportmodule;

import ru.knowledgebase.analyticsmodule.Analyser;
import ru.knowledgebase.analyticsmodule.rank.ArticleRank;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.exceptionmodule.reportexception.UnableToCreateReportExeption;
import ru.knowledgebase.loggermodule.Log.LogReader;
import ru.knowledgebase.modelsmodule.rolemodels.UserArticleRole;
import ru.knowledgebase.usermodule.UserController;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Мария on 01.10.2016.
 */
public class ReportController {

    private static volatile ReportController instance;

    private UserController userController = new UserController();
    private ReportBuilder reportBuilder = new ReportBuilder();
    private Analyser analyser = new Analyser();
    private LogReader logReader = new LogReader();
    private DataCollector dataCollector = new DataCollector();

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
     • Количество раз использования функции поиска
     • «Часто ищут» в разбивке по разделам 1 уровня
     • Разбивка поисковых запросов по разделам 1 уровня и пользователям с их запросами

     * @param from
     * @param to
     * @param sections
     * @return
     */
    public String getSystemActionsReport(Timestamp from, Timestamp to, List<Integer> sections) {
        //TODO
        String path = reportBuilder.buildSystemActionsReport(from, to, sections);
        return path;
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

        // For get users each section
        List <UserArticleRole> users = new LinkedList<>();
        for (Integer section : sections) {
            users.addAll(userController.getSectionUsers(section));
        }

        //Get Articles They Viewed and number of views
        Map<String, Map<String, Integer>> usersArticlesNumViews = new HashMap<>();
        for (UserArticleRole userRole: users) {
            usersArticlesNumViews.put(
                    userRole.getUser().getFullName(),
                    getArticlesUserViewedWithNumOfViews(userRole, from, to));
        }

        return reportBuilder.buildEmployeesActionsReport(userID, from, to, usersArticlesNumViews);
    }

    private Map<String, Integer> getArticlesUserViewedWithNumOfViews(UserArticleRole userRole,
                                                                     Timestamp from, Timestamp to) {
        List<ArticleRank> articlesViewed = analyser.getUserViews(
                logReader.getRecordsFromLog(),
                userRole.getUser().getId(),
                from, to);
        Map<String, Integer> articlesUserViewedWithNumOfViews = new HashMap<>();
        for(ArticleRank article : articlesViewed){
            articlesUserViewedWithNumOfViews.put(
                    dataCollector.findArticle(article.getId()).getTitle(),
                    article.getRank());
        }
        return articlesUserViewedWithNumOfViews;
    }

    /**
     • Количество выгруженных элементов (статей)
     • Разбивка статей в соответствии с разделами 1 уровня и ниже (сортировка)
     • Название статьи
     • Дата создания, автор
     • Дата последнего изменения, автор
     • Версия
     • Размер

     * @param from
     * @param to
     * @param sections
     * @return
     */
    public String getSearchActionsReport(Timestamp from, Timestamp to, List<Integer> sections) {
        //TODO
        String path = reportBuilder.buildSearchActionsReport(from, to, sections);
        return path;
    }
}

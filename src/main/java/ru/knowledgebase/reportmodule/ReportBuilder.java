package ru.knowledgebase.reportmodule;

import ru.knowledgebase.exceptionmodule.reportexception.UnableToCreateReportExeption;
import ru.knowledgebase.modelsmodule.articlemodels.Article;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Мария on 01.10.2016.
 */
public class ReportBuilder {

    private XLSBuilder xlsBuilder;

    /**
     • Количество раз использования функции поиска
     • «Часто ищут» в разбивке по разделам 1 уровня
     • Разбивка поисковых запросов по разделам 1 уровня и пользователям с их запросами

     *
     * @param userID
     * @param from
     * @param to
     * @param sections
     * @return
     */
    public String buildSearchActionsReport(int userID, Timestamp from, Timestamp to,
                                           List<Integer> sections) {
        String reportType = "SearchActionsReport";
        String reportName = getReportName(userID, from, to, reportType);
        return null;
    }

    /**
     • Дата
     • ФИО оператора
     • Название статей, просмотренных оператором
     • Количество просмотров за день одной и той же статьи

     Sheet 1: Statistics
     |Dates|
     Sheet 2: Articles views
     |Name of user|Name of article|Num of views

     * @param from
     * @param to
     * @param usersArticlesNumViews
     * @return
     */
    public String buildEmployeesActionsReport(int userID, Timestamp from, Timestamp to,
                                              Map<String, Map<String, Integer>> usersArticlesNumViews)
                                                throws UnableToCreateReportExeption {
        String reportType = "EmployeesActionsReport";
        String date = getDate(from, to);
        String reportName = getReportName(userID, date, reportType);

        List<Object[]> statisticsRows = new LinkedList<>();
        statisticsRows.add(createRow(date));

        List<Object[]> rows = new LinkedList<>();
        rows.add(createRow(
                "User name",
                "Articles user viewed during given period",
                "Number of times article was viewed during given period"
        ));
        Set<String> users = usersArticlesNumViews.keySet();
        for(String user : users){
            Map<String, Integer> articlesViews = usersArticlesNumViews.get(user);
            Set<String> articles = articlesViews.keySet();
            int i = 0;
            String firstCell = user;
            for(String article : articles){
                if( i != 0){
                    firstCell = "";
                }
                rows.add(createRow(
                        firstCell, article, Integer.toString(articlesViews.get(article))
                ));
                i++;
            }
        }

        createReport(reportName);
        createSheet(statisticsRows, "Statistics");
        createSheet(rows, "Articles views");
        return saveReport();
    }

    private String saveReport() throws UnableToCreateReportExeption {
        try {
            xlsBuilder.saveXLS();
        } catch (Exception e) {
            throw new UnableToCreateReportExeption();
        }
        return xlsBuilder.getPath();
    }

    private String getDate(Timestamp from, Timestamp to) {
        return from.toString() + " - " + to.toString();
    }

    private String getReportName(int userID, String date, String reportType){
        return reportType + "-" + userID + "-" + date;
    }

    /**
     • Количество выгруженных элементов (статей)
     • Разбивка статей в соответствии с разделами 1 уровня и ниже (сортировка)
     • Название статьи
     • Дата создания, автор
     • Дата последнего изменения, автор
     • Версия
     • Размер

     Sheet 1: Statistics
     |Num elements|NUM|
     Sheet 2: Created articles
     |Level|Article Name|Date of creation|Author|Date of last change|Size

     * @param from
     * @param to
     * @param levelArticle
     * @return
     */
    public String buildSystemActionsReport(int userID, Timestamp from, Timestamp to,
                                           Map<Integer, List<Article>> levelArticle)
                                            throws UnableToCreateReportExeption {
        String reportType = "SystemActionsReport";
        String dates = getDate(from, to);
        String reportName = getReportName(userID, dates, reportType);

        List<Object[]> statisticsRows = new LinkedList<>();
        statisticsRows.add(createRow(
                "Number of created articles", String.valueOf(levelArticle.size())
        ));
        statisticsRows.add(createRow(
                "Dates", dates
        ));

        List<Object[]> rows = new LinkedList<>();
        rows.add(createRow(
                "Level",
                "Article name",
                "Date of creation",
                "Author",
                "Date of last change",
                "Size"
        ));
        Set<Integer> levels = levelArticle.keySet();
        for(Integer level : levels){
            List<Article> articles = levelArticle.get(level);
            int i = 0;
            String firstCell = String.valueOf(level);
            for(Article article : articles){
                if( i != 0){
                    firstCell = "";
                }
                rows.add(createRow(
                        firstCell, article.getTitle(),
                        article.getCreatedTime().toString(), article.getAuthor().getFullName(),
                        article.getUpdatedTime().toString(),
                        String.valueOf(article.getClearBody().length())
                ));
                i++;
            }
        }

        createReport(reportName);
        createSheet(rows, "Statistics");
        createSheet(rows, "Created articles");
        return saveReport();
    }

    private String[] createRow(String ... cellValues){
        return cellValues;
    }
    private void createReport(String reportName) {
        xlsBuilder = new XLSBuilder(reportName);
    }

    private void createSheet(List<Object[]> rows, String sheetName) {
        xlsBuilder.printToSheet(
                xlsBuilder.addSheetToXLS(sheetName),
                rows
        );
    }


}

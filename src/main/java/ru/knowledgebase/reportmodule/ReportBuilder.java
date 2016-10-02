package ru.knowledgebase.reportmodule;

import ru.knowledgebase.analyticsmodule.rank.RequestRank;
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
     * Составляет отчет о поиске в системе за данный период. Создает xlsx файл и размечает поля в соответствии
     * со структурой отчетв.
     * В отчете:
     *   • Количество раз использования функции поиска
     *   • «Часто ищут» в разбивке по разделам 1 уровня
     *   • Разбивка поисковых запросов по разделам 1 уровня и пользователям с их запросами
     * Разметка xlsx:
     *    Страница 1: Статистика
     *    |Период|Количество поисковых запросов в системе|
     *    Страница 2: Частые поисковые запросы по разделам
     *    |Раздел первого уровня|Поисковой зарос|Количество раз искали|
     *    Страница 3: Поисковые запросы по поьзователям и разделам
     *    |Раздел первого уровня|Пользователь|Поисковой зарос|
     * @param userID
     * @param from
     * @param to
     * @param sectionUsersRequests
     * @param sectionsRequests
     * @return путь к созданноу отчету
     */
    public String buildSearchActionsReport(int userID, Timestamp from, Timestamp to, int numSearchActions,
                                           Map<String, Map<String, LinkedList<String>>> sectionUsersRequests,
                                           Map<String, LinkedList<RequestRank>> sectionsRequests)
                                            throws UnableToCreateReportExeption {
        String reportType = "Отчео о поиске в системе";
        String dates = getDate(from, to);
        String reportName = getReportName(userID, dates, reportType);

        List<Object[]> statisticsRows = new LinkedList<>();
        statisticsRows.add(createRow("Период", "Количество поисковых запросов в системе"));
        statisticsRows.add(createRow(dates, String.valueOf(numSearchActions)));

        List<Object[]> popularSearchReqsRows = new LinkedList<>();
        popularSearchReqsRows.add(createRow(
                "Раздел первого уровня",
                "Поисковой зарос",
                "Количество раз искали"
        ));

        List<Object[]> frequentlySearchedWithUsersRows = new LinkedList<>();
        frequentlySearchedWithUsersRows.add(createRow(
                "Раздел первого уровня",
                "Пользователь",
                "Поисковой зарос"
        ));

        for(String section : sectionsRequests.keySet()){
            LinkedList<RequestRank> requestRanks = sectionsRequests.get(section);
            int i = 0;
            String firstCell = section;
            for(RequestRank req : requestRanks){
                if( i != 0){
                    firstCell = "";
                }
                popularSearchReqsRows.add(createRow(
                        firstCell, req.getRequest(), String.valueOf(req.getRank()))
                );
                i++;
            }
        }

        for(String section : sectionUsersRequests.keySet()) {
            Map<String, LinkedList<String>> userReq = sectionUsersRequests.get(section);
            Set<String> users = userReq.keySet();
            int i = 0;
            String firstCell = section;
            for (String user : users) {

                LinkedList<String> reqs = userReq.get(user);
                int j = 0;
                String secondCell = user;
                for (String req : reqs){
                    if (i != 0) {
                        firstCell = "";
                    }
                    if (j != 0) {
                        firstCell = "";
                    }
                    frequentlySearchedWithUsersRows.add(createRow(
                            firstCell, secondCell, req)
                    );
                    i++;
                    j++;
                }
            }
        }


        createReport(reportName);
        createSheet(statisticsRows, "Статистика");
        createSheet(popularSearchReqsRows, "Частые поисковые запросы по разделам");
        createSheet(frequentlySearchedWithUsersRows, "Поисковые запросы по поьзователям и разделам");
        return saveReport();
    }

    /**
     * Составляет отчет о действия подчиненных супервизора за данный период. Создает xlsx файл и размечает поля в соответствии
     * со структурой отчетв.
     * В отчете:
     *    • Период
     *    • ФИО оператора
     *    • Название статей, просмотренных оператором
     *    • Количество просмотров за период одной и той же статьи
     * Разметка xlsx:
     *    Страница 1: Статистика
     *    |Период|
     *    Страница 2: Просмотренные статьи
     *    |Имя пользователя|Название статьи|Количество просмотров|
     * @param from - начальная дата
     * @param to - конечная дата
     * @param usersArticlesNumViews - содержит всею пользователей, просмотренные ими статьи и количество
     * просмотров за данный период
     * @return Путь к созданному отчету
     */
    public String buildEmployeesActionsReport(int userID, Timestamp from, Timestamp to,
                                              Map<String, Map<String, Integer>> usersArticlesNumViews)
                                                throws UnableToCreateReportExeption {
        String reportType = "Отчет о действиях подчиненных";
        String dates = getDate(from, to);
        String reportName = getReportName(userID, dates, reportType);

        List<Object[]> statisticsRows = new LinkedList<>();
        statisticsRows.add(createRow("Период"));
        statisticsRows.add(createRow(dates));

        List<Object[]> rows = new LinkedList<>();
        rows.add(createRow(
                "Имя пользователя",
                "Название статьи",
                "Количество просмотров"
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
        createSheet(statisticsRows, "Статистика");
        createSheet(rows, "Просмотренные статьи");
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
     * Разметка xlsx:
     *    Sheet 1: Статистика
     *    |Период|Количество созданных статей|
     *    Sheet 2: Созданные статьи
     *    |Назваание раздела|Название статьи|Дата создани статьи|Автор|Дата последнего обновления|Размер|
     * @param from
     * @param to
     * @param levelArticle
     * @return путь к созданному отчету
     */
    public String buildSystemActionsReport(int userID, Timestamp from, Timestamp to,
                                           Map<String, List<Article>> levelArticle)
                                            throws UnableToCreateReportExeption {
        String reportType = "Отчет о действиях в системе";
        String dates = getDate(from, to);
        String reportName = getReportName(userID, dates, reportType);

        List<Object[]> statisticsRows = new LinkedList<>();
        statisticsRows.add(createRow("Период", "Количество созданных статей"));
        statisticsRows.add(createRow(dates, String.valueOf(levelArticle.size())));

        List<Object[]> rows = new LinkedList<>();
        rows.add(createRow(
                "Название раздела",
                "Название статьи",
                "Дата создания",
                "Автор",
                "Дата последнего изменения",
                "Размер"
        ));
        Set<String> levels = levelArticle.keySet();
        for(String level : levels){
            List<Article> articles = levelArticle.get(level);
            int i = 0;
            String firstCell = level;
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
        createSheet(rows, "Статистика");
        createSheet(rows, "Созданные стаьи");
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

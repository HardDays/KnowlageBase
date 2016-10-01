package ru.knowledgebase.reportmodule;

import ru.knowledgebase.exceptionmodule.reportexception.UnableToCreateReportExeption;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Мария on 01.10.2016.
 */
public class ReportBuilder {

    private XLSBuilder xlsBuilder;
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
    public String buildSearchActionsReport(Timestamp from, Timestamp to, List<Integer> sections) {

        return null;
    }

    /**
     • Дата
     • ФИО оператора
     • Название статей, просмотренных оператором
     • Количество просмотров за день одной и той же статьи

     * @param from
     * @param to
     * @param usersArticlesNumViews
     * @return
     */
    public String buildEmployeesActionsReport(int userID, Timestamp from, Timestamp to,
                                              Map<String, Map<String, Integer>> usersArticlesNumViews)
                                                throws UnableToCreateReportExeption {
        String date = from.toString() + " - " + to.toString();
        String reportName = "EmployeesActionsReport-" + userID + "-" + date;

        List<Object[]> rows = new LinkedList<>();
        String[] dataRow = {date, " ", " "};
        rows.add(dataRow);
        String[] headerRow = {
                "User name",
                "Articles user viewed during given period",
                "Number of times article was viewed during given period"};
        rows.add(headerRow);
        Set<String> users = usersArticlesNumViews.keySet();
        for(String user : users){
            Map<String, Integer> articlesViews = usersArticlesNumViews.get(user);
            Set<String> articles = articlesViews.keySet();
            int i = 0;
            for(String article : articles){
                if( i == 0){
                    String[] firstRow = {user, article, Integer.toString(articlesViews.get(article))};
                    rows.add(firstRow);
                }else{
                    String[] generalRow = {"", article, Integer.toString(articlesViews.get(article))};
                    rows.add(generalRow);
                }
                i++;
            }
        }

        xlsBuilder = new XLSBuilder(reportName);
        xlsBuilder.printToSheet(
                xlsBuilder.addSheetToXLS("1"),
                rows
        );
        try {
            xlsBuilder.saveXLS();
        } catch (Exception e) {
            throw new UnableToCreateReportExeption();
        }
        return xlsBuilder.getPath();
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
    public String buildSystemActionsReport(Timestamp from, Timestamp to, List<Integer> sections) {

        return null;
    }


}

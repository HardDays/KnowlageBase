package ru.knowledgebase.analyticsmodule;

import ru.knowledgebase.analyticsmodule.rank.ArticleRank;
import ru.knowledgebase.analyticsmodule.rank.OperationFrequency;
import ru.knowledgebase.analyticsmodule.rank.RequestRank;
import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.exceptionmodule.databaseexceptions.DataBaseException;
import ru.knowledgebase.loggermodule.LogRecord.ALogRecord;
import ru.knowledgebase.loggermodule.LogRecord.SearchRequestRecord;
import ru.knowledgebase.loggermodule.LogRecord.SearchResultRecord;
import ru.knowledgebase.loggermodule.logenums.OPERATION;
import ru.knowledgebase.modelsmodule.articlemodels.Article;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by vova on 05.09.16.
 */
public class Analyser {

    private DataCollector dataCollector = new DataCollector();
    private RequestParser requestParser = new RequestParser();

    private static volatile Analyser instance;

    public static Analyser getInstance() {
        Analyser localInstance = instance;
        if (localInstance == null) {
            synchronized (Analyser.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Analyser();
                }
            }
        }
        return localInstance;
    }
    /**
     * Find popular articles in log
     * @param log list with log records
     * @return list of articles and rank ordered by rank
     */
    public List<ArticleRank> getPopularArticles(List <ALogRecord> log){
        //id статьи и количество переходов на нее
        Map <Integer, Integer> articles = new HashMap <Integer, Integer>();
        for (ALogRecord rec : log){
            if (rec.getOperationType() == OPERATION.SEARCH_RESULT) {
                SearchResultRecord record = (SearchResultRecord) rec;
                Integer id = record.getArticleID();
                if (articles.containsKey(id)){
                    articles.put(id, articles.get(id) + 1);
                } else {
                    articles.put(id, 1);
                }
            }
        }
        //формируем результат
        List <ArticleRank> result = new LinkedList<>();
        for (Map.Entry<Integer, Integer> article : articles.entrySet()){
            result.add(new ArticleRank(article.getKey(), article.getValue()));
        }
        //сортировка по количеству переходов
        Collections.sort(result, (ArticleRank rec1, ArticleRank rec2) -> rec2.getRank().compareTo(rec1.getRank()));
        return result;
    }
    /**
     * Find relevant articles for requests
     * @param log list with log records
     * @return hashmap with request key and list of articles with rank ordered by rank
     */
    public HashMap<String, List<ArticleRank>> getRelevantArticles(List <ALogRecord> log) throws Exception{
        //сортируем лог по дате
        Collections.sort(log, (ALogRecord rec1, ALogRecord rec2) -> rec1.getTime().compareTo(rec2.getTime()));
        //для каждого ключевого слова запроса храним статью и количество переходов по ней
        HashMap<String, HashMap<Integer, Integer>> result = new HashMap<String, HashMap<Integer, Integer>>();
        for (int i = 0; i < log.size(); i++){
            ALogRecord rec = log.get(i);
            if (rec.getOperationType() == OPERATION.SEARCH_REQUEST) {
                SearchRequestRecord request = (SearchRequestRecord) rec;
                //ищем через 1000 ответов
                int size = Math.min(i + 1000, log.size());
                for (int k = i + 1; k < size; k++) {
                    ALogRecord rec2 = log.get(k);
                    //если нашли запись в логе нужного юзера
                    if(rec2.getUserID() == request.getUserID()){
                        //если это ответ на запрос
                        if (rec2.getOperationType() == OPERATION.SEARCH_RESULT) {
                            SearchResultRecord response = (SearchResultRecord) rec2;
                            int articleId = response.getArticleID();
                            //для каждого ключевого слова в запросе увеличиваем количество
                            //статей, которые выдавались в ответе
                            for (String keyword : requestParser.getRequestKeywords(request.getSearchRequest())) {
                                //если еще ключевое слово не встречалось
                                if (!result.containsKey(keyword)) {
                                    result.put(keyword, new HashMap<Integer, Integer>());
                                }
                                //обновляем, количество выдачей статьи на запрос
                                HashMap<Integer, Integer> ranks = result.get(keyword);
                                if (ranks.containsKey(articleId)) {
                                    ranks.put(articleId, ranks.get(articleId) + 1);
                                } else {
                                    ranks.put(articleId, 1);
                                }
                            }
                        //если пользоватей начал что-то еще искать, прерываемся
                        }else if (rec2.getOperationType() == OPERATION.SEARCH_REQUEST){
                            break;
                        }
                    }
                }
            }
        }
        //формируем результат
        HashMap<String, List<ArticleRank>> sorted = new HashMap<String, List<ArticleRank>>();
        for (Map.Entry<String, HashMap<Integer, Integer>> keyw : result.entrySet()){
            List <ArticleRank> ranks = new LinkedList<>();
            for (Map.Entry<Integer, Integer> article : keyw.getValue().entrySet()){
                ranks.add(new ArticleRank(article.getKey(), article.getValue()));
            }
            //сортируем для каждого запроса статьи по релевантности
            Collections.sort(ranks, (ArticleRank rec1, ArticleRank rec2) -> rec2.getRank().compareTo(rec1.getRank()));
            sorted.put(keyw.getKey(), ranks);
        }
        return sorted;
    }
    /**
     * Find popular requests
     * @param log list with log records
     * @return list of requests and ranks ordered by rank
     */
    public List<RequestRank> getPopularRequests(List <ALogRecord> log) throws Exception{
        //храним количество встречаний каждого ключевого слова в запросе
        Map<String, Integer> keywords = new HashMap<String, Integer>();
        for (ALogRecord rec : log){
            if (rec.getOperationType() == OPERATION.SEARCH_REQUEST){
                String request = ((SearchRequestRecord)rec).getSearchRequest();
                for (String keyword : requestParser.getRequestKeywords(request)){
                    if (keywords.containsKey(keyword)) {
                        keywords.put(keyword, keywords.get(keyword) + 1);
                    } else {
                        keywords.put(keyword, 1);
                    }
                }
            }
        }
        //формируем результат
        List <RequestRank> result = new LinkedList<>();
        for (Map.Entry<String, Integer> article : keywords.entrySet()){
            result.add(new RequestRank(article.getKey(), article.getValue()));
        }
        //сортируем по убыванию
        Collections.sort(result, (RequestRank rec1, RequestRank rec2) -> rec2.getRank().compareTo(rec1.getRank()));
        return result;
    }
    /**
     * Calculate frequency for each operation
     * @param log list with log records
     * @return hashmap of operation and it's frequency and count
     */
    public HashMap<OPERATION, OperationFrequency> getOperationFrequency(List <ALogRecord> log){
        //сортируем лог по возрастанию времени
        Collections.sort(log, (ALogRecord rec1, ALogRecord rec2) -> rec1.getTime().compareTo(rec2.getTime()));
        //храним операцию и ее количество
        HashMap<OPERATION, OperationFrequency> operations = new HashMap<>();
        //общее число операций
        int totalCount = 0;
        for (ALogRecord rec : log){
            OPERATION operation = rec.getOperationType();
            if (operations.containsKey(operation)) {
                operations.get(operation).setCount(operations.get(operation).getCount() + 1);
            } else {
                operations.put(operation, new OperationFrequency(1, 0));
            }
            totalCount += 1;
        }
        //формируем результат
        for (Map.Entry<OPERATION, OperationFrequency> op : operations.entrySet()){
            int count = op.getValue().getCount();
            //считаем процент от общего числа операций
            op.getValue().setFrequency(100 * count / totalCount);
        }
        return operations;
    }
    /**
     * Calculate average time between each type of operations
     * @param log list with log records
     * @return hashmap of operation and it's average time in milliseconds
     */
    public HashMap<OPERATION, Long> getAverageOperationTime(List <ALogRecord> log){
        //предыдущее время для запроса
        HashMap<OPERATION, Long> previous = new HashMap<>();
        //суммарное время запроса
        HashMap<OPERATION, Long> time = new HashMap<>();
        //число запрсов
        HashMap<OPERATION, Long> count = new HashMap<>();
        for (ALogRecord rec : log){
            OPERATION operation = rec.getOperationType();
            Long curTime = rec.getTime().getTime();
            //если первый раз встретили запрос
            if (!previous.containsKey(operation)) {
                previous.put(operation, curTime);
                time.put(operation, 0L);
            }else{
                //разница с предыдущим временем
                Long diff = curTime - previous.get(operation);
                //суммируем с общим временем
                Long newTime = diff + time.get(operation);
                time.put(operation, newTime);
                previous.put(operation, curTime);
            }
            //считаем количество каждлго типа запросов
            if (count.containsKey(operation)) {
                count.put(operation, count.get(operation) + 1L);
            }else{
                count.put(operation, 1L);
            }
        }
        //формируем результат
        for (Map.Entry<OPERATION, Long> entry : time.entrySet()){
            OPERATION operation = entry.getKey();
            //среднее время запроса
            Long newTime = entry.getValue() / count.get(operation);
            time.put(entry.getKey(), newTime);
        }
        return time;
    }
    /**
     * Filter log records by time period
     * @param log list with log records
     * @param from lower bound of time period
     * @param to higher bound of time period
     * @return filtered list of log records
     */
    private List<ALogRecord> filterTime(List <ALogRecord> log, Timestamp from, Timestamp to){
        List <ALogRecord> newLog = new LinkedList<>();
        for (ALogRecord rec : log){
            Long time = rec.getTime().getTime();
            if (time >= from.getTime() && time <= to.getTime()){
                newLog.add(rec);
            }
        }
        return newLog;
    }
    /**
     * Filter log records sections
     * @param log list with log records
     * @param parentId id of parent article
     * @return filtered list of log records
     */
    private List<ALogRecord> filterParent(List <ALogRecord> log, int parentId) throws Exception{
        List <ALogRecord> newLog = new LinkedList<>();
        for (ALogRecord rec : log){
            if (rec.getOperationType() == OPERATION.SEARCH_RESULT){
                SearchResultRecord res = (SearchResultRecord)rec;
                try {
                    //find parent article
                    Article temp = dataCollector.findArticle(res.getArticleID());
                    while(temp != null){
                        if (temp.getId() == parentId)
                            break;
                        temp = temp.getParentArticle();
                    }
                    if (temp != null) {
                        if (temp.getId() == parentId)
                            newLog.add(rec);
                    }
                }catch (Exception e){
                    throw new DataBaseException();
                }
            }
        }
        return newLog;
    }
    /**
     * Find popular articles in log in time period
     * @param log list with log records
     * @param from lower bound of time period
     * @param to higher bound of time period
     * @return list of articles and rank ordered by rank
     */
    public List<ArticleRank> getPopularArticles(List <ALogRecord> log, Timestamp from, Timestamp to) throws Exception{
        return getPopularArticles(filterTime(log, from, to));
    }
    /**
     * Find popular articles in log in time period in section
     * @param log list with log records
     * @param parentId id of parent article
     * @param from lower bound of time period
     * @param to higher bound of time period
     * @return list of articles and rank ordered by rank
     */
    public List<ArticleRank> getPopularArticles(List <ALogRecord> log, int parentId, Timestamp from, Timestamp to) throws Exception{
        return getPopularArticles(filterParent(filterTime(log, from, to), parentId));
    }
    /**
     * Find popular articles in log in section
     * @param log list with log records
     * @param parentId id of parent article
     * @return list of articles and rank ordered by rank
     */
    public List<ArticleRank> getPopularArticles(List <ALogRecord> log, int parentId) throws Exception{
        return getPopularArticles(filterParent(log, parentId));
    }
    /**
     * Find relevant articles for requests in time period
     * @param log list with log records
     * @param from lower bound of time period
     * @param to higher bound of time period
     * @return hashmap with request key and list of articles with rank ordered by rank
     */
    public HashMap<String, List<ArticleRank>> getRelevantArticles(List <ALogRecord> log, Timestamp from, Timestamp to) throws Exception{
        return getRelevantArticles(filterTime(log, from, to));
    }
    /**
     * Find relevant articles for requests in time period in section
     * @param log list with log records
     * @param parentId id of parent article
     * @param from lower bound of time period
     * @param to higher bound of time period
     * @return hashmap with request key and list of articles with rank ordered by rank
     */
    public HashMap<String, List<ArticleRank>> getRelevantArticles(List <ALogRecord> log, int parentId, Timestamp from, Timestamp to) throws Exception{
        return getRelevantArticles(filterParent(filterTime(log, from, to), parentId));
    }
    /**
     * Find relevant articles for requests in time period in section
     * @param log list with log records
     * @param parentId id of parent article
     * @return hashmap with request key and list of articles with rank ordered by rank
     */
    public HashMap<String, List<ArticleRank>> getRelevantArticles(List <ALogRecord> log, int parentId) throws Exception{
        return getRelevantArticles(filterParent(log, parentId));
    }
    /**
     * Find popular requests in time period
     * @param log list with log records
     * @param from lower bound of time period
     * @param to higher bound of time period
     * @return list of requests and ranks ordered by rank
     */
    public List<RequestRank> getPopularRequests(List <ALogRecord> log, Timestamp from, Timestamp to) throws Exception{
        return getPopularRequests(filterTime(log, from, to));
    }
    /**
     * Calculate frequency for each operation in time period
     * @param log list with log records
     * @param from lower bound of time period
     * @param to higher bound of time period
     * @return hashmap of operation and it's frequency and count
     */
    public HashMap<OPERATION, OperationFrequency> getOperationFrequency(List <ALogRecord> log, Timestamp from, Timestamp to) throws Exception{
        return getOperationFrequency(filterTime(log, from, to));
    }
    /**
     * Calculate average time between each type of operations in time period
     * @param log list with log records
     * @param from lower bound of time period
     * @param to higher bound of time period
     * @return hashmap of operation and it's average time in milliseconds
     */
    public HashMap<OPERATION, Long> getAverageOperationTime(List <ALogRecord> log, Timestamp from, Timestamp to) throws Exception{
        return getAverageOperationTime(filterTime(log, from, to));
    }
}

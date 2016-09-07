package ru.knowledgebase.analyticsmodule;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.knowledgebase.analyticsmodule.rank.ArticleRank;
import ru.knowledgebase.analyticsmodule.rank.OperationFrequency;
import ru.knowledgebase.analyticsmodule.rank.RequestRank;
import ru.knowledgebase.loggermodule.LogRecord.ALogRecord;
import ru.knowledgebase.loggermodule.LogRecord.SearchRequestRecord;
import ru.knowledgebase.loggermodule.LogRecord.SearchResultRecord;
import ru.knowledgebase.loggermodule.logenums.OPERATION;

import java.io.File;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.stachek66.nlp.mystem.holding.Factory;
import ru.stachek66.nlp.mystem.holding.MyStem;
import ru.stachek66.nlp.mystem.holding.Request;
import ru.stachek66.nlp.mystem.model.Info;
import scala.Option;
import scala.collection.JavaConversions;

/**
 * Created by vova on 05.09.16.
 */
public class Analyser {

    // private DataCollector dataCollector = new DataCollector();
    private RequestParser requestParser = new RequestParser();

    public List<ArticleRank> getPopularArticles(List <ALogRecord> log){

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
        List <ArticleRank> result = new LinkedList<>();
        for (Map.Entry<Integer, Integer> article : articles.entrySet()){
            result.add(new ArticleRank(article.getKey(), article.getValue()));
        }
        Collections.sort(result, (ArticleRank rec1, ArticleRank rec2) -> rec2.getRank().compareTo(rec1.getRank()));
        return result;
    }

    public HashMap<String, List<ArticleRank>> getRelevantArticles(List <ALogRecord> log) throws Exception{
        Collections.sort(log, (ALogRecord rec1, ALogRecord rec2) -> rec1.getTime().compareTo(rec2.getTime()));

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

    public List<RequestRank> getPopularRequests(List <ALogRecord> log) throws Exception{
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
        List <RequestRank> result = new LinkedList<>();
        for (Map.Entry<String, Integer> article : keywords.entrySet()){
            result.add(new RequestRank(article.getKey(), article.getValue()));
        }
        Collections.sort(result, (RequestRank rec1, RequestRank rec2) -> rec2.getRank().compareTo(rec1.getRank()));
        return result;
    }

    public List <OperationFrequency> getRequestFrequency(List <ALogRecord> log){
        Collections.sort(log, (ALogRecord rec1, ALogRecord rec2) -> rec1.getTime().compareTo(rec2.getTime()));

       // Calendar cal = Calendar.getInstance();
        //cal.setTime(log.get(0).getTime());
        //int firstDay = cal.get(Calendar.DAY_OF_MONTH);

        HashMap<OPERATION, Integer> operations = new HashMap<OPERATION, Integer>();
        int totalCount = 0;
        for (ALogRecord rec : log){
           // cal.setTime(rec.getTime());
           // if (cal.get(Calendar.DAY_OF_MONTH) != firstDay)
                //break;
            OPERATION operation = rec.getOperationType();
            if (operations.containsKey(operation)) {
                operations.put(operation, operations.get(operation) + 1);
            } else {
                operations.put(operation, 1);
            }
            totalCount += 1;
        }
        List <OperationFrequency> result = new LinkedList<>();
        for (Map.Entry<OPERATION, Integer> op : operations.entrySet()){
            int count = op.getValue();
            result.add(new OperationFrequency(op.getKey(), count, 100 * count / totalCount));
        }
        return result;
    }



}

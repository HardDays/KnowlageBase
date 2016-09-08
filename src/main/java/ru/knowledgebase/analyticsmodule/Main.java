package ru.knowledgebase.analyticsmodule;

import ru.knowledgebase.analyticsmodule.rank.ArticleRank;
import ru.knowledgebase.analyticsmodule.rank.OperationFrequency;
import ru.knowledgebase.loggermodule.LogRecord.ALogRecord;
import ru.knowledgebase.loggermodule.LogRecord.CRUDRecord;
import ru.knowledgebase.loggermodule.LogRecord.SearchRequestRecord;
import ru.knowledgebase.loggermodule.LogRecord.SearchResultRecord;
import ru.knowledgebase.loggermodule.logenums.OPERATION;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by vova on 06.09.16.
 */
public class Main {

    public static void main(String [] a) throws Exception{
        Analyser an = new Analyser();
        List<ALogRecord> log = new LinkedList<ALogRecord>();
        Random rand = new Random();
        for (int i = 0; i < 150; i++) {
            int id = 1;
            if (rand.nextInt(2) == 1){
                id = rand.nextInt(100);
            }
            try{
                //Thread.sleep(rand.nextInt(200) + 100);
            }catch (Exception e){

            }
        //    log.add(new CRUDRecord(OPERATION.SEARCH_RESULT, new Timestamp(System.currentTimeMillis()), 1, id));
        }

        LinkedList<String> words = new LinkedList<>();
        words.add(" Банк ");
        words.add(" ?банки ");
        words.add(" банков! ");
        words.add(" банковкские ");
        words.add(" ,банка ");
        words.add("   банковые ");
        words.add(" банк ");
        words.add(" ООО ");
        words.add(" ЯМАЛ ");
        words.add(" стол ");
        words.add(" столы   ");
        words.add("  супер   ");
        words.add("  стола  ");
        words.add("  супер-банк   ");
        words.add("  альфа-банк   ");
        words.add("  альфа банк   ");
        words.add("  бета банк   ");
        words.add("  купить банк   ");
        words.add("  продажа   ");
        words.add("  продать   ");

        /*
        log.add(new SearchRequestRecord( new Timestamp(System.currentTimeMillis()), 1, "Банк"));
        log.add(new SearchResultRecord( new Timestamp(System.currentTimeMillis()), 1, 2));
        log.add(new SearchResultRecord( new Timestamp(System.currentTimeMillis()), 1, 3));
        log.add(new SearchResultRecord( new Timestamp(System.currentTimeMillis()), 1, 5));
        log.add(new SearchResultRecord( new Timestamp(System.currentTimeMillis()), 2, 2));
        log.add(new SearchResultRecord( new Timestamp(System.currentTimeMillis()), 3, 2));
        log.add(new SearchRequestRecord( new Timestamp(System.currentTimeMillis()), 2, "банком"));
        log.add(new SearchResultRecord( new Timestamp(System.currentTimeMillis()), 2, 2));
        log.add(new SearchResultRecord( new Timestamp(System.currentTimeMillis()), 2, 5));
        log.add(new SearchResultRecord( new Timestamp(System.currentTimeMillis()), 2, 7));
        log.add(new SearchResultRecord( new Timestamp(System.currentTimeMillis()), 1, 9));
        log.add(new SearchRequestRecord( new Timestamp(System.currentTimeMillis()), 3, "альфа банк!!1"));
        log.add(new SearchResultRecord( new Timestamp(System.currentTimeMillis()), 3, 2));
        */


        log.add(new SearchResultRecord( new Timestamp(System.currentTimeMillis()), 1, 2));
        try{
            Thread.sleep(1000);
        }catch (Exception e){

        }
        Timestamp time = new Timestamp(System.currentTimeMillis());
        System.out.println(time);
        log.add(new SearchResultRecord(time, 1, 3));
        try{
            Thread.sleep(10000);
        }catch (Exception e){

        }
        time = new Timestamp(System.currentTimeMillis());
        System.out.println(time);
        log.add(new SearchResultRecord(time, 1, 3));
        try{
            Thread.sleep(3000);
        }catch (Exception e){

        }
        time = new Timestamp(System.currentTimeMillis());
        System.out.println(time);

        log.add(new SearchResultRecord(time, 1, 3));
        try{
            Thread.sleep(5000);
        }catch (Exception e){

        }
        time = new Timestamp(System.currentTimeMillis());
        System.out.println(time);

        log.add(new SearchResultRecord(time, 1, 3));
        an.getAverageRequestTime(log);


        /*
        for (int i = 0; i < 10; i++){
            String req = words.get(rand.nextInt(words.size()));
            System.out.println(req);
            log.add(new SearchRequestRecord( new Timestamp(System.currentTimeMillis()), 1, req));
        }*/

        /*
        List<OperationFrequency> res = an.getRequestFrequency(log);
        for (OperationFrequency f : res){
            System.out.println(f.getOperation() + " " + f.getCount() + " " + f.getFrequency());
        }*/
        /*
        HashMap<String, List<ArticleRank>> rs = an.getRelevantArticles(log);
        for (Map.Entry<String, List<ArticleRank>> e : rs.entrySet()){
            System.out.println(e.getKey());
            for (ArticleRank r : e.getValue()){
                System.out.println("id: " + r.getId() + " count: " + r.getRank());
            }
        }
        */
        System.out.println("finished");

    }
}

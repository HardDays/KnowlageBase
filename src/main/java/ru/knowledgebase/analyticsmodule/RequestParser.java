package ru.knowledgebase.analyticsmodule;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.knowledgebase.exceptionmodule.analyticsexceptions.ParseKeywordException;
import ru.stachek66.nlp.mystem.holding.Factory;
import ru.stachek66.nlp.mystem.holding.MyStem;
import ru.stachek66.nlp.mystem.holding.Request;
import ru.stachek66.nlp.mystem.model.Info;
import scala.Option;
import scala.collection.JavaConversions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.BreakIterator;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vova on 07.09.16.
 */
public class RequestParser {

    private MyStem mystemAnalyzer = new Factory("-iw --format json").newMyStem("3.0", Option.<File>empty()).get();
   // private String synonymPath = "/home/vova/Project BZ/syn.txt";
   // private HashMap<String, String> synonyms = new HashMap<>();
    private EnglishStemmer englishStemmer = new EnglishStemmer();

    private static volatile RequestParser instance;

    public static RequestParser getInstance() {
        RequestParser localInstance = instance;
        if (localInstance == null) {
            synchronized (RequestParser.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new RequestParser();
                }
            }
        }
        return localInstance;
    }
/*
    public void init(){
        File file = new File(synonymPath);
        RequestParser parser = RequestParser.getInstance();
        HashMap <String, Integer> keys = new HashMap<>();
        int i = 0;
        try  {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String [] spl = line.split(":");
                synonyms.put(spl[0], spl[1]);
            }
        }catch (Exception e){

        }
    }*/

    private final Set<String> types = new HashSet<String>() {{
        add("A");
        //add("ADV"); //kak, kogda
        add("ADVPRO");
        add("ANUM");
        add("APRO");
        add("COM");
        add("INTJ");
        add("NUM");
        add("S");
        add("SPRO");
        add("V"); //glagol
    }};
    /**
     * Find keywords in string
     * @param request string with request
     * @return list of keywords
     */
    public List<String> getRequestKeywords(String request) throws Exception {
        try {
            Iterable<Info> result = JavaConversions.asJavaIterable(mystemAnalyzer
                    .analyze(Request.apply(request))
                    .info()
                    .toIterable());
            List<String> keywords = new LinkedList<String>();

            for (Info info : result) {
                JSONObject obj = new JSONObject(info.rawResponse());
                JSONArray array = obj.getJSONArray("analysis");
                if (array.length() == 0)
                    continue;
                obj = (JSONObject) array.get(0);
                //get base word
                String base = (String) obj.get("lex");
                String type = null;
                Pattern pattern = Pattern.compile("([A-Z])+");
                Matcher matcher = pattern.matcher((String) obj.get("gr"));
                while (matcher.find()) {
                    type = matcher.group();
                }
                //synonmys, need to improve later
                if (types.contains(type)){
                //    if (synonyms.containsKey(base)){
               //         base = synonyms.get(base);
                //    }
                    keywords.add(base);
                }
            }
            String [] words = request.split("\\s+");
            for (int i = 0; i < words.length; i++) {
                words[i] = words[i].replaceAll("[^\\w]", "");
            }
            for (String word : words){
                if (!keywords.contains(word) && word.length() > 0){
                    englishStemmer.add(word.toCharArray(), word.length());
                    englishStemmer.stem();
                    String keyWord = new String(englishStemmer.getResultBuffer(), 0, englishStemmer.getResultLength());
                }
            }
            return keywords;
        } catch (Exception e) {
            throw new ParseKeywordException();
        }
    }
}

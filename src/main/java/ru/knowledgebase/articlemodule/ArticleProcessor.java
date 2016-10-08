package ru.knowledgebase.articlemodule;

import org.jsoup.Jsoup;

/**
 * Created by root on 25.08.16.
 */
public class ArticleProcessor {

    /**
     * Cut html tags from article body string
     * @param body - article body with html tags
     * @return - body without tags
     */
    public static String getPureBody(String body) throws Exception {
        String clearBody = Jsoup.parse(body).text();
        TextProcessor textProcessor = new TextProcessor();
        return textProcessor.getTextWithOnlyKeywords(clearBody);
    }
}

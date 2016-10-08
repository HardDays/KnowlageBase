package ru.knowledgebase.articlemodule;

import ru.knowledgebase.analyticsmodule.RequestParser;
import ru.knowledgebase.exceptionmodule.analyticsexceptions.ParseKeywordException;

import java.util.List;

/**
 * Created by root on 08.10.16.
 */
public class TextProcessor {
    RequestParser requestParser = new RequestParser();

    public String removePunctuation(String text){
        return text.replaceAll("[^a-zA-Z ]", "");
    }

    public List<String> getKeywords(String text) throws ParseKeywordException {
        //TODO: спросить Вову может он и так чистит от пунктуации
        return requestParser.getRequestKeywords(removePunctuation(text));
    }

    public String getTextWithOnlyKeywords(String text) throws ParseKeywordException {
        return buildTextOfList(getKeywords(text));
    }

    private static String buildTextOfList(List<String> words) {
        StringBuilder stringBuilder = new StringBuilder();
        for(String word : words){
            stringBuilder.append(word + " ");
        }
        return stringBuilder.toString();
    }
}

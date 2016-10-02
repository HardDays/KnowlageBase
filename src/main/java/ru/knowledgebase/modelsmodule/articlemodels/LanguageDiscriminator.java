package ru.knowledgebase.modelsmodule.articlemodels;

import org.hibernate.search.analyzer.Discriminator;
import ru.knowledgebase.modelsmodule.articlemodels.Article;

/**
 * Created by Мария on 19.09.2016.
 */
public class LanguageDiscriminator implements Discriminator {
    @Override
    public String getAnalyzerDefinitionName(Object value, Object entity, String field) {
        if ( value == null || !( entity instanceof Article) ) {
            return null;
        }
        return (String) value;
    }
}

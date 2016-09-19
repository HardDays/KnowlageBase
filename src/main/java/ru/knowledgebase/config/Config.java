/*package ru.knowledgebase.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

*//**
 * Created by Мария on 12.09.2016.
 *//*
@Configuration
@EnableJpaRepositories(basePackages = {
        "ru.knowledgebase.dbmodule.repositories.rolerepositories",
        "ru.knowledgebase.dbmodule.repositories.userrepositories"})
@EnableElasticsearchRepositories(basePackages =
        "ru.knowledgebase.dbmodule.repositories.articlerepositories")
public class Config {
    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchTemplate(nodeBuilder().local(true).node().client());
    }
}*/

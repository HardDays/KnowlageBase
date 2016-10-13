package ru.knowledgebase.configmodule;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.client.support.Headers;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.threadpool.ThreadPool;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.stereotype.Component;
import ru.knowledgebase.dbmodule.repositories.JPA.articlerepository.ArticleJPARepository;

import java.net.InetAddress;

import static org.elasticsearch.common.settings.Settings.settingsBuilder;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by Мария on 07.10.2016.
 */

@Component
@Configuration
//@EnableElasticsearchRepositories(basePackages = "ru.knowledgebase")
//@ComponentScan(basePackages = { "ru.knowledgebase" })
public class ElasticSearchConfig {

//    --
}

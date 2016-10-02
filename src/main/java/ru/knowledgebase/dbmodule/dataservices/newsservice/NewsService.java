package ru.knowledgebase.dbmodule.dataservices.newsservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.knowledgebase.dbmodule.repositories.articlerepositories.ImageRepository;
import ru.knowledgebase.dbmodule.repositories.newsrepositories.NewsRepository;
import ru.knowledgebase.modelsmodule.articlemodels.News;

import java.util.List;

/**
 * Created by root on 02.10.16.
 */

@Service("newsService")
public class NewsService {
    @Autowired
    private NewsRepository newsRepository;

    public News addNews(News news) {
        return this.newsRepository.save(news);
    }

    public News updateNews(News news) {
        return newsRepository.save(news);
    }

    public News findNews(int id) {
        return this.newsRepository.findOne(id);
    }

    public void deleteNews(int id) {
        this.newsRepository.delete(id);
    }

    public List<News> getAllNewsBySection(int sectionId) {
        return this.newsRepository.findNewsBySection(sectionId);
    }

    public void deleteAllBySection(int section) {
        newsRepository.deleteBySectionId(section);
    }
}

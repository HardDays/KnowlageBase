package ru.knowledgebase.dbmodule.dataservices.newsservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.knowledgebase.dbmodule.repositories.articlerepositories.ImageRepository;
import ru.knowledgebase.dbmodule.repositories.newsrepositories.NewsRepository;
import ru.knowledgebase.modelsmodule.articlemodels.News;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by root on 02.10.16.
 */

@Service("newsService")
public class NewsService {
    @Autowired
    private NewsRepository newsRepository;

    public News addNews(News news) throws Exception {
        return this.newsRepository.save(news);
    }

    public News updateNews(News news)throws Exception {
        return newsRepository.save(news);
    }

    public News findNews(int id) throws Exception {
        return this.newsRepository.findOne(id);
    }

    public void deleteNews(int id) throws Exception {
        this.newsRepository.delete(id);
    }

    public List<News> getAllNewsBySection(int sectionId) throws Exception {
        return this.newsRepository.findNewsBySection(sectionId);
    }

    public void deleteAllBySection(int section) throws Exception {
        newsRepository.deleteBySectionId(section);
    }

    public List<News> getSectionNewsFromDate(Integer i, Timestamp date) throws Exception {
        return newsRepository.getSectionNewsByDate(i, date);
    }
}

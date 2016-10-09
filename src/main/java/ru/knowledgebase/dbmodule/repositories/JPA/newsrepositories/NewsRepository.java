package ru.knowledgebase.dbmodule.repositories.JPA.newsrepositories;

import org.openxmlformats.schemas.drawingml.x2006.chart.CTRotX;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.modelsmodule.articlemodels.News;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by root on 02.10.16.
 */
public interface NewsRepository extends CrudRepository<News, Integer> {

    @Query("from News where sectionId = ?1")
    List<News> findNewsBySection(int sectionId);

    @Modifying
    @Transactional
    @Query("delete from News where sectionId = ?1")
    void deleteBySectionId(int sectionId);

    @Query("from News where sectionId=?1 and creationDate >= ?2")
    List<News> getSectionNewsByDate(Integer i, Timestamp date);
}

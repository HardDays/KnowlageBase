package ru.knowledgebase.dbmodule.repositories.archiverepositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.knowledgebase.modelsmodule.archivemodels.ArchiveArticle;

import java.util.List;

/**
 * Created by root on 17.09.16.
 */
public interface ArchiveArticleRepository extends CrudRepository<ArchiveArticle, Integer> {
    @Query("from ArchiveArticle where sectionId = ?1")
    List<ArchiveArticle> getSectionArchive(int sectionId);
}

package ru.knowledgebase.dbmodule.dataservices.archiveservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.dbmodule.repositories.archiverepositories.ArchiveArticleRepository;
import ru.knowledgebase.modelsmodule.archivemodels.ArchiveArticle;

import java.util.List;

/**
 * Created by root on 17.09.16.
 */
@Service("archiveArticleService")
public class ArchiveArticleService {
    @Autowired
    private ArchiveArticleRepository archiveArticleRepository;


    public ArchiveArticle create(ArchiveArticle archArticle) {
        return archiveArticleRepository.save(archArticle);
    }

    @Transactional
    public void createAll(List<ArchiveArticle> archiveArticles) {
        for (ArchiveArticle arch : archiveArticles) {
            archiveArticleRepository.save(arch);
        }
    }

    public void delete(int id) {
        archiveArticleRepository.delete(id);
    }

    public List<ArchiveArticle> getSectionArchive(int sectionId) {
        return archiveArticleRepository.getSectionArchive(sectionId);
    }

    public ArchiveArticle findById(int archiveArticleId) {
        return archiveArticleRepository.findOne(archiveArticleId);
    }
}

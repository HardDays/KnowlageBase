package ru.knowledgebase.dbmodule.dataservices;

import ru.knowledgebase.dbmodule.models.*;
import ru.knowledgebase.dbmodule.DAO.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("storageService")
public class MediaServiceImpl implements MediaService {

    @Autowired
    private MediaDAO dao;

    @Transactional
    public void save(Article a) {
        dao.save(a);
    }

    public void save(Users u) {dao.save(u); }

    public List<Article> getAll() {
        return dao.getAll();
    }
}
package ru.knowladgebase.dbmodule.dataservices;

import ru.knowladgebase.dbmodule.models.*;
import ru.knowladgebase.dbmodule.DAO.*;

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
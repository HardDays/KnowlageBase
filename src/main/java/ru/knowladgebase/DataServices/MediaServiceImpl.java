package ru.knowladgebase.DataServices;

import ru.knowladgebase.DAO.*;
import ru.knowladgebase.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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

    public void save(User u) {dao.save(u); }

    public List<Article> getAll() {
        return dao.getAll();
    }
}

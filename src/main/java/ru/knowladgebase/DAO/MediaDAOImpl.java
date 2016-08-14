package ru.knowladgebase.DAO;

import org.springframework.stereotype.Repository;

import ru.knowladgebase.models.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import ru.knowladgebase.models.*;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class MediaDAOImpl implements MediaDAO {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void save(Article a) {
        em.persist(a);
    }
    @Transactional
    public void save(User u) {
        em.persist(u);
    }

    public List<Article> getAll() {
        return em.createQuery("from Article", Article.class).getResultList();
    }
}


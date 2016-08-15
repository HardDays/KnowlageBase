package ru.knowladgebase.dbmodule.DAO; /**
 * Created by root on 10.08.16.
 */
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import ru.knowladgebase.dbmodule.models.*;

import javax.persistence.PersistenceContext;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@Repository
public class MediaDAOImpl implements MediaDAO{

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void save(Article a) {
        em.persist(a);
    }
    @Transactional
    public void save(Users u) {
        em.persist(u);
    }

    public List<Article> getAll() {
        return em.createQuery("from Article", Article.class).getResultList();
    }
}

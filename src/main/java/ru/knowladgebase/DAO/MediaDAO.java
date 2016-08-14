package ru.knowladgebase.DAO;

import org.springframework.stereotype.Repository;
import ru.knowladgebase.models.*;

import javax.swing.text.AbstractDocument;
import java.util.List;

/**
 * Created by root on 10.08.16.
 */

@Repository
public interface MediaDAO {
    public void save(Article a);
    public void save(User u);
    public List<Article> getAll();
}

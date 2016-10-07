package ru.knowledgebase.dbmodule.repositories.commentrepositories;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.knowledgebase.modelsmodule.commentmodels.Comment;
import ru.knowledgebase.modelsmodule.usermodels.User;

import java.util.List;

/**
 * Created by vova on 01.09.16.
 */
public interface CommentRepository extends CrudRepository<Comment, Integer> {

    @Query("from Comment")
    public List<Comment> getAll();

    @Query("from Comment as c where c.admin.id = ?1 ")
    public List<Comment> findByAdmin(int admin);

    @Query("from Comment as c where c.admin.id = ?1 ")
    public List<Comment> findByAdminOffset(int admin, Pageable p);

}

package ru.knowledgebase.dbmodule.dataservices.commentservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.dbmodule.repositories.commentrepositories.CommentRepository;
import ru.knowledgebase.modelsmodule.commentmodels.Comment;
import ru.knowledgebase.modelsmodule.usermodels.User;

import java.util.List;


/**
 * Created by vova on 01.09.16.
 */
@Service("commentService")
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Transactional
    public Comment create(Comment Comment) throws Exception{
        return commentRepository.save(Comment);
    }

    @Transactional
    public List<Comment> getAll() throws Exception{
        return commentRepository.getAll();
    }

    @Transactional
    public Comment find(int id) throws Exception{
        return commentRepository.findOne(id);
    }

    @Transactional
    public List<Comment> findByAdmin(User admin) throws Exception{
        return commentRepository.findByAdmin(admin);
    }

    @Transactional
    public void update(Comment Comment) throws Exception{
        commentRepository.save(Comment);
    }

    @Transactional
    public void delete(Comment Comment) throws Exception{
        commentRepository.delete(Comment);
    }

    @Transactional
    public void delete(int id) throws Exception{
        commentRepository.delete(id);
    }
}

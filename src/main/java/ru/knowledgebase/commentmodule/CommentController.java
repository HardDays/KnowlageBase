package ru.knowledgebase.commentmodule;

import ru.knowledgebase.dbmodule.DataCollector;
import ru.knowledgebase.exceptionmodule.articleexceptions.ArticleNotFoundException;
import ru.knowledgebase.exceptionmodule.commentexceptions.CommentNotFoundException;
import ru.knowledgebase.exceptionmodule.databaseexceptions.DataBaseException;
import ru.knowledgebase.exceptionmodule.userexceptions.UserNotFoundException;
import ru.knowledgebase.exceptionmodule.userexceptions.WrongUserDataException;
import ru.knowledgebase.modelsmodule.articlemodels.Article;
import ru.knowledgebase.modelsmodule.commentmodels.Comment;
import ru.knowledgebase.modelsmodule.usermodels.User;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by vova on 01.09.16.
 */
public class CommentController {
    private DataCollector collector = DataCollector.getInstance();

    private static volatile CommentController instance;

    /**
     * Get instance of a class
     * @return instance of a class
     */
    public static CommentController getInstance() {
        CommentController localInstance = instance;
        if (localInstance == null) {
            synchronized (CommentController.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new CommentController();
                }
            }
        }
        return localInstance;
    }
    /**
     * Add new comment object to database
     * @param comment comment
     */
    public Comment add(Comment comment) throws Exception{
        try{
            return collector.addComment(comment);
        }catch (Exception e){
            throw new DataBaseException();
        }
    }
    /**
     * Add new comment to database
     * @param userId id of user
     * @param articleId id of article
     * @param comment comment of user
     * @param articleText text with mistake
     */
    public LinkedList <Comment> add(int userId, int articleId, String comment, String articleText) throws Exception{
        if (userId == 0 || articleId == 0 || comment == null || articleText == null)
            throw new WrongUserDataException();
        User user = null;
        Article article = null;
        Article section = null;
        LinkedList <Comment> comments = new LinkedList<>();
        if (articleText.length() == 0 || comment.length() == 0)
            throw new WrongUserDataException();
        try{
            user = collector.findUser(userId);
            article = collector.findArticle(articleId);
            section = collector.findArticle(article.getSectionId());
        }catch (Exception e){
            e.printStackTrace();
            throw new DataBaseException();
        }
        if (user == null)
            throw new UserNotFoundException();
        if (article == null)
            throw new ArticleNotFoundException();
        try {
            List <User> admins =  collector.findMistakeViewers(section);
            for (int i = 0; i < admins.size(); i++) {
                Comment com = add(new Comment(user, admins.get(i), article, comment, articleText));
                comments.add(com);
            }
        }catch (Exception e){
            throw new DataBaseException();
        }
        return comments;
    }
    /**
     * Find list of comments sended to admin
     * @param adminId id of admin
     * @return list of comments
     */
    public List<Comment> findByAdmin(int adminId, int offset, int limit) throws Exception{
        if (adminId == 0 || limit == 0)
            throw new WrongUserDataException();
        try {
            return collector.findCommentsByAdmin(adminId, offset, limit);
        }catch (Exception e){
            throw new DataBaseException();
        }
    }
    /**
     * Check can admin delete comment
     * @param adminId id of admin
     * @param commentId id of comment
     * @return true or false
     */
    public boolean canDeleteComment(int adminId, int commentId) throws Exception{
        if (adminId == 0 || commentId == 0)
            throw new WrongUserDataException();
        Comment comment = null;
        try {
            comment = collector.findComment(commentId);
        }catch (Exception e){
            throw new DataBaseException();
        }
        if (comment == null)
            throw new CommentNotFoundException();
        return comment.getAdmin().getId() == adminId;
    }
    /**
     * Delete comment object from database
     * @param comment comment object
     */
    public void delete(Comment comment) throws Exception{
        if (comment == null)
            throw new CommentNotFoundException();
        try{
            collector.deleteComment(comment);
        }catch (Exception e){
            throw new DataBaseException();
        }
    }
    /**
     * Delete comment from database
     * @param id id of comment
     */
    public void delete(int id) throws Exception{
        if (id == 0)
            throw new WrongUserDataException();
        Comment comment = null;
        try{
            comment = collector.findComment(id);
        }catch (Exception e){
            throw new DataBaseException();
        }
        delete(comment);
    }

}

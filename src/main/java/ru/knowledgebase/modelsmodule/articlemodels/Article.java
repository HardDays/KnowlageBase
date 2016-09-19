package ru.knowledgebase.modelsmodule.articlemodels;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.ru.RussianLightStemFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Parameter;
import org.springframework.transaction.annotation.Transactional;
import ru.knowledgebase.modelsmodule.imagemodels.Image;
import ru.knowledgebase.modelsmodule.rolemodels.UserArticleRole;
import ru.knowledgebase.modelsmodule.usermodels.User;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by root on 10.08.16.
 */
@Entity
@Indexed
//@AnalyzerDefs({
//        @AnalyzerDef(name = "en",
//                tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
//                filters = {
//                        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
//                        @TokenFilterDef(factory = StopFilterFactory.class),
//                        @TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
//                            @Parameter(name = "language", value = "English")
//                        })
//                }),
//        @AnalyzerDef(name = "ru",
//                tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
//                filters = {
//                        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
//                        @TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
//                                @Parameter(name = "language", value = "Russian")
//                        })
//                })
//})
@AnalyzerDef(name = "customanalyzer",
        tokenizer =
                @TokenizerDef(factory = StandardTokenizerFactory.class),
        filters = {
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(factory = StopFilterFactory.class),
//                @TokenFilterDef(factory = SynonymFilterFactory.class, params = {
//                        @Parameter(name = "language", value = "Russian")
//                }),
//                @TokenFilterDef(factory = SynonymFilterFactory.class, params = {
//                        @Parameter(name = "language", value = "English")
//                }),
                @TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
                        @Parameter(name = "language", value = "Russian")
                }),
                @TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
                        @Parameter(name = "language", value = "English")
                })
        })
public class Article {

    @Id
    @SequenceGenerator(name="article_id_seq",
            sequenceName="article_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="article_id_seq")
    private int id;

    @Field
    @Analyzer(definition = "customanalyzer")
//    @AnalyzerDiscriminator(impl = LanguageDiscriminator.class)
    @Column(length = 10000)
    private String title;

    @Column(length=10000000)
    private String body;

    @ManyToOne
    private User author;

    @Field
    @Analyzer(definition = "customanalyzer")
//    @AnalyzerDiscriminator(impl = LanguageDiscriminator.class)
    @Column(length=10000000)
    private String clearBody;

    //*
    @ManyToOne
    private Article parentArticle;
    //*/

    @OneToMany(mappedBy="parentArticle", cascade = {CascadeType.ALL})
    private List<Article> children = new LinkedList<Article>();

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    private List<Image> images;

    @OneToMany(mappedBy = "article", cascade = {CascadeType.REMOVE})
    private List<UserArticleRole> userArticleRole;

    //BEGIN CONSTRUCTORS
    public Article(){
    }

    public Article(int id) {
        this.id = id;
    }

    public Article(String title, String body, String clearBody,
                   User author, Article parentArticle, List<Image> images) {
        this.title         = title;
        this.body          = body;
        this.clearBody     = clearBody;
        this.author        = author;
        this.parentArticle = parentArticle;
        if (images != null) {
            this.images = images;
        }
        else {
            this.images = new LinkedList<Image>();
        }
    }

    //END CONSTRUCTORS


    //BEGIN SG METHODS

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Transactional
    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getClearBody() {
        return clearBody;
    }

    public void setClearBody(String clearBody) {
        this.clearBody = clearBody;
    }

    public Article getParentArticle() {
        return parentArticle;
    }

    public void setParentArticle(Article parentArticle) {
        this.parentArticle = parentArticle;
    }

    public void addChild(Article article) {
        children.add(article);
    }

    public List<Article> getChildren() {
        return children;
    }
    //END SG METHODS


    //BEGIN SUPPORT METHODS
    public void copy(Article article) {
        if (article == null)
            return;
        this.author        = article.author;
        this.id            = article.id;
        this.title         = article.title;
        this.clearBody     = article.clearBody;
        this.body          = article.body;
        this.parentArticle = article.parentArticle;
        this.images        = article.images;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        boolean res= true;
        Article comp = (Article)other;
        res &= this.author.equals(comp.author);
        res &= this.id == comp.id;
        res &= this.title.equals(comp.title);
        res &= this.clearBody.equals(comp.clearBody);
        res &= this.body.equals(comp.body);
        if (this.images == comp.images && comp.images != null)
            res &= this.images.equals(comp.images);
        if (this.parentArticle == comp.parentArticle && comp.parentArticle != null)
            res &= this.parentArticle.getId() == comp.parentArticle.getId();
        return res;
    }

    //END SUPPORT METHODS

}

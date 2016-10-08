package ru.knowledgebase.analyticsmodule.rank;

/**
 * Created by vova on 07.09.16.
 */
public class ArticleRank extends Rank {

    private Integer id;

    public ArticleRank(int id, int rank){
        super(rank);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

package ru.knowledgebase.analyticsmodule.rank;

import ru.knowledgebase.analyticsmodule.rank.Rank;

/**
 * Created by vova on 07.09.16.
 */
public class RequestRank extends Rank {

    private String request;

    public RequestRank(String request, int rank){
        super(rank);
        this.request = request;
    }


    public String getRequest() {
        return request;
    }
}

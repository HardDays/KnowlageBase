package ru.knowledgebase.dbmodule.dataservices.chunkrequest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Created by vova on 07.10.16.
 */
public class ChunkRequest implements Pageable {

    private int limit = 0;
    private int offset = 0;

    public ChunkRequest(int offset, int limit) {
        if (offset < 0)
            throw new IllegalArgumentException("Skip must not be less than zero!");

        if (limit <= 0)
            throw new IllegalArgumentException("Offset must not be less than zero!");

        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public int getPageNumber() {
        return 0;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return null;
    }


    public Pageable next() {
        return null;
    }


    public Pageable previousOrFirst() {
        return this;
    }


    public Pageable first() {
        return this;
    }


    public boolean hasPrevious() {
        return false;
    }

}


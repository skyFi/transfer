package com.darcytech.transfer.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHits;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by darcy on 2015/11/30.
 */
@Component
@Scope("prototype")
public class EsScroller {

    private Client client;

    private SearchResponse response;

    private TimeValue timeValue;

    public EsScroller(Client client, SearchResponse response, TimeValue timeValue) {
        this.client = client;
        this.response = response;
        this.timeValue = timeValue;
    }

    public SearchHits next() throws IOException {
        response = client.prepareSearchScroll(response.getScrollId()).setScroll(timeValue).execute().actionGet();
        if (response.getFailedShards() != 0) {
            throw new IOException("Has shard failed:" + response.getFailedShards());
        }
        return response.getHits();
    }

}

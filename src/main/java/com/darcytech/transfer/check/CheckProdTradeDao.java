package com.darcytech.transfer.check;

import java.util.Date;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.darcytech.transfer.model.Trade;

/**
 * Created by darcy on 2015/12/24.
 */
@Repository
public class CheckProdTradeDao {

    @Autowired
    private Client esClient;

    public long count(Date startTime, Date endTime) {

        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.rangeQuery("lastModifyTime").gt(startTime.getTime()).lt(endTime.getTime()));

        SearchRequestBuilder requestBuilder = esClient.prepareSearch("hermes")
                .setTypes(Trade.class.getSimpleName())
                .setQuery(queryBuilder)
                .setSearchType(SearchType.COUNT);

        SearchResponse response = requestBuilder.execute().actionGet();
        return response.getHits().getTotalHits();
    }
}

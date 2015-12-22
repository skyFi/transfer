package com.darcytech.transfer.dao;

import java.util.Date;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.model.Refund;

/**
 * Created by darcy on 2015/12/22.
 */
@Component
public class ProdRefundDao {

    @Autowired
    private Client esClient;

    public EsScroller prepareScrollByDate(Date startTime, Date endTime) {

        TimeValue timeOut = new TimeValue(60 * 1000);

        FilterBuilder filterBuilder;

        if (startTime != null) {
            filterBuilder = FilterBuilders.boolFilter()
                    .must(FilterBuilders.rangeFilter("taobaoModified").gte(startTime.getTime()).lte(endTime.getTime()));
        } else {
            filterBuilder = FilterBuilders.boolFilter()
                    .must(FilterBuilders.rangeFilter("taobaoModified").lte(endTime.getTime()));
        }

        SearchRequestBuilder searchRequest = esClient.prepareSearch("hermes")
                .setTypes(Refund.class.getSimpleName())
                .setPostFilter(filterBuilder)
                .setSearchType(SearchType.SCAN)
                .setSize(50)
                .setScroll(timeOut);

        SearchResponse response = searchRequest.execute().actionGet();

        return new EsScroller(esClient, response, timeOut);
    }

}

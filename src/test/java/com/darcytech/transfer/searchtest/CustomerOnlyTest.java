package com.darcytech.transfer.searchtest;

import java.io.IOException;
import java.util.ArrayList;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.darcytech.transfer.TransferMain;
import com.darcytech.transfer.dao.EsScroller;
import com.darcytech.transfer.model.CustomerDetail;

/**
 * Created by darcy on 2015/12/11.
 */
@Configuration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TransferMain.class)
public class CustomerOnlyTest {

    private static final Logger logger = LoggerFactory.getLogger(CustomerOnlyTest.class);

    @Autowired
    private Client esClient;

    @Test
    public void countCustomer() {

        ArrayList<String> testUserIds = new ArrayList<>();
        testUserIds.add("52405347");
        testUserIds.add("844921681");

        for (String testUserId : testUserIds) {
            FilterBuilder filterBuilder = FilterBuilders.boolFilter()
                    .must(FilterBuilders.termFilter("userId", testUserId));

            SearchRequestBuilder requestBuilder = esClient.prepareSearch(testUserId)
                    .setTypes(CustomerDetail.class.getSimpleName())
                    .setPostFilter(filterBuilder)
                    .setSearchType(SearchType.COUNT);

            Long timer = System.currentTimeMillis();
            SearchResponse response = requestBuilder.execute().actionGet();
            logger.debug("count only CustomerDetail( " + testUserId +
                    " ). take: " + (System.currentTimeMillis() - timer) + "ms" +
                    ". count: " + response.getHits().getTotalHits()+ ".");

        }

    }

    @Test
    public void searchCustomerScroll() throws IOException {

        TimeValue timeOut = new TimeValue(60 * 1000);
        ArrayList<String> testUserIds = new ArrayList<>();
        testUserIds.add("52405347");
        testUserIds.add("844921681");

        for (String testUserId : testUserIds) {

            QueryBuilder nestQueryBuilder = QueryBuilders.boolQuery()
                    .must(QueryBuilders.termQuery("userId", testUserId))
                    .must(new NestedQueryBuilder("trade", FilterBuilders.notFilter(FilterBuilders.existsFilter("id"))));

            SearchRequestBuilder requestBuilder = esClient.prepareSearch(testUserId)
                    .setTypes(CustomerDetail.class.getSimpleName())
                    .setQuery(nestQueryBuilder)
                    .setSearchType(SearchType.SCAN)
                    .setSize(50)
                    .setScroll(timeOut);


            Long exeTime = System.currentTimeMillis();
            SearchResponse response = requestBuilder.execute().actionGet();
            logger.debug("execute scroll only customerDetail take: " + (System.currentTimeMillis() - exeTime));

            EsScroller esScroller = new EsScroller(esClient, response, timeOut);

            int count = 0;
            while (true) {
                Long scrollTime = System.currentTimeMillis();
                SearchHits searchHits = esScroller.next();
                if (searchHits.getHits().length == 0) {
                    break;
                }
                count = count + searchHits.getHits().length;
                logger.debug("1、scroll only customerDetail ( "+ testUserId + " )." +
                "take: " + (System.currentTimeMillis() - scrollTime) +
                "progress: " + count + " / " + searchHits.getTotalHits());

            }
        }
    }
}

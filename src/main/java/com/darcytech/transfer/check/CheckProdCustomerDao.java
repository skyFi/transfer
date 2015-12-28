package com.darcytech.transfer.check;

import java.io.IOException;
import java.util.Date;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.darcytech.transfer.dao.EsScroller;
import com.darcytech.transfer.dao.ProdCustomerDao;
import com.darcytech.transfer.enumeration.FailedDataType;
import com.darcytech.transfer.model.CustomerDetail;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by darcy on 2015/12/24.
 */
@Repository
public class CheckProdCustomerDao {

    private final Logger logger = LoggerFactory.getLogger(CheckProdCustomerDao.class);

    @Autowired
    private Client esClient;

    @Autowired
    private ProdCustomerDao prodCustomerDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CheckNewCustomerDao checkNewCustomerDao;

    @Autowired
    private FailedDataDao failedDataDao;

    public long count(Date startTime, Date endTime) {

        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.rangeQuery("lastModifyTime").gt(startTime.getTime()).lt(endTime.getTime()));

        SearchRequestBuilder requestBuilder = esClient.prepareSearch("hermes")
                .setTypes(CustomerDetail.class.getSimpleName())
                .setQuery(queryBuilder)
                .setSearchType(SearchType.COUNT);

        SearchResponse response = requestBuilder.execute().actionGet();
        return response.getHits().getTotalHits();
    }

    public void showMissedCustomer(Date startTime, Date endTime) throws IOException {

        EsScroller esScroller = prodCustomerDao.prepareScrollByDate(startTime, endTime);
        int count = 0;
        while (true) {
            SearchHits searchHits = esScroller.next();
            count = count + searchHits.getHits().length;
            if (searchHits.getHits().length == 0) {
                break;
            }
            for (SearchHit customerSearchHit : searchHits) {
                CustomerDetail customerDetail = objectMapper.readValue(customerSearchHit.getSourceAsString(), CustomerDetail.class);

                if (failedDataDao.getFailedData(customerDetail.getUserId(), customerDetail.getNick(), FailedDataType.CUSTOMER) == 0
                    && checkNewCustomerDao.getCustomer(customerDetail.getUserId(), customerDetail.getNick(), startTime, endTime) == 0) {
                    long countRepeat = countRepeatCustomer(customerDetail.getUserId(), customerDetail.getNick());
                    logger.debug("missing customer user_id: {}, nick: {}. repeat: {}",
                            customerDetail.getUserId(), customerDetail.getNick(), countRepeat);

                }
            }
            logger.debug("progress: {}/{}",
                    count, searchHits.getTotalHits());

        }
    }

    private long countRepeatCustomer(Long userId, String nick) {

        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("userId", userId))
                .must(QueryBuilders.termQuery("nick", nick));

        SearchRequestBuilder requestBuilder = esClient.prepareSearch("hermes")
                .setTypes(CustomerDetail.class.getSimpleName())
                .setQuery(queryBuilder)
                .setSearchType(SearchType.COUNT);

        SearchResponse response = requestBuilder.execute().actionGet();

        return response.getHits().getTotalHits();
    }

}

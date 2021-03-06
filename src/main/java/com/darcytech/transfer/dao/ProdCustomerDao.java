package com.darcytech.transfer.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.model.CustomerDetail;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by darcy on 2015/11/30.
 */
@Component
public class ProdCustomerDao {

    @Autowired
    private Client esClient;

    @Autowired
    private ObjectMapper objectMapper;

    private Set<String> existsUsers;

    public EsScroller prepareScrollByDate(Date startTime, Date endTime) {

        TimeValue timeOut = new TimeValue(60 * 1000);

        FilterBuilder filterBuilder;

        if (startTime != null) {
            filterBuilder = FilterBuilders.rangeFilter("lastModifyTime").gte(startTime.getTime()).lte(endTime.getTime());
        } else {
            filterBuilder = FilterBuilders.rangeFilter("lastModifyTime").lte(endTime.getTime());
        }

        SearchRequestBuilder searchRequest = esClient.prepareSearch("hermes")
                .setTypes(CustomerDetail.class.getSimpleName())
                .setPostFilter(filterBuilder)
                .setSearchType(SearchType.SCAN)
                .setSize(50)
                .setScroll(timeOut);

        SearchResponse response = searchRequest.execute().actionGet();

        return new EsScroller(esClient, response, timeOut);
    }

    public Map<String, Long> getUserDocCounts() throws IOException {
        Map<String, Long> userDocCounts = new HashMap<>();

        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("customerCount")
                .field("userId").size(5000);

        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch("hermes")
                .setTypes(CustomerDetail.class.getSimpleName())
                .addAggregation(aggregationBuilder)
                .setSearchType(SearchType.COUNT);

        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

        Terms terms = searchResponse.getAggregations().get("customerCount");
        for (Terms.Bucket bucket : terms.getBuckets()) {
            if (getExistsUsers().contains(bucket.getKey())) {
                userDocCounts.put(bucket.getKey(), bucket.getDocCount());
            }
        }

        return userDocCounts;
    }

    public Set<String> getExistsUsers() {
        if (existsUsers == null) {
            GetIndexResponse getIndexResponse = esClient.admin().indices().prepareGetIndex().execute().actionGet();
            existsUsers = new HashSet<>();
            for (AliasMetaData aliasMetaData : getIndexResponse.getAliases().get("hermes")) {
                existsUsers.add(aliasMetaData.alias());
            }
        }

        return existsUsers;
    }

    public List<CustomerDetail> getCustomerDetail(Long userId, String nick) throws IOException {

        List<CustomerDetail> customerDetails = new ArrayList<>();

        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("userId", userId))
                .must(QueryBuilders.termQuery("nick", nick));

        SearchRequestBuilder requestBuilder = esClient.prepareSearch("hermes")
                .setTypes(CustomerDetail.class.getSimpleName())
                .setQuery(queryBuilder);

        SearchResponse response = requestBuilder.execute().actionGet();

        for (SearchHit searchHit : response.getHits()) {
            CustomerDetail customerDetail = objectMapper.readValue(searchHit.getSourceAsString(), CustomerDetail.class);
            customerDetails.add(customerDetail);
        }

        return customerDetails;
    }

}

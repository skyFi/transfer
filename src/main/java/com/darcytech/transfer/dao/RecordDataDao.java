package com.darcytech.transfer.dao;

import java.util.Date;
import java.util.List;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.darcytech.transfer.enumeration.RecordTableName;

/**
 * Created by darcy on 2015/12/28.
 */
@Repository
public class RecordDataDao {

    @Autowired
    private Client esClient;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> getTransferredDays(RecordTableName tableName) {

        String sql = "select transfer_day from " + tableName.name();

        return jdbcTemplate.queryForList(sql, String.class);

    }

    public long count(Date startTime, Date endTime, String type) {

        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.rangeQuery("lastModifyTime").gt(startTime.getTime()).lt(endTime.getTime()));

        SearchRequestBuilder requestBuilder = esClient.prepareSearch("hermes")
                .setTypes(type)
                .setQuery(queryBuilder)
                .setSearchType(SearchType.COUNT);

        SearchResponse response = requestBuilder.execute().actionGet();
        return response.getHits().getTotalHits();
    }
}

package com.darcytech.transfer.dao;

import java.util.Date;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.darcytech.transfer.dto.CampaignResult;

/**
 * User: dixi
 * Time: 2015/12/17 15:16
 */
@Repository
public class CampaignResultDaoEs {

    @Autowired
    private Client client;

    public List<CampaignResult> searchScrollByLastModifyTime(Date endTimeLine) {
        BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.rangeQuery("lastModifyTime").to(endTimeLine.getTime()));

        SearchResponse scrollResp = client.prepareSearch(CampaignResult.class.getSimpleName())
                .setSearchType(SearchType.SCAN)
                .setScroll(new TimeValue(60000))
                .setQuery(booleanQueryBuilder)
                .setSize(100).execute().actionGet();

        return null;
    }
}

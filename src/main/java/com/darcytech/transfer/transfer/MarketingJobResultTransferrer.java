package com.darcytech.transfer.transfer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.dao.EsScroller;
import com.darcytech.transfer.dao.ProdMarketingJobResultDao;
import com.darcytech.transfer.dao.TransferEntityDao;
import com.darcytech.transfer.model.MarketingJobResult;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by darcy on 2015/12/19.
 */
@Component
public class MarketingJobResultTransferrer extends AbstractTransferrer{

    private final Logger logger = LoggerFactory.getLogger(MarketingJobResultTransferrer.class);

    @Autowired
    private ProdMarketingJobResultDao prodMarketingJobResultDao;

    @Autowired
    private TransferEntityDao transferEntityDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void bulkTransfer(SearchHits searchHits) throws Exception {

        List<MarketingJobResult> marketingJobResults = new ArrayList<>();
        for (SearchHit searchHit : searchHits) {
            MarketingJobResult marketingJobResult = objectMapper.readValue(searchHit.getSourceAsString(), MarketingJobResult.class);

            if (marketingJobResult.getMobile() == null && marketingJobResult.getBuyerNick() == null) {
                logger.error(searchHit.getSourceAsString());
            }
            if (marketingJobResult.getBuyerNick() == null) {
                marketingJobResult.setBuyerNick("");
            }
            if (marketingJobResult.getMobile() == null) {
                marketingJobResult.setMobile("");
            }
            marketingJobResults.add(marketingJobResult);

        }
        transferEntityDao.multiSave(marketingJobResults);
    }

    @Override
    protected EsScroller getScroller(Date start, Date end) {
        if (end != null) {
            return prodMarketingJobResultDao.prepareScrollByDate(start, end);
        } else {
            return prodMarketingJobResultDao.prepareScroll();
        }
    }

}

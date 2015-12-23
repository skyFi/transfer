package com.darcytech.transfer.transfer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.dao.EsScroller;
import com.darcytech.transfer.dao.ProdMarketingJobResultOrderDao;
import com.darcytech.transfer.dao.TransferEntityDao;
import com.darcytech.transfer.model.MarketingJobResultOrder;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by darcy on 2015/12/21.
 */
@Component
public class MarketingJobResultOrderTransferrer extends AbstractTransferrer {

    @Autowired
    private ProdMarketingJobResultOrderDao prodMarketingJobResultOrderDao;

    @Autowired
    private TransferEntityDao transferEntityDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void bulkTransfer(SearchHits searchHits) throws Exception {

        List<MarketingJobResultOrder> marketingJobResultOrders = new ArrayList<>();
        for (SearchHit searchHit : searchHits) {
            MarketingJobResultOrder marketingJobResultOrder = objectMapper.readValue(searchHit.getSourceAsString(), MarketingJobResultOrder.class);

            marketingJobResultOrders.add(marketingJobResultOrder);

        }
        transferEntityDao.multiSave(marketingJobResultOrders);
    }

    @Override
    protected EsScroller getScroller(Date start, Date end) {
        if (end != null) {
            return prodMarketingJobResultOrderDao.prepareScrollByDate(start, end);
        } else {
            return prodMarketingJobResultOrderDao.prepareScroll();
        }
    }
}

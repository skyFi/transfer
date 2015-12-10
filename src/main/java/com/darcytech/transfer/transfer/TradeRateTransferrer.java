package com.darcytech.transfer.transfer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.dao.EsScroller;
import com.darcytech.transfer.dao.NewTradeRateDao;
import com.darcytech.transfer.dao.ProdCustomerDao;
import com.darcytech.transfer.dao.ProdTradeRateDao;
import com.darcytech.transfer.model.TradeRate;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by darcy on 2015/12/3.
 */
@Component
public class TradeRateTransferrer extends AbstractTransferrer{

    @Autowired
    private ProdTradeRateDao prodTradeRateDao;

    @Autowired
    private NewTradeRateDao newTradeRateDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProdCustomerDao prodCustomerDao;

    @Override
    protected void bulkTransfer(SearchHits searchHits) throws IOException {
        List<TradeRate> tradeRates = new ArrayList<>();
        Set<String> existsUsers = prodCustomerDao.getExistsUsers();
        for (SearchHit customerSearchHit : searchHits) {
            TradeRate tradeRate = objectMapper.readValue(customerSearchHit.getSourceAsString(), TradeRate.class);
            if (existsUsers.contains(String.valueOf(tradeRate.getUserId()))) {
                tradeRate.setCustomerId(tradeRate.getUserId() + "_" + tradeRate.getNick());
                tradeRates.add(tradeRate);
            }
        }
        newTradeRateDao.bulkSave(tradeRates);
    }

    @Override
    protected EsScroller getScroller(Date start, Date end) {
        return prodTradeRateDao.prepareScrollByDate(start, end);
    }
}

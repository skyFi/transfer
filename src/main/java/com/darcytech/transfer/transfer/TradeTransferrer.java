package com.darcytech.transfer.transfer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.dao.EsScroller;
import com.darcytech.transfer.dao.NewTradeDao;
import com.darcytech.transfer.dao.ProdCustomerDao;
import com.darcytech.transfer.dao.ProdOrderDao;
import com.darcytech.transfer.dao.ProdTradeDao;
import com.darcytech.transfer.dao.TransferEntityDao;
import com.darcytech.transfer.enumeration.FailedDataType;
import com.darcytech.transfer.enumeration.FailedReason;
import com.darcytech.transfer.model.FailedData;
import com.darcytech.transfer.model.Order;
import com.darcytech.transfer.model.Trade;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by darcy on 2015/12/3.
 */
@Component
public class TradeTransferrer extends AbstractTransferrer {

    private static final Logger logger = LoggerFactory.getLogger(TradeTransferrer.class);

    @Autowired
    private ProdTradeDao prodTradeDao;

    @Autowired
    private NewTradeDao newTradeDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProdOrderDao prodOrderDao;

    @Autowired
    private ProdCustomerDao prodCustomerDao;

    @Autowired
    private TransferEntityDao transferEntityDao;

    @Override
    protected void bulkTransfer(SearchHits searchHits) throws IOException {
        List<Trade> trades = new ArrayList<>();
        Set<String> existsUsers = prodCustomerDao.getExistsUsers();
        for (SearchHit searchHit : searchHits) {
            Trade trade = objectMapper.readValue(searchHit.getSourceAsString(), Trade.class);
            if (existsUsers.contains(String.valueOf(trade.getUserId()))) {
                trades.add(trade);
            } else {
                FailedData failedData = new FailedData();
                failedData.setProdId(trade.getId());
                failedData.setUserId(trade.getUserId());
                failedData.setBuyerNick(trade.getNick());
                failedData.setType(FailedDataType.TRADE);
                failedData.setReason(FailedReason.EXPIRED);
                failedData.setTransferTime(trade.getLastModifyTime());
                transferEntityDao.persist(failedData);
            }
        }

        if (trades.isEmpty()) {
            return;
        }

        List<Order> orders = prodOrderDao.multiGetOrdersByTrades(trades);

        for (Order order : orders) {
            for (Trade trade : trades) {
                if (trade.getId().equals(order.getTradeId())) {
                    trade.addOrder(order);
                }
            }
        }

        newTradeDao.bulkSave(trades);
    }

    @Override
    protected EsScroller getScroller(Date start, Date end) {
        return prodTradeDao.prepareScrollByDate(start, end);
    }
}

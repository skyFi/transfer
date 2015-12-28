package com.darcytech.transfer.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.darcytech.transfer.TransferMain;
import com.darcytech.transfer.dao.EsScroller;
import com.darcytech.transfer.dao.ProdTradeDao;
import com.darcytech.transfer.model.Trade;
import com.fasterxml.jackson.databind.ObjectMapper;

import junit.framework.Assert;

/**
 * Created by darcy on 2015/12/2.
 */
@Configuration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TransferMain.class)
public class CheckProdTradeDaoTest {

    @Autowired
    private ProdTradeDao prodTradeDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testPrepareScrollByDate() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date start = simpleDateFormat.parse("2015-01-01");
        Date end = simpleDateFormat.parse("2015-02-01");

        EsScroller esScroller = prodTradeDao.prepareScrollByDate(start, end);
        SearchHits searchHits = esScroller.next();
        for (SearchHit searchHit : searchHits) {
            Trade trade = objectMapper.readValue(searchHit.getSourceAsString(), Trade.class);
            Assert.assertNotNull(trade.getId());
        }
        Assert.assertFalse(searchHits.getHits().length == 0);
    }
}

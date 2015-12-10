package com.darcytech.transfer.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.darcytech.transfer.TransferMain;
import com.darcytech.transfer.dao.EsScroller;
import com.darcytech.transfer.dao.ProdTradeRateDao;
import com.darcytech.transfer.model.TradeRate;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Created by darcy on 2015/12/3.
 */

@Configuration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TransferMain.class)
public class ProdTradeRateDaoTest {

    @Autowired
    private ProdTradeRateDao prodTradeRateDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testPrepareScrollByDate() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date start = simpleDateFormat.parse("2015-01-01");
        Date end = simpleDateFormat.parse("2015-01-02");

        EsScroller esScroller = prodTradeRateDao.prepareScrollByDate(start, end);
        SearchHits searchHits = esScroller.next();
        for (SearchHit searchHit : searchHits) {
            TradeRate tradeRate = objectMapper.readValue(searchHit.getSourceAsString(), TradeRate.class);
            Assert.assertNotNull(tradeRate.getId());
        }
        Assert.assertFalse(searchHits.getHits().length == 0);
    }
}

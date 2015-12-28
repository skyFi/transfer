package com.darcytech.transfer.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.darcytech.transfer.TransferMain;
import com.darcytech.transfer.dao.ProdOrderDao;
import com.darcytech.transfer.model.Order;
import com.darcytech.transfer.model.Trade;

/**
 * Created by darcy on 2015/12/2.
 */
@Configuration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TransferMain.class)
public class CheckProdOrderDaoTest {

    @Autowired
    private ProdOrderDao prodOrderDao;

    @Test
    public void testMultiGetOrdersByTrades() throws IOException {
        List<Trade> trades = new ArrayList<>();

        Trade trade = new Trade();
        trade.setId(867455820712658L);
        trade.setCustomerId("36074986");
        trade.setUserId(761679524L);

        Set<Long> oids = new HashSet<>();
        oids.add(867455820722658L);
        oids.add(867455820732658L);

        trade.setOids(oids);

        trades.add(trade);

        List<Order> orders = prodOrderDao.multiGetOrdersByTrades(trades);
        Assert.assertEquals(orders.size(), 2);
    }
}

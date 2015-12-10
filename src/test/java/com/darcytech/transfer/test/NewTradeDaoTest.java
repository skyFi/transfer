package com.darcytech.transfer.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.darcytech.transfer.TransferMain;
import com.darcytech.transfer.dao.NewTradeDao;
import com.darcytech.transfer.model.Trade;

/**
 * Created by darcy on 2015/12/2.
 */

@Configuration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TransferMain.class)
public class NewTradeDaoTest {

    @Autowired
    private NewTradeDao newTradeDao;

    @Test
    public void testBulkSave() throws IOException {

        List<Trade> tradeList = new ArrayList<>();

        Trade trade = new Trade();
        trade.setId(2L);
        trade.setUserId(2222l);
        trade.setCustomerId("2222_hello");
        trade.setBuyerRate(false);

        tradeList.add(trade);

        newTradeDao.bulkSave(tradeList);
    }
}

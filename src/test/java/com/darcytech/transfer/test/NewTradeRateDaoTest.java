package com.darcytech.transfer.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.darcytech.transfer.dao.NewTradeRateDao;
import com.darcytech.transfer.model.TradeRate;

/**
 * Created by darcy on 2015/12/3.
 */
public class NewTradeRateDaoTest extends BaseTest{

    @Autowired
    private NewTradeRateDao newTradeRateDao;

    @Test
    public void testBulkSave() throws IOException {
        TradeRate tradeRate = new TradeRate();
        List<TradeRate> tradeRates = new ArrayList<>();

        tradeRate.setId("2");
        tradeRate.setCustomerId("23458207_我的u盾2");
        tradeRate.setContent("NO.2");

        tradeRates.add(tradeRate);

        newTradeRateDao.bulkSave(tradeRates);

    }
}

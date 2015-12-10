package com.darcytech.transfer.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.darcytech.transfer.TransferMain;
import com.darcytech.transfer.transfer.TradeTransferrer;

/**
 * Created by darcy on 2015/12/3.
 */
@Configuration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TransferMain.class)
public class TradeTransferrerTest {

    @Autowired
    private TradeTransferrer tradeTransferrer;

    @Test
    public void testBulkTransfer() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start = simpleDateFormat.parse("2015-01-05");
        tradeTransferrer.transferByDay(start);
    }
}

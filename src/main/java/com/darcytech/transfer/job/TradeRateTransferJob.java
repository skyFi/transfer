package com.darcytech.transfer.job;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.transfer.TradeRateTransferrer;

/**
 * Created by darcy on 2015/12/3.
 */
@Component
public class TradeRateTransferJob extends AbstractTransferJob{

    @Value("${transfer.trade.rate.worker.count}")
    private int transferTradeRateWorkerCount;

    @Autowired
    private TradeRateTransferrer tradeRateTransferrer;

    @Override
    protected void saveTransferRecord(String transferDay) throws Exception {

    }

    @Override
    protected void transferByDay(Date day) throws Exception {
        if (day != null) {
            tradeRateTransferrer.transferByDay(day);
        }
    }

    @Override
    protected BlockingQueue<Integer> prepareTokenQueue() {
        BlockingQueue<Integer> tokens = new ArrayBlockingQueue<>(transferTradeRateWorkerCount);
        for (int i = 0; i < transferTradeRateWorkerCount; i++) {
            tokens.add(i);
        }
        return tokens;
    }

    @Override
    protected List<String> getTransferDays() {
        return null;
    }
}

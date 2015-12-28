package com.darcytech.transfer.job;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.transfer.TradeTransferrer;

/**
 * Created by darcy on 2015/12/3.
 */
@Component
public class TradeTransferJob extends AbstractTransferJob{

    @Value("${transfer.trade.worker.count}")
    private int transferTradeWorkerCount;

    @Autowired
    private TradeTransferrer tradeTransferrer;

    @Override
    protected void saveTransferRecord(String transferDay) throws Exception {

    }

    @Override
    protected void transferByDay(Date day) throws Exception {
        if (day != null) {
            tradeTransferrer.transferByDay(day);
        }
    }

    @Override
    protected BlockingQueue<Integer> prepareTokenQueue() {
        BlockingQueue<Integer> tokens = new ArrayBlockingQueue<>(transferTradeWorkerCount);
        for (int i = 0; i < transferTradeWorkerCount; i++) {
            tokens.add(i);
        }
        return tokens;
    }

    @Override
    protected List<String> getTransferDays() {
        return null;
    }
}

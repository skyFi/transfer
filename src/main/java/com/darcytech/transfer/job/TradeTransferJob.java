package com.darcytech.transfer.job;

import java.io.File;
import java.io.IOException;
import java.util.Date;
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
    protected File getRecordFile() throws IOException {
        return new File("transferred-trade.rcd");
    }

    @Override
    protected void transferByDay(Date day) throws IOException {
        tradeTransferrer.transferByDay(day);
    }

    @Override
    protected BlockingQueue<Integer> prepareTokenQueue() {
        BlockingQueue<Integer> tokens = new ArrayBlockingQueue<>(transferTradeWorkerCount);
        for (int i = 0; i < transferTradeWorkerCount; i++) {
            tokens.add(i);
        }
        return tokens;
    }
}

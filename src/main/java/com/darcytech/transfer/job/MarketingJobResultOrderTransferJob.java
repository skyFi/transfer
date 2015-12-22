package com.darcytech.transfer.job;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.transfer.MarketingJobResultOrderTransferrer;

/**
 * Created by darcy on 2015/12/21.
 */
@Component
public class MarketingJobResultOrderTransferJob extends AbstractTransferJob{


    @Value("${transfer.marketingjob.result.order.worker.count}")
    private int transferMarketingJobResultOrderWorkerCount;

    @Autowired
    private MarketingJobResultOrderTransferrer marketingJobResultOrderTransferrer;

    @Override
    protected File getRecordFile() throws IOException {
        return new File("marketingjob-result-order.rcd");
    }

    @Override
    protected void transferByDay(Date day) throws Exception {
        if (day != null) {
            marketingJobResultOrderTransferrer.transferByDay(day);
        } else {
            marketingJobResultOrderTransferrer.transfer();
        }
    }

    @Override
    protected BlockingQueue<Integer> prepareTokenQueue() {
        BlockingQueue<Integer> tokens = new ArrayBlockingQueue<>(transferMarketingJobResultOrderWorkerCount);
        for (int i = 0; i < transferMarketingJobResultOrderWorkerCount; i++) {
            tokens.add(i);
        }
        return tokens;
    }
}

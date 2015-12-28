package com.darcytech.transfer.job;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.transfer.MarketingJobResultTransferrer;

/**
 * Created by darcy on 2015/12/19.
 */
@Component
public class MarketingJobResultTransferJob extends AbstractTransferJob{

    @Value("${transfer.marketingjob.result.worker.count}")
    private int transferMarketingJobResultWorkerCount;

    @Autowired
    private MarketingJobResultTransferrer marketingJobResultTransferrer;

    @Override
    protected void saveTransferRecord(String transferDay) throws Exception {

    }

    @Override
    protected void transferByDay(Date day) throws Exception {
        if (day != null) {
            marketingJobResultTransferrer.transferByDay(day);
        } else {
            marketingJobResultTransferrer.transfer();
        }
    }

    @Override
    protected BlockingQueue<Integer> prepareTokenQueue() {
        BlockingQueue<Integer> tokens = new ArrayBlockingQueue<>(transferMarketingJobResultWorkerCount);
        for (int i = 0; i < transferMarketingJobResultWorkerCount; i++) {
            tokens.add(i);
        }
        return tokens;
    }

    @Override
    protected List<String> getTransferDays() {
        return null;
    }
}

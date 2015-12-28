package com.darcytech.transfer.job;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.transfer.RefundTransferrer;

/**
 * Created by darcy on 2015/12/22.
 */
@Component
public class RefundTransferJob extends AbstractTransferJob{

    @Value("${transfer.refund.count}")
    private int transferRefundCount;

    @Autowired
    private RefundTransferrer refundTransferrer;

    @Override
    protected void saveTransferRecord(String transferDay) throws Exception {

    }

    @Override
    protected void transferByDay(Date day) throws Exception {
        if (day != null) {
            refundTransferrer.transferByDay(day);
        }
    }

    @Override
    protected BlockingQueue<Integer> prepareTokenQueue() {
        BlockingQueue<Integer> tokens = new ArrayBlockingQueue<>(transferRefundCount);
        for (int i = 0; i < transferRefundCount; i++) {
            tokens.add(i);
        }
        return tokens;
    }

    @Override
    protected List<String> getTransferDays() {
        return null;
    }
}

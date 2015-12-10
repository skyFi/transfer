package com.darcytech.transfer.job;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.transfer.CustomerTransferrer;

/**
 * Created by darcy on 2015/12/2.
 */
@Component
public class CustomerTransferJob extends AbstractTransferJob{

    @Autowired
    private CustomerTransferrer customerTransferrer;

    @Value("${transfer.customer.worker.count}")
    private int transferCustomerWorkerCount;

    @Override
    protected File getRecordFile() throws IOException {
        return new File("transferred-customer.rcd");
    }

    @Override
    protected void transferByDay(Date day) throws IOException {
        customerTransferrer.transferByDay(day);
    }

    @Override
    protected BlockingQueue<Integer> prepareTokenQueue() {
        BlockingQueue<Integer> tokens = new ArrayBlockingQueue<>(transferCustomerWorkerCount);
        for (int i = 0; i < transferCustomerWorkerCount; i++) {
            tokens.add(i);
        }
        return tokens;
    }

}

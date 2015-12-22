package com.darcytech.transfer.job;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.transfer.ActionRecordTransferrer;

/**
 * Created by darcy on 2015/12/21.
 */
@Component
public class ActionRecordTransferJob extends AbstractTransferJob{

    @Value("${transfer.action.record.count}")
    private int transferActionRecordCount;

    @Autowired
    private ActionRecordTransferrer actionRecordTransferrer;

    @Override
    protected File getRecordFile() throws IOException {
        return new File("action-record.rcd");
    }

    @Override
    protected void transferByDay(Date day) throws Exception {
        if (day != null) {
            actionRecordTransferrer.transferByDay(day);
        } else {
            actionRecordTransferrer.transfer();
        }
    }

    @Override
    protected BlockingQueue<Integer> prepareTokenQueue() {
        BlockingQueue<Integer> tokens = new ArrayBlockingQueue<>(transferActionRecordCount);
        for (int i = 0; i < transferActionRecordCount; i++) {
            tokens.add(i);
        }
        return tokens;
    }
}

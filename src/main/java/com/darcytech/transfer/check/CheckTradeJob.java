package com.darcytech.transfer.check;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.enumeration.FailedDataType;

/**
 * Created by darcy on 2015/12/25.
 */
@Component
public class CheckTradeJob extends AbstractCheckJob {

    @Autowired
    private CheckProdTradeDao checkProdTradeDao;

    @Autowired
    private CheckNewTradeDao checkNewTradeDao;

    @Autowired
    private FailedDataDao failedDataDao;

    @Override
    protected long getNewCount(Date start, Date end) throws IOException {
        return checkNewTradeDao.count( start, end);
    }

    @Override
    protected long getProdCount(Date start, Date end) {
        return checkProdTradeDao.count(start, end);
    }

    @Override
    protected long getFailedCount(Date start, Date end) {
        return failedDataDao.count(FailedDataType.TRADE, start, end);
    }
}

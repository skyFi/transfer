package com.darcytech.transfer.check;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darcytech.transfer.enumeration.FailedDataType;

/**
 * Created by darcy on 2015/12/24.
 */
@Component
public class CheckCustomerJob extends AbstractCheckJob{

    @Autowired
    private CheckProdCustomerDao checkProdCustomerDao;

    @Autowired
    private CheckNewCustomerDao checkNewCustomerDao;

    @Autowired
    private FailedDataDao failedDataDao;

    @Override
    protected long getNewCount(Date start, Date end) throws IOException {
        return checkNewCustomerDao.count(start, end);
    }

    @Override
    protected long getProdCount(Date start, Date end) {
        return checkProdCustomerDao.count(start, end);
    }

    @Override
    protected long getFailedCount(Date start, Date end) {
        return failedDataDao.count(FailedDataType.CUSTOMER, start, end);
    }
}

package com.darcytech.transfer.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.darcytech.transfer.BaseTest;
import com.darcytech.transfer.check.FailedDataDao;
import com.darcytech.transfer.enumeration.FailedDataType;

/**
 * Created by darcy on 2015/12/25.
 */
public class FailedDataDaoTest extends BaseTest {

    @Autowired
    private FailedDataDao failedDataDao;

    @Test
    public void testCount() throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date start = simpleDateFormat.parse("2015-01-17");
        Date end = simpleDateFormat.parse("2015-02-05");

        Long count = failedDataDao.count(FailedDataType.CUSTOMER, start, end);

        Assert.assertNotNull(count);
    }

    @Test
    public void testGetFailedData() {

        Long count = failedDataDao.getFailedData(1756929907L, "cjx5226", FailedDataType.CUSTOMER);

        Assert.assertNotNull(count);
    }
}

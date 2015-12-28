package com.darcytech.transfer.dao;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.darcytech.transfer.BaseTest;
import com.darcytech.transfer.check.CheckProdCustomerDao;

/**
 * Created by darcy on 2015/12/26.
 */
public class CheckProdCustomerDaoTest extends BaseTest {

    @Autowired
    private CheckProdCustomerDao checkProdCustomerDao;

    @Test
    public void testGetCustomerDetailList() throws ParseException, IOException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date start = simpleDateFormat.parse("2015-03-19");
        Date end = simpleDateFormat.parse("2015-03-20");

        checkProdCustomerDao.showMissedCustomer(start, end);
    }

}

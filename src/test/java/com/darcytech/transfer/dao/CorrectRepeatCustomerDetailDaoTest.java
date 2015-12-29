package com.darcytech.transfer.dao;

import java.io.IOException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.darcytech.transfer.BaseTest;

/**
 * Created by darcy on 2015/12/28.
 */
public class CorrectRepeatCustomerDetailDaoTest extends BaseTest {

    @Autowired
    private CorrectRepeatCustomerDetailDao correctRepeatCustomerDetailDao;

    @Test
    public void testCorrectRepeatData() throws IOException {
         correctRepeatCustomerDetailDao.correctRepeatData();
    }

}

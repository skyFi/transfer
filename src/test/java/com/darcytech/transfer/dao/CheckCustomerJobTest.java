package com.darcytech.transfer.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.darcytech.transfer.CheckBaseTest;
import com.darcytech.transfer.check.CheckCustomerJob;

/**
 * Created by darcy on 2015/12/24.
 */
public class CheckCustomerJobTest extends CheckBaseTest {

    @Autowired
    private CheckCustomerJob checkCustomerJob;

    @Test
    public void testGetRandomTime() {

        checkCustomerJob.test();

    }
}

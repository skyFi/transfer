package com.darcytech.transfer.check;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.darcytech.transfer.BaseTest;

public class AbstractCheckJobTest extends BaseTest {

    @Autowired
    private CheckCustomerJob checkCustomerJob;

    @Test
    public void testTest1() throws Exception {
        checkCustomerJob.test();
    }
}
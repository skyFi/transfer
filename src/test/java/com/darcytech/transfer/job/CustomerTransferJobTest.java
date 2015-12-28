package com.darcytech.transfer.job;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.darcytech.transfer.BaseTest;

/**
 * Created by darcy on 2015/12/28.
 */
public class CustomerTransferJobTest extends BaseTest{

    @Autowired
    private CustomerTransferJob customerTransferJob;

    @Test
    public void testDoTransfer() throws Exception {
        customerTransferJob.doTransfer();
    }

}

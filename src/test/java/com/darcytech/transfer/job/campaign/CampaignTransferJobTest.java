package com.darcytech.transfer.job.campaign;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.darcytech.transfer.BaseTest;

/**
 * User: dixi
 * Time: 2015/12/19 10:30
 */
public class CampaignTransferJobTest extends BaseTest {

    @Autowired
    private CampaignTransferJob transferJob;

    @Test
    public void testDoJob() throws Exception {
        transferJob.doJob();
    }
}
package com.darcytech.transfer.job.campaigninstance;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.darcytech.transfer.BaseTest;

import static org.junit.Assert.*;

/**
 * User: dixi
 * Time: 2015/12/19 10:30
 */
public class CampaignInstanceNewTransferJobTest extends BaseTest {

    @Autowired
    private CampaignInstanceNewTransferJob transferJob;

    @Test
    public void testDoJob() throws Exception {
        transferJob.doJob();
    }
}
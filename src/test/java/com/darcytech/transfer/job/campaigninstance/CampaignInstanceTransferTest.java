package com.darcytech.transfer.job.campaigninstance;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.darcytech.transfer.BaseTest;

/**
 * User: dixi
 * Time: 2015/12/18 16:14
 */
public class CampaignInstanceTransferTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(CampaignInstanceTransferTest.class);

    @Autowired
    private CampaignInstanceTransfer instanceTransfer;

    private String pattern = "yyyy-MM-dd HH:mm:ss";
    private Date endTimeLine = DateTime.parse("2015-12-01 14:03:50", DateTimeFormat.forPattern(pattern)).toDate();

    @Test
    public void testTransferAndSave() throws Exception {
        instanceTransfer.transferAndSave(endTimeLine);
        logger.info("success");
    }
}
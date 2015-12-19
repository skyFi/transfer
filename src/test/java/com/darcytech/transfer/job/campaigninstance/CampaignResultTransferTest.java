package com.darcytech.transfer.job.campaigninstance;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.darcytech.transfer.BaseTest;

/**
 * User: dixi
 * Time: 2015/12/19 14:08
 */
public class CampaignResultTransferTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(CampaignResultTransferTest.class);

    @Autowired
    private CampaignResultTransfer resultTransfer;

    private DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    @Value("${transfer.campaign.opeateStartTimeLine}")
    private String startLine;

    @Value("${transfer.campaign.operateEndTimeLine}")
    private String endLine;

    @Test
    public void testTransferAndSave() throws Exception {
        Date startTime = StringUtils.isEmpty(startLine) ? null : DateTime.parse(startLine, formatter).toDate();
        Date endTime = StringUtils.isEmpty(endLine) ? null : DateTime.parse(endLine, formatter).toDate();

        resultTransfer.transferAndSave(startTime, endTime);
        logger.info("success");
    }

    @Test
    public void test() throws Exception{

    }
}
package com.darcytech.transfer.job.campaigninstance;

import java.util.Date;
import java.util.List;

import org.elasticsearch.common.collect.Lists;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: dixi
 * Time: 2015/12/17 14:43
 */
@Component
public class CampaignInstanceNewTransferJob {
    private static final Logger logger = LoggerFactory.getLogger(CampaignInstanceNewTransferJob.class);

    private String pattern = "yyyy-MM-dd HH:mm:ss";

    private Date operateEndTimeLine = DateTime.parse("2015-12-01 14:03:50", DateTimeFormat.forPattern(pattern)).toDate();

    @Autowired
    private List<CampaignInstanceNewTransfer> instanceNewTransfers = Lists.newArrayList();

    @Transactional 
    public void doJob() {
        try {
            // TODO: 2015/12/18 save campaianInstanceNew 是否会回滚  
            for (CampaignInstanceNewTransfer newTransfer : instanceNewTransfers) {
                newTransfer.transferAndSave(operateEndTimeLine);
            }
        } catch (Exception e) {
            logger.error("CampaignInstanceNewTransfer error ,msg = {}", e);
        }
    }
}

package com.darcytech.transfer.job.campaigninstance;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.collect.Lists;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: dixi
 * Time: 2015/12/17 14:43
 */
@Component
public class CampaignInstanceNewTransferJob {
    private static final Logger logger = LoggerFactory.getLogger(CampaignInstanceNewTransferJob.class);

    private DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    @Value("${transfer.campaign.opeateStartTimeLine}")
    private String startLine;

    @Value("${transfer.campaign.operateEndTimeLine}")
    private String endLine;

    @Autowired
    private List<CampaignInstanceNewTransfer> instanceNewTransfers = Lists.newArrayList();

    @Transactional
    public void doJob() {
        try {
            Date startTime = StringUtils.isEmpty(startLine) ? null : DateTime.parse(startLine, formatter).toDate();
            Date endTime = StringUtils.isEmpty(endLine) ? null : DateTime.parse(endLine, formatter).toDate();
            // TODO: 2015/12/18 save campaianInstanceNew 是否会回滚  
            for (CampaignInstanceNewTransfer newTransfer : instanceNewTransfers) {
                newTransfer.transferAndSave(startTime, endTime);
            }
        } catch (Exception e) {
            logger.error("CampaignInstanceNewTransfer error ,msg = {}", e);
        }
    }
}

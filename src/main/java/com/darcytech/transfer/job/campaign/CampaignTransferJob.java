package com.darcytech.transfer.job.campaign;

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

/**
 * User: dixi
 * Time: 2015/12/17 14:43
 */
@Component
public class CampaignTransferJob {
    private static final Logger logger = LoggerFactory.getLogger(CampaignTransferJob.class);

    private DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    @Value("${transfer.campaign.opeateStartTimeLine}")
    private String startLine;

    @Value("${transfer.campaign.operateEndTimeLine}")
    private String endLine;

    @Autowired
    private List<CampaignTransfer> instanceNewTransfers = Lists.newArrayList();

    public void doJob() {
        try {
            Date startTime = StringUtils.isEmpty(startLine) ? null : DateTime.parse(startLine, formatter).toDate();
            Date endTime = StringUtils.isEmpty(endLine) ? null : DateTime.parse(endLine, formatter).toDate();

            for (CampaignTransfer newTransfer : instanceNewTransfers) {
                try {
                    newTransfer.transferAndSave(startTime, endTime);
                } catch (Exception e) {
                    logger.error("campaign transfer :{} error msg = {}", newTransfer.getClass().getSimpleName(), e);
                }
            }
        } catch (Exception e) {
            logger.error("CampaignTransfer error ,msg = {}", e);
        }
    }
}

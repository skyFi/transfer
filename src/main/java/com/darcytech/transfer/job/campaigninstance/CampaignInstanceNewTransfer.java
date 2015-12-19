package com.darcytech.transfer.job.campaigninstance;

import java.util.Date;

/**
 * User: dixi
 * Time: 2015/12/17 18:21
 */
public interface CampaignInstanceNewTransfer {

    void transferAndSave(Date startTime, Date endTime);
}

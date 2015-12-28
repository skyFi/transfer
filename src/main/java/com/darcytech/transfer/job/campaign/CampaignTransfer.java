package com.darcytech.transfer.job.campaign;

import java.util.Date;

/**
 * User: dixi
 * Time: 2015/12/17 18:21
 */
public interface CampaignTransfer {

    void transferAndSave(Date startTime, Date endTime) throws Exception;
}

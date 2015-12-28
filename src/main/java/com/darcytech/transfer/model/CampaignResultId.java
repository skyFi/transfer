package com.darcytech.transfer.model;

import java.io.Serializable;

/**
 * User: dixi
 * Time: 2015/12/26 15:33
 */
public class CampaignResultId implements Serializable {

    private Long campaignId;

    private Long triggerTradeId;

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public Long getTriggerTradeId() {
        return triggerTradeId;
    }

    public void setTriggerTradeId(Long triggerTradeId) {
        this.triggerTradeId = triggerTradeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        CampaignResultId that = (CampaignResultId) o;

        if (campaignId != null ? !campaignId.equals(that.campaignId) : that.campaignId != null)
            return false;
        return !(triggerTradeId != null ? !triggerTradeId.equals(that.triggerTradeId) : that.triggerTradeId != null);

    }

    @Override
    public int hashCode() {
        int result = campaignId != null ? campaignId.hashCode() : 0;
        result = 31 * result + (triggerTradeId != null ? triggerTradeId.hashCode() : 0);
        return result;
    }
}

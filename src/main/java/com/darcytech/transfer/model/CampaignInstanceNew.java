package com.darcytech.transfer.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.darcytech.transfer.enumeration.InstanceStatus;

/**
 * User: dixi
 * Time: 2015/12/16 10:42
 */
@Entity
public class CampaignInstanceNew extends BaseModel {

    private Long userId;

    private Long campaignId;

    private Long campaignTypeId;

    private Long triggerTradeId;

    private String buyerNick;

    private String mobile;

    private Integer currentStep;

    private Date createDate;

    private Date startDate;

    private Date endDate;
    /**
     * step累计交易
     */
    @Type(type = "JSONString", parameters = {
            @Parameter(name = "targetClass", value = "java.lang.Long"),
            @Parameter(name = "isArray", value = "true")})
    private List<Long> totalTrades = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private InstanceStatus status;

    private Date lastStepTriggerTime;

    private Long oldInstanceId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public Long getCampaignTypeId() {
        return campaignTypeId;
    }

    public void setCampaignTypeId(Long campaignTypeId) {
        this.campaignTypeId = campaignTypeId;
    }

    public Long getTriggerTradeId() {
        return triggerTradeId;
    }

    public void setTriggerTradeId(Long triggerTradeId) {
        this.triggerTradeId = triggerTradeId;
    }

    public String getBuyerNick() {
        return buyerNick;
    }

    public void setBuyerNick(String buyerNick) {
        this.buyerNick = buyerNick;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(Integer currentStep) {
        this.currentStep = currentStep;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<Long> getTotalTrades() {
        return totalTrades;
    }

    public void setTotalTrades(List<Long> totalTrades) {
        this.totalTrades = totalTrades;
    }

    public InstanceStatus getStatus() {
        return status;
    }

    public void setStatus(InstanceStatus status) {
        this.status = status;
    }

    public Date getLastStepTriggerTime() {
        return lastStepTriggerTime;
    }

    public void setLastStepTriggerTime(Date lastStepTriggerTime) {
        this.lastStepTriggerTime = lastStepTriggerTime;
    }

    public Long getOldInstanceId() {
        return oldInstanceId;
    }

    public void setOldInstanceId(Long oldInstanceId) {
        this.oldInstanceId = oldInstanceId;
    }
}

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
 * Time: 2015/12/17 17:00
 */
@Entity
public class CampaignInstance extends BaseModel {

    private Integer currentStep;

    private Date startDate;

    private Date endDate;

    private Long campaignId;

    private Long campaignTypeId;

    private Long customerId;

    private String mobile;

    private Long userId;

    private Date createDate;

    private String resultId;
    /**
     * step累计交易
     */
    @Type(type = "JSONString", parameters = {
            @Parameter(name = "targetClass", value = "java.lang.String"),
            @Parameter(name = "isArray", value = "true")})
    private List<String> totalTrades = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private InstanceStatus status;

    private Date lastStepTriggerTime;

    public Integer getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(Integer currentStep) {
        this.currentStep = currentStep;
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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public List<String> getTotalTrades() {
        return totalTrades;
    }

    public void setTotalTrades(List<String> totalTrades) {
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
}

package com.darcytech.transfer.model;

import java.math.BigDecimal;
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

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 活动id
     */
    private Long campaignId;

    /**
     * 活动类型
     */
    private Long campaignTypeId;

    /**
     * 客户昵称
     */
    private String buyerNick;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 客户真实姓名
     */
    private String buyerRealName;

    /**
     * 当前应执行步骤
     */
    private int currentStep;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date endDate;

    /**
     * 总交易量
     */
    private int tradeNum;

    /**
     * 是否营销过：至少执行过1个成功动作的买家
     */
    private Boolean contacted;
    /**
     * 营销任务是否成功
     */
    private Boolean succeed;

    /**
     * 总交易金额
     */
    private BigDecimal totalPayment;

    /**
     * 上一步触发时间
     */
    private Date lastStepTriggerTime;

    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    private InstanceStatus status;

    @Type(type = "JSONString", parameters = {
            @Parameter(name = "targetClass", value = "java.lang.String"),
            @Parameter(name = "isArray", value = "true")})
    private List<CampaignStepInstance> stepInstances = new ArrayList<>();

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

    public String getBuyerRealName() {
        return buyerRealName;
    }

    public void setBuyerRealName(String buyerRealName) {
        this.buyerRealName = buyerRealName;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
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

    public int getTradeNum() {
        return tradeNum;
    }

    public void setTradeNum(int tradeNum) {
        this.tradeNum = tradeNum;
    }

    public Boolean getContacted() {
        return contacted;
    }

    public void setContacted(Boolean contacted) {
        this.contacted = contacted;
    }

    public Boolean getSucceed() {
        return succeed;
    }

    public void setSucceed(Boolean succeed) {
        this.succeed = succeed;
    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
    }

    public Date getLastStepTriggerTime() {
        return lastStepTriggerTime;
    }

    public void setLastStepTriggerTime(Date lastStepTriggerTime) {
        this.lastStepTriggerTime = lastStepTriggerTime;
    }

    public InstanceStatus getStatus() {
        return status;
    }

    public void setStatus(InstanceStatus status) {
        this.status = status;
    }

    public List<CampaignStepInstance> getStepInstances() {
        return stepInstances;
    }

    public void setStepInstances(List<CampaignStepInstance> stepInstances) {
        this.stepInstances = stepInstances;
    }
}

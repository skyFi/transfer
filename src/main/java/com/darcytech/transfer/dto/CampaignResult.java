package com.darcytech.transfer.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CampaignResult {

    protected String id;

    private Long userId;

    private Long customerId;

    private Long campaignId;

    private Long campaignTypeId;

    private String buyerNick;

    private String buyerPhone;

    private String buyerRealName;

    /**
     * 营销累计交易数
     */
    private Integer tradeNum = 0;

    /**
     * 营销累计交易
     */
    private Set<String> totalTrades = new HashSet<>();

    /**
     * 营销累计金额
     */
    private BigDecimal totalPayment = BigDecimal.ZERO;
    /**
     * 执行时间
     */
    private Date campaignTime;
    /**
     * 是否营销过：至少执行过1个成功动作的买家
     */
    private Boolean contacted;
    /**
     * 成功营销
     */
    private Boolean success;

    private List<StepResult> stepResultList = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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

    public String getBuyerPhone() {
        return buyerPhone;
    }

    public void setBuyerPhone(String buyerPhone) {
        this.buyerPhone = buyerPhone;
    }

    public String getBuyerRealName() {
        return buyerRealName;
    }

    public void setBuyerRealName(String buyerRealName) {
        this.buyerRealName = buyerRealName;
    }

    public Integer getTradeNum() {
        return tradeNum;
    }

    public void setTradeNum(Integer tradeNum) {
        this.tradeNum = tradeNum;
    }

    public Set<String> getTotalTrades() {
        return totalTrades;
    }

    public void setTotalTrades(Set<String> totalTrades) {
        this.totalTrades = totalTrades;
    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
    }

    public Date getCampaignTime() {
        return campaignTime;
    }

    public void setCampaignTime(Date campaignTime) {
        this.campaignTime = campaignTime;
    }

    public Boolean getContacted() {
        return contacted;
    }

    public void setContacted(Boolean contacted) {
        this.contacted = contacted;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<StepResult> getStepResultList() {
        return stepResultList;
    }

    public void setStepResultList(List<StepResult> stepResultList) {
        this.stepResultList = stepResultList;
    }
}

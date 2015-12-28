package com.darcytech.transfer.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Version;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

/**
 * User: dixi
 * Time: 2015/12/25 16:21
 */
@Entity
@IdClass(CampaignResultId.class)
public class CampaignResultNew implements Serializable {

    @Column(nullable = false)
    @Version
    protected long version;

    private Long userId;

    @Id
    private Long campaignId;

    private Long campaignTypeId;

    private String buyerNick;

    private String buyerPhone;

    private String buyerRealName;

    @Id
    private Long triggerTradeId;

    /**
     * 营销累计交易数
     */
    private Integer tradeNum = 0;

    /**
     * 营销累计交易
     */
    @Type(type = "JSONString", parameters = {
            @Parameter(name = "targetClass", value = "java.lang.Long"),
            @Parameter(name = "isArray", value = "true")})
    private List<Long> totalTrades = new ArrayList<>();

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

    @Type(type = "JSONString", parameters = {
            @Parameter(name = "targetClass", value = "com.darcytech.transfer.model.CampaignStepResult"),
            @Parameter(name = "isArray", value = "true")})
    private List<CampaignStepResult> campaignStepResults = new ArrayList<>();

    /**
     * 最后修改时间
     */
    private Date lastModifyTime;

    private String oldResultId;

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

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

    public Long getTriggerTradeId() {
        return triggerTradeId;
    }

    public void setTriggerTradeId(Long triggerTradeId) {
        this.triggerTradeId = triggerTradeId;
    }

    public Integer getTradeNum() {
        return tradeNum;
    }

    public void setTradeNum(Integer tradeNum) {
        this.tradeNum = tradeNum;
    }

    public List<Long> getTotalTrades() {
        return totalTrades;
    }

    public void setTotalTrades(List<Long> totalTrades) {
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

    public List<CampaignStepResult> getCampaignStepResults() {
        return campaignStepResults;
    }

    public void setCampaignStepResults(List<CampaignStepResult> campaignStepResults) {
        this.campaignStepResults = campaignStepResults;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getOldResultId() {
        return oldResultId;
    }

    public void setOldResultId(String oldResultId) {
        this.oldResultId = oldResultId;
    }
}

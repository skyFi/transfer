package com.darcytech.transfer.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * User: dixi
 * Time: 2015/12/16 14:02
 */
public class CampaignStepInstance {

    /**
     * 步骤索引
     */
    private int stepIndex;

    /**
     * 步骤名称
     */
    private String stepName;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 触发此步的订单
     */
    private String triggerTradeId;

    /**
     * 是否真实联系过客户
     */
    private Boolean contacted;//步骤至少执行过1个成功动作

    /**
     * 总订单id
     */
    private Set<String> totalTrades = new HashSet<>();

    /**
     * 总动作id
     */
    private Set<String> actionRecords = new HashSet<>();

    public int getStepIndex() {
        return stepIndex;
    }

    public void setStepIndex(int stepIndex) {
        this.stepIndex = stepIndex;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getTriggerTradeId() {
        return triggerTradeId;
    }

    public void setTriggerTradeId(String triggerTradeId) {
        this.triggerTradeId = triggerTradeId;
    }

    public Boolean getContacted() {
        return contacted;
    }

    public void setContacted(Boolean contacted) {
        this.contacted = contacted;
    }

    public Set<String> getTotalTrades() {
        return totalTrades;
    }

    public void setTotalTrades(Set<String> totalTrades) {
        this.totalTrades = totalTrades;
    }

    public Set<String> getActionRecords() {
        return actionRecords;
    }

    public void setActionRecords(Set<String> actionRecords) {
        this.actionRecords = actionRecords;
    }
}
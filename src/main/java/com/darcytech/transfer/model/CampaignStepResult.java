package com.darcytech.transfer.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: darcy
 * Date: 14-4-11
 * Time: 下午4:45
 */
public class CampaignStepResult {

    private Integer stepIndex;

    private String stepName;

    private Date created;

    private Long triggerTradeId;

    private Boolean contacted;//步骤至少执行过1个成功动作

    private Set<Long> totalTrades = new HashSet<>();

    private Set<Long> actionRecords = new HashSet<>();

    public Integer getStepIndex() {
        return stepIndex;
    }

    public void setStepIndex(Integer stepIndex) {
        this.stepIndex = stepIndex;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Long getTriggerTradeId() {
        return triggerTradeId;
    }

    public void setTriggerTradeId(Long triggerTradeId) {
        this.triggerTradeId = triggerTradeId;
    }

    public Boolean getContacted() {
        return contacted;
    }

    public void setContacted(Boolean contacted) {
        this.contacted = contacted;
    }

    public Set<Long> getTotalTrades() {
        return totalTrades;
    }

    public void setTotalTrades(Set<Long> totalTrades) {
        this.totalTrades = totalTrades;
    }

    public Set<Long> getActionRecords() {
        return actionRecords;
    }

    public void setActionRecords(Set<Long> actionRecords) {
        this.actionRecords = actionRecords;
    }
}

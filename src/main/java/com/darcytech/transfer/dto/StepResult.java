package com.darcytech.transfer.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: darcy
 * Date: 14-4-11
 * Time: 下午4:45
 */
public class StepResult {

    private Integer stepIndex;
    private String stepName;
    private Date created;
    private String triggerTradeId;
    private Boolean contacted;//步骤至少执行过1个成功动作
    private Set<String> totalTrades = new HashSet<>();
    private Set<String> actionRecords = new HashSet<>();

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

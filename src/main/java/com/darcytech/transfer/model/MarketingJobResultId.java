package com.darcytech.transfer.model;

import java.io.Serializable;

/**
 * Created by darcy on 2015/12/21.
 */

public class MarketingJobResultId implements Serializable {

    private Long jobId;

    private String buyerNick;

    private String mobile;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
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
}
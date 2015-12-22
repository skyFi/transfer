package com.darcytech.transfer.model;

import java.io.Serializable;

/**
 * Created by darcy on 2015/12/21.
 */
public class MarketingJobResultOrderId implements Serializable {

    private Long jobId;

    private Long oid;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }
}

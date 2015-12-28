package com.darcytech.transfer.model;

import javax.persistence.MappedSuperclass;

/**
 * Created by darcy on 2015/12/28.
 */
@MappedSuperclass
public abstract class BaseRecord extends BaseModel {

    protected String transferDay;

    protected long totalCount;

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public String getTransferDay() {
        return transferDay;
    }

    public void setTransferDay(String transferDay) {
        this.transferDay = transferDay;
    }
}

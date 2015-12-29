package com.darcytech.transfer.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.darcytech.transfer.enumeration.RecordType;

/**
 * Created by darcy on 2015/12/28.
 */
@Entity
public class Record extends BaseModel {

    private String transferDay;

    private long totalCount;

    @Enumerated(EnumType.STRING)
    private RecordType recordType;

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public RecordType getRecordType() {
        return recordType;
    }

    public void setRecordType(RecordType recordType) {
        this.recordType = recordType;
    }

    public String getTransferDay() {
        return transferDay;
    }

    public void setTransferDay(String transferDay) {
        this.transferDay = transferDay;
    }
}

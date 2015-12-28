package com.darcytech.transfer.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.darcytech.transfer.enumeration.FailedDataType;
import com.darcytech.transfer.enumeration.FailedReason;

/**
 * Created by darcy on 2015/12/24.
 */
@Entity
public class FailedData extends BaseModel {

    private String prodId;

    @Enumerated(EnumType.STRING)
    private FailedDataType type;

    private Long userId;

    private String buyerNick;

    private Date transferTime;

    @Enumerated(EnumType.STRING)
    private FailedReason reason;

    private String oids;

    public String getOids() {
        return oids;
    }

    public void setOids(String oids) {
        this.oids = oids;
    }

    public Date getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(Date transferTime) {
        this.transferTime = transferTime;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = String.valueOf(prodId);
    }

    public FailedDataType getType() {
        return type;
    }

    public void setType(FailedDataType type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBuyerNick() {
        return buyerNick;
    }

    public void setBuyerNick(String buyerNick) {
        this.buyerNick = buyerNick;
    }

    public FailedReason getReason() {
        return reason;
    }

    public void setReason(FailedReason reason) {
        this.reason = reason;
    }

}

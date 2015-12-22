package com.darcytech.transfer.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

/**
 * Created by darcy on 2015/12/22.
 */
@Entity
public class Refund {

    @Id
    private String id;

    @Column(nullable = false)
    @Version
    private long version;

    private Long userId;

    private String buyerNick;

    private Long orderId;

    private Long tradeId;

    private String sid;

    private String status;

    private String refundOrderStatus;

    private double refundFee;

    private String reason;

    private boolean hasGoodReturn;

    private String goodStatus;

    private Date goodReturnTime;

    private String description;

    private Date createdTime;

    private String companyName;

    private Date taobaoModified;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getBuyerNick() {
        return buyerNick;
    }

    public void setBuyerNick(String buyerNick) {
        this.buyerNick = buyerNick;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRefundOrderStatus() {
        return refundOrderStatus;
    }

    public void setRefundOrderStatus(String refundOrderStatus) {
        this.refundOrderStatus = refundOrderStatus;
    }

    public double getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(double refundFee) {
        this.refundFee = refundFee;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isHasGoodReturn() {
        return hasGoodReturn;
    }

    public void setHasGoodReturn(boolean hasGoodReturn) {
        this.hasGoodReturn = hasGoodReturn;
    }

    public String getGoodStatus() {
        return goodStatus;
    }

    public void setGoodStatus(String goodStatus) {
        this.goodStatus = goodStatus;
    }

    public Date getGoodReturnTime() {
        return goodReturnTime;
    }

    public void setGoodReturnTime(Date goodReturnTime) {
        this.goodReturnTime = goodReturnTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Date getTaobaoModified() {
        return taobaoModified;
    }

    public void setTaobaoModified(Date taobaoModified) {
        this.taobaoModified = taobaoModified;
    }
}

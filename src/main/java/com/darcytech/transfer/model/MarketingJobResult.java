package com.darcytech.transfer.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Version;

/**
 * Created by darcy on 2015/12/19.
 */
@Entity
@IdClass(MarketingJobResultId.class)
public class MarketingJobResult implements Serializable {

    @Column(nullable = false)
    @Version
    private long version;

    private Long userId;

    @Id
    private Long jobId;

    @Id
    private String buyerNick;

    @Id
    private String mobile;

    private BigDecimal createdPayment;

    private int createdItemNum;

    private BigDecimal paidPayment;

    private int paidItemNum;

    private Date marketingTime;

    private boolean paid;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public BigDecimal getCreatedPayment() {
        return createdPayment;
    }

    public void setCreatedPayment(BigDecimal createdPayment) {
        this.createdPayment = createdPayment;
    }

    public int getCreatedItemNum() {
        return createdItemNum;
    }

    public void setCreatedItemNum(int createdItemNum) {
        this.createdItemNum = createdItemNum;
    }

    public BigDecimal getPaidPayment() {
        return paidPayment;
    }

    public void setPaidPayment(BigDecimal paidPayment) {
        this.paidPayment = paidPayment;
    }

    public int getPaidItemNum() {
        return paidItemNum;
    }

    public void setPaidItemNum(int paidItemNum) {
        this.paidItemNum = paidItemNum;
    }

    public Date getMarketingTime() {
        return marketingTime;
    }

    public void setMarketingTime(Date marketingTime) {
        this.marketingTime = marketingTime;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

}

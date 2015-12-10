package com.darcytech.transfer.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.darcytech.transfer.enumeration.MemberStatus;
import com.darcytech.transfer.enumeration.RelationSource;
import com.darcytech.transfer.enumeration.TaobaoShopMember;

/**
 * @author Eric
 */
public class CustomerDetail {

    private String id;

    private long userId;

    /**
     * 客户昵称
     */
    private String nick;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 省份
     */
    private String state;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String district;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 淘宝买家会员ID
     */
    private Long buyerId;

    /**
     * 会员状态
     */
    private MemberStatus status;

    /**
     * 会员等级
     */
    private TaobaoShopMember grade;

    /**
     * 交易成功次数
     */
    private long tradeCount = 0;

    /**
     * 交易成功金额
     */
    private BigDecimal tradeAmount = new BigDecimal("0");

    /**
     * 最近交易日期
     */
    private Date lastTradeTime;

    /**
     * 关闭交易笔数
     */
    private long closeTradeCount = 0;

    /**
     * 关闭交易金额
     */
    private BigDecimal closeTradeAmount = new BigDecimal("0");

    /**
     * 购买宝贝件数
     */
    private long itemNum = 0;

    /**
     * 淘宝分组id集合
     */
    private Set<String> groupIds = new HashSet<String>();

    /**
     * 关系来源
     */
    private RelationSource relationSource;

    /**
     * 最后一次交易的订单号
     */
    private Long bizOrderId;

    /**
     * 最后下单时间
     */
    private Date lastCreateTime;

    /**
     * 是否接收短信(对批量营销有效)
     * 当inBlacklist & receiverSms = false 时 不发送批量营销短信
     */
    private boolean receiveSms = true;

    /**
     * 是否属于黑名单(对客户关怀和营销短信有效)
     */
    private boolean inBlacklist = false;

    /**
     * 系统中所属客户组Id
     */
    private Set<String> customerGroupIds;

    /**
     * 最后付款时间，来自订单
     */
    private Date lastPayTime;

    /**
     * 最后修改时间
     */
    private Date lastModifyTime;

    private List<Trade> trades;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }

    public TaobaoShopMember getGrade() {
        return grade;
    }

    public void setGrade(TaobaoShopMember grade) {
        this.grade = grade;
    }

    public long getTradeCount() {
        return tradeCount;
    }

    public void setTradeCount(long tradeCount) {
        this.tradeCount = tradeCount;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public Date getLastTradeTime() {
        return lastTradeTime;
    }

    public void setLastTradeTime(Date lastTradeTime) {
        this.lastTradeTime = lastTradeTime;
    }

    public long getCloseTradeCount() {
        return closeTradeCount;
    }

    public void setCloseTradeCount(long closeTradeCount) {
        this.closeTradeCount = closeTradeCount;
    }

    public BigDecimal getCloseTradeAmount() {
        return closeTradeAmount;
    }

    public void setCloseTradeAmount(BigDecimal closeTradeAmount) {
        this.closeTradeAmount = closeTradeAmount;
    }

    public long getItemNum() {
        return itemNum;
    }

    public void setItemNum(long itemNum) {
        this.itemNum = itemNum;
    }

    public Set<String> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(Set<String> groupIds) {
        this.groupIds = groupIds;
    }

    public RelationSource getRelationSource() {
        return relationSource;
    }

    public void setRelationSource(RelationSource relationSource) {
        this.relationSource = relationSource;
    }

    public Long getBizOrderId() {
        return bizOrderId;
    }

    public void setBizOrderId(Long bizOrderId) {
        this.bizOrderId = bizOrderId;
    }

    public Date getLastCreateTime() {
        return lastCreateTime;
    }

    public void setLastCreateTime(Date lastCreateTime) {
        this.lastCreateTime = lastCreateTime;
    }

    public boolean isReceiveSms() {
        return receiveSms;
    }

    public void setReceiveSms(boolean receiveSms) {
        this.receiveSms = receiveSms;
    }

    public boolean isInBlacklist() {
        return inBlacklist;
    }

    public void setInBlacklist(boolean inBlacklist) {
        this.inBlacklist = inBlacklist;
    }

    public Set<String> getCustomerGroupIds() {
        return customerGroupIds;
    }

    public void setCustomerGroupIds(Set<String> customerGroupIds) {
        this.customerGroupIds = customerGroupIds;
    }

    public Date getLastPayTime() {
        return lastPayTime;
    }

    public void setLastPayTime(Date lastPayTime) {
        this.lastPayTime = lastPayTime;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }
}

package com.darcytech.transfer.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.darcytech.transfer.enumeration.StepTradeStatus;
import com.darcytech.transfer.enumeration.TradeStatus;

/**
 * @author Eric
 */
public class Trade {

    private Long id;

    private String customerId;

    private Long userId;

    private Set<Long> oids;

    private String nick;

    /**
     * trade的title
     */
    private String title;

    /**
     * trade的status
     */
    private TradeStatus status;

    /**
     * trade的payment
     */
    private BigDecimal payment;

    /**
     * trade的createdTime
     */
    private Date createdTime;

    /**
     * 淘宝上的最后一次修改时间
     */
    private Date taobaoLastModifyTime;

    /**
     * 交易付款时间
     */
    private Date payTime;

    /**
     * 卖家发货时间
     */
    private Date consignTime;

    /**
     * 交易结束时间
     */
    private Date endTime;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人手机
     */
    private String receiveMobile;

    /**
     * 订单来源
     */
    private Set<String> tradeFrom;

    /**
     * 卖家实际收到的支付宝打款金额
     */
    private BigDecimal receivedPayment;

    /**
     * 收货省份
     */
    private String receiverState;

    /**
     * 收货城市
     */
    private String receiverCity;

    /**
     * 收货区县
     */
    private String receiverDistrict;

    /**
     * 收货地址
     */
    private String receiverAddress;

    /**
     * 快递单号
     */
    private String shippingNo;

    /**
     * 快递公司名称
     */
    private String shippingCompanyName;

    /**
     * 最后一次修改时间
     */
    private Date lastModifyTime;

    /**
     * 买家留言
     */
    private String buyerMessage;

    /**
     * 卖家备注
     */
    private String sellerMemo;

    /**
     * 卖家备注旗帜（与淘宝网上订单的卖家备注旗帜对应，只有卖家才能查看该字段）<br/>
     * 红、黄、绿、蓝、紫 分别对应 1、2、3、4、5<br/>
     * 0 代表无旗帜
     */
    private Long sellerFlag;

    /**
     * 交易类型列表。<br/>
     * 可选值：fixed(一口价) auction(拍卖) guarantee_trade(一口价、拍卖) auto_delivery(自动发货) <br/>
     * independent_simple_trade(旺店入门版交易) independent_shop_trade(旺店标准版交易) ec(直冲) cod(货到付款)<br/>
     * fenxiao(分销) game_equipment(游戏装备) shopex_trade(ShopEX交易) netcn_trade(万网交易)<br/>
     * external_trade(统一外部交易) step (万人团) nopaid(无付款订单)
     */
    private String type;

    /**
     * 买家是否已评价
     */
    private Boolean buyerRate;

    /**
     * 卖家是否已评价
     */
    private Boolean sellerRate;

    /**
     * 超时到期时间
     */
    private Date timeoutActionTime;

    /**
     * 创建交易时的物流方式
     */
    private String shippingType;

    /**
     * 邮费
     */
    private BigDecimal postFee;

    /**
     * 不含邮费的实付金额
     */
    private BigDecimal paymentWithoutPostFee;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * 商品总数
     */

    private long itemNum;

    private StepTradeStatus stepTradeStatus;

    private BigDecimal stepPaidFee;

    private List<Order> orders;

    public void addOrder(Order order) {
        if (this.orders == null) {
            this.orders = new ArrayList<>();
        }
        if (this.orders.isEmpty()) {
            this.orders.add(order);
        } else {
            Boolean hasOrder = false;
            for (int i = 0; i < this.orders.size(); i++) {
                if (orders.get(i).getId().equals(order.getId())) {
                    orders.set(i, order);
                    hasOrder = true;
                }
            }

            if (!hasOrder) {
                this.orders.add(order);
            }
        }

    }

    public Set<Long> getOids() {
        return oids;
    }

    public void setOids(Set<Long> oids) {
        this.oids = oids;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TradeStatus getStatus() {
        return status;
    }

    public void setStatus(TradeStatus status) {
        this.status = status;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getTaobaoLastModifyTime() {
        return taobaoLastModifyTime;
    }

    public void setTaobaoLastModifyTime(Date taobaoLastModifyTime) {
        this.taobaoLastModifyTime = taobaoLastModifyTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getConsignTime() {
        return consignTime;
    }

    public void setConsignTime(Date consignTime) {
        this.consignTime = consignTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiveMobile() {
        return receiveMobile;
    }

    public void setReceiveMobile(String receiveMobile) {
        this.receiveMobile = receiveMobile;
    }

    public Set<String> getTradeFrom() {
        return tradeFrom;
    }

    public void setTradeFrom(Set<String> tradeFrom) {
        this.tradeFrom = tradeFrom;
    }

    public BigDecimal getReceivedPayment() {
        return receivedPayment;
    }

    public void setReceivedPayment(BigDecimal receivedPayment) {
        this.receivedPayment = receivedPayment;
    }

    public String getReceiverState() {
        return receiverState;
    }

    public void setReceiverState(String receiverState) {
        this.receiverState = receiverState;
    }

    public String getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public String getReceiverDistrict() {
        return receiverDistrict;
    }

    public void setReceiverDistrict(String receiverDistrict) {
        this.receiverDistrict = receiverDistrict;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getShippingNo() {
        return shippingNo;
    }

    public void setShippingNo(String shippingNo) {
        this.shippingNo = shippingNo;
    }

    public String getShippingCompanyName() {
        return shippingCompanyName;
    }

    public void setShippingCompanyName(String shippingCompanyName) {
        this.shippingCompanyName = shippingCompanyName;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getBuyerMessage() {
        return buyerMessage;
    }

    public void setBuyerMessage(String buyerMessage) {
        this.buyerMessage = buyerMessage;
    }

    public String getSellerMemo() {
        return sellerMemo;
    }

    public void setSellerMemo(String sellerMemo) {
        this.sellerMemo = sellerMemo;
    }

    public Long getSellerFlag() {
        return sellerFlag;
    }

    public void setSellerFlag(Long sellerFlag) {
        this.sellerFlag = sellerFlag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getBuyerRate() {
        return buyerRate;
    }

    public void setBuyerRate(Boolean buyerRate) {
        this.buyerRate = buyerRate;
    }

    public Boolean getSellerRate() {
        return sellerRate;
    }

    public void setSellerRate(Boolean sellerRate) {
        this.sellerRate = sellerRate;
    }

    public Date getTimeoutActionTime() {
        return timeoutActionTime;
    }

    public void setTimeoutActionTime(Date timeoutActionTime) {
        this.timeoutActionTime = timeoutActionTime;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public BigDecimal getPostFee() {
        return postFee;
    }

    public void setPostFee(BigDecimal postFee) {
        this.postFee = postFee;
    }

    public BigDecimal getPaymentWithoutPostFee() {
        return paymentWithoutPostFee;
    }

    public void setPaymentWithoutPostFee(BigDecimal paymentWithoutPostFee) {
        this.paymentWithoutPostFee = paymentWithoutPostFee;
    }

    public long getItemNum() {
        return itemNum;
    }

    public void setItemNum(long itemNum) {
        this.itemNum = itemNum;
    }

    public StepTradeStatus getStepTradeStatus() {
        return stepTradeStatus;
    }

    public void setStepTradeStatus(StepTradeStatus stepTradeStatus) {
        this.stepTradeStatus = stepTradeStatus;
    }

    public BigDecimal getStepPaidFee() {
        return stepPaidFee;
    }

    public void setStepPaidFee(BigDecimal stepPaidFee) {
        this.stepPaidFee = stepPaidFee;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

}

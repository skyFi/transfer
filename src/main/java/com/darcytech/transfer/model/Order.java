package com.darcytech.transfer.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import com.darcytech.transfer.enumeration.RefundStatus;
import com.darcytech.transfer.enumeration.TradeStatus;

/**
 * @author Eric
 */
public class Order {

    private Long id;

    private Long tradeId;

    private Long customerId;

    /**
     * item的id
     */
    private Long itemId;

    /**
     * item的title
     */
    private String itemTitle;

    /**
     * item的ImgUrl
     */
    private String itemImgUrl;

    /**
     * 退款状态
     */
    private RefundStatus refundStatus;

    /**
     * 卖家发货时间
     */
    private Date consignTime;

    /**
     * sku id
     */
    private String skuId;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 实付金额
     */
    private BigDecimal payment;

    /**
     * Order金额， 不含邮费
     */
    private BigDecimal orderPayment;

    private Long num;

    private Set<String> orderFrom;

    /**
     * 快递单号
     */
    private String shippingNo;

    /**
     * 快递公司名称
     */
    private String shippingCompanyName;

    /**
     * trade的status
     */
    private TradeStatus status;

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

    public TradeStatus getStatus() {
        return status;
    }

    public void setStatus(TradeStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemImgUrl() {
        return itemImgUrl;
    }

    public void setItemImgUrl(String itemImgUrl) {
        this.itemImgUrl = itemImgUrl;
    }

    public RefundStatus getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(RefundStatus refundStatus) {
        this.refundStatus = refundStatus;
    }

    public Date getConsignTime() {
        return consignTime;
    }

    public void setConsignTime(Date consignTime) {
        this.consignTime = consignTime;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public BigDecimal getOrderPayment() {
        return orderPayment;
    }

    public void setOrderPayment(BigDecimal orderPayment) {
        this.orderPayment = orderPayment;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public Set<String> getOrderFrom() {
        return orderFrom;
    }

    public void setOrderFrom(Set<String> orderFrom) {
        this.orderFrom = orderFrom;
    }
}

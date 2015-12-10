package com.darcytech.transfer.enumeration;

/**
 * 订单状态
 *
 * @author Eric
 */
public enum TradeStatus {
    /**
     * 没有创建支付宝交易
     */
    TRADE_NO_CREATE_PAY("没有创建支付宝交易"),

    /**
     * 等待买家付款
     */
    WAIT_BUYER_PAY("等待买家付款"),

    /**
     * 等待卖家发货,即:买家已付款
     */
    WAIT_SELLER_SEND_GOODS("等待卖家发货(买家已付款)"),

    /**
     * 等待买家确认收货,即:卖家已发货
     */
    WAIT_BUYER_CONFIRM_GOODS("等待买家确认收货(卖家已发货)"),

    /**
     * 买家已签收,货到付款专用
     */
    TRADE_BUYER_SIGNED("买家已签收(货到付款专用)"),

    /**
     * 交易成功
     */
    TRADE_FINISHED("交易成功"),

    /**
     * 付款以后用户退款成功，交易自动关闭
     */
    TRADE_CLOSED("付款以后用户退款成功，交易自动关闭"),

    /**
     * 付款以前，卖家或买家主动关闭交易
     */
    TRADE_CLOSED_BY_TAOBAO("付款以前，卖家或买家主动关闭交易"),

    /**
     * 包含：WAIT_BUYER_PAY、TRADE_NO_CREATE_PAY
     */
    ALL_WAIT_PAY(""),

    /**
     * 包含：TRADE_CLOSED、TRADE_CLOSED_BY_TAOBAO
     */
    ALL_CLOSED(""),

    /**
     * 部分发货
     */
    SELLER_CONSIGNED_PART(""),

    PAY_PENDING("国际信用卡支付付款确认中"),

    WAIT_PRE_AUTH_CONFIRM("0元购合约中");

    private String displayName;

    private TradeStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

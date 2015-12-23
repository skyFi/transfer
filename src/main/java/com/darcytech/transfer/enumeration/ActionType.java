package com.darcytech.transfer.enumeration;

/**
 * Created by darcy on 2015/12/23.
 */
public enum ActionType {

    /**
     * 发短信
     */
    SMS(true),
    /**
     * 写备注
     */
    TRADE_MEMO(true),
    /**
     * 通知客服
     */
    CUSTOMER_SERVICE(false),
    /**
     * 客服点击短信
     */
    CUSTOMER_SERVICE_SMS(true),
    /**
     * 客服点击旺旺
     */
    CUSTOMER_SERVICE_WW(true),
    /**
     * 发放优惠券
     */
    COUPON(true);

    private boolean contact;

    ActionType(boolean contact) {
        this.contact = contact;
    }

    public boolean isContact() {
        return contact;
    }
}

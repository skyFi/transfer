package com.darcytech.transfer.enumeration;

/**
 * 退款状态
 *
 * @author Eric
 */
public enum RefundStatus {

    /**
     * 买家已经申请退款，等待卖家同意
     */
    WAIT_SELLER_AGREE,

    /**
     * 卖家已经同意退款，等待买家退货
     */
    WAIT_BUYER_RETURN_GOODS,

    /**
     * 买家已经退货，等待卖家确认收货
     */
    WAIT_SELLER_CONFIRM_GOODS,

    /**
     * 卖家拒绝退款
     */
    SELLER_REFUSE_BUYER,

    /**
     * 退款关闭
     */
    CLOSED,

    /**
     * 退款成功
     */
    SUCCESS,

    /**
     * 无退款
     */
    NO_REFUND,

    /**
     *
     */
    WAIT_BUYER_CONFIRM_REDO_SEND_GOODS

}

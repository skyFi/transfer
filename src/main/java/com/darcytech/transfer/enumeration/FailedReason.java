package com.darcytech.transfer.enumeration;

/**
 * Created by darcy on 2015/12/24.
 */
public enum FailedReason {

    /**
     * 数据过期
     * */
    EXPIRED,

    /**
     * 没有order的trade
     * */
    TRADE_HAS_NO_ORDERS,

    /**
     * 数据丢失
     * */
    MISSING,

    /**
     * 没有找到用户
     * */
    CANT_FIND_USERID


}

package com.darcytech.transfer.enumeration;

/**
 * 会员关系来源
 *
 * @author Eric
 */
public enum RelationSource {

    TRADE_SUCCESS(1, "交易成功"),

    TRADE_UNSUCCESS(2, "交易未成功"),

    SELLER_CATCH(3, "卖家主动吸纳");

    private int value;

    private String display;

    RelationSource(int value, String display) {
        this.value = value;
        this.display = display;
    }

    public int getValue() {
        return value;
    }

    public String getDisplay() {
        return display;
    }

}

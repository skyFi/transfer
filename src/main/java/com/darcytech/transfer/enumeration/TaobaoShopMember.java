package com.darcytech.transfer.enumeration;

/**
 * 淘宝店铺会员等级
 *
 * @author Eric
 */
public enum TaobaoShopMember {

    /**
     * 店铺会员
     */
    VIP0("店铺客户", 0),

    /**
     * 普通会员
     */
    VIP1("普通会员", 1),

    /**
     * 高级会员
     */
    VIP2("高级会员", 2),

    /**
     * VIP会员
     */
    VIP3("VIP会员", 3),

    /**
     * 至尊VIP会员
     */
    VIP4("至尊VIP会员", 4);

    private String display;

    private int level;

    private TaobaoShopMember(String display, int level) {
        this.display = display;
        this.level = level;
    }

    public String getDisplay() {
        return this.display;
    }

    public int getLevel() {
        return this.level;
    }

}

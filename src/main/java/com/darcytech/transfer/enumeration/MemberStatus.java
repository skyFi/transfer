package com.darcytech.transfer.enumeration;

/**
 * @author Eric
 */
public enum MemberStatus {

    /**
     * 正常
     */
    normal("正常"),

    /**
     * 被卖家删除
     */
    delete("被卖家删除"),

    /**
     * 黑名单
     */
    blacklist("黑名单"),

    exceptionStatus("异常状态");

    private String display;

    private MemberStatus(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}

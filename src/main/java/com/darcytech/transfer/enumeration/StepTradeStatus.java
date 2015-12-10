package com.darcytech.transfer.enumeration;

/**
 * @author Eric
 */
public enum StepTradeStatus {

    FRONT_NOPAID_FINAL_NOPAID("定金未付尾款未付"),
    FRONT_PAID_FINAL_NOPAID("定金已付尾款未付"),
    FRONT_PAID_FINAL_PAID("定金和尾款都付");

    private String display;

    StepTradeStatus(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}

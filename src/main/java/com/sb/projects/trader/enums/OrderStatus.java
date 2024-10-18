package com.sb.projects.trader.enums;

public enum OrderStatus {
    Saved("saved"),
    Processing("processing"),
    Submitted("submitted"),
    Completed("completed"),
    Rejected("rejected");

    public final String label;

    private OrderStatus(String label){
        this.label = label;
    }

    public String value(){
        return this.label;
    }
}

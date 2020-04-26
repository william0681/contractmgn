package com.zx.servicegateway.vo;

import com.zx.servicegateway.model.AccountReceivable;

import java.io.Serializable;

public class OperationNode extends AccountReceivable implements Serializable {

	private static final long serialVersionUID = 1L;

    private Integer type;
    /**
     * 具体日期或（合同签订后/服务开始后）
     */
    private String time;

    private Integer days;

    private Integer years;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getYears() {
        return years;
    }

    public void setYears(Integer years) {
        this.years = years;
    }
}

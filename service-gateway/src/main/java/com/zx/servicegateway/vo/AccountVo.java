package com.zx.servicegateway.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class AccountVo implements Serializable {
    private static final long serialVersionUID = 8662217551079078771L;
    /**
     * 节点名
     */
    private String nodeName;
    /**
     * 期数
     */
    private Integer stage;
    /**
     * 应收款
     */
    private Double receivableMoney;
    /**
     * 应收款日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date receivableDate;
    /**
     * 实收款金额
     */
    private Double ActualMoney;
    /**
     *实收款日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date actualDate;
    /**
     * 是否支付完成
     */
    private Integer status;

    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public Double getReceivableMoney() {
        return receivableMoney;
    }

    public void setReceivableMoney(Double receivableMoney) {
        this.receivableMoney = receivableMoney;
    }

    public Date getReceivableDate() {
        return receivableDate;
    }

    public void setReceivableDate(Date receivableDate) {
        this.receivableDate = receivableDate;
    }

    public Double getActualMoney() {
        return ActualMoney;
    }

    public void setActualMoney(Double actualMoney) {
        ActualMoney = actualMoney;
    }

    public Date getActualDate() {
        return actualDate;
    }

    public void setActualDate(Date actualDate) {
        this.actualDate = actualDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

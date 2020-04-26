package com.zx.servicereminder.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class Reminder implements Serializable {
    private Integer id;

    /**
     * 1合同信息2财务信息
     */
    private Integer type;

    /**
     * 合同id
     */
    private Integer contractId;

    /**
     * 操作人员id
     */
    private Integer operator;

    /**
     * 操作类型
     */
    private String operateType;

    /**
     * 操作时间
     */
    private Date operateTime;

    private Boolean manager;

    private Boolean accountant;

    private Boolean overallchief;

    private Boolean areachief;

    private Boolean buyer;

    private Boolean responser;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public Integer getOperator() {
        return operator;
    }

    public void setOperator(Integer operator) {
        this.operator = operator;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Boolean getManager() {
        return manager;
    }

    public void setManager(Boolean manager) {
        this.manager = manager;
    }

    public Boolean getAccountant() {
        return accountant;
    }

    public void setAccountant(Boolean accountant) {
        this.accountant = accountant;
    }

    public Boolean getOverallchief() {
        return overallchief;
    }

    public void setOverallchief(Boolean overallchief) {
        this.overallchief = overallchief;
    }

    public Boolean getAreachief() {
        return areachief;
    }

    public void setAreachief(Boolean areachief) {
        this.areachief = areachief;
    }

    public Boolean getBuyer() {
        return buyer;
    }

    public void setBuyer(Boolean buyer) {
        this.buyer = buyer;
    }

    public Boolean getResponser() {
        return responser;
    }

    public void setResponser(Boolean responser) {
        this.responser = responser;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Reminder other = (Reminder) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getContractId() == null ? other.getContractId() == null : this.getContractId().equals(other.getContractId()))
            && (this.getOperator() == null ? other.getOperator() == null : this.getOperator().equals(other.getOperator()))
            && (this.getOperateType() == null ? other.getOperateType() == null : this.getOperateType().equals(other.getOperateType()))
            && (this.getOperateTime() == null ? other.getOperateTime() == null : this.getOperateTime().equals(other.getOperateTime()))
            && (this.getManager() == null ? other.getManager() == null : this.getManager().equals(other.getManager()))
            && (this.getAccountant() == null ? other.getAccountant() == null : this.getAccountant().equals(other.getAccountant()))
            && (this.getOverallchief() == null ? other.getOverallchief() == null : this.getOverallchief().equals(other.getOverallchief()))
            && (this.getAreachief() == null ? other.getAreachief() == null : this.getAreachief().equals(other.getAreachief()))
            && (this.getBuyer() == null ? other.getBuyer() == null : this.getBuyer().equals(other.getBuyer()))
            && (this.getResponser() == null ? other.getResponser() == null : this.getResponser().equals(other.getResponser()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getContractId() == null) ? 0 : getContractId().hashCode());
        result = prime * result + ((getOperator() == null) ? 0 : getOperator().hashCode());
        result = prime * result + ((getOperateType() == null) ? 0 : getOperateType().hashCode());
        result = prime * result + ((getOperateTime() == null) ? 0 : getOperateTime().hashCode());
        result = prime * result + ((getManager() == null) ? 0 : getManager().hashCode());
        result = prime * result + ((getAccountant() == null) ? 0 : getAccountant().hashCode());
        result = prime * result + ((getOverallchief() == null) ? 0 : getOverallchief().hashCode());
        result = prime * result + ((getAreachief() == null) ? 0 : getAreachief().hashCode());
        result = prime * result + ((getBuyer() == null) ? 0 : getBuyer().hashCode());
        result = prime * result + ((getResponser() == null) ? 0 : getResponser().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", type=").append(type);
        sb.append(", contractId=").append(contractId);
        sb.append(", operator=").append(operator);
        sb.append(", operateType=").append(operateType);
        sb.append(", operateTime=").append(operateTime);
        sb.append(", manager=").append(manager);
        sb.append(", accountant=").append(accountant);
        sb.append(", overallchief=").append(overallchief);
        sb.append(", areachief=").append(areachief);
        sb.append(", buyer=").append(buyer);
        sb.append(", responser=").append(responser);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
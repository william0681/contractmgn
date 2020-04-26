package com.zx.servicereminder.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class Contract implements Serializable {
    private Integer id;

    private Integer createUser;

    /**
     * 合同编号
     */
    private String number;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 合同类别
     */
    private Integer contractType;

    /**
     * 合同内容类别
     */
    private Integer contentType;

    /**
     * 合同名称
     */
    private String name;

    /**
     * 归属区块
     */
    private Integer block;

    /**
     * 经办人
     */
    private String operator;

    /**
     * 技术服务员
     */
    private String technician;

    /**
     * 客户名称
     */
    private String customer;

    /**
     * 合同签订日期
     */
    private Date signDate;

    /**
     * 合同到期日期
     */
    private Date due;

    /**
     * 合同金额
     */
    private Double amount;

    /**
     * 合同剩余应支付金额
     */
    private Double restAmount;

    /**
     * 合同内容
     */
    private String content;

    /**
     * 合同服务开始时间（运维类）
     */
    private Date serviceStart;

    /**
     * 合同服务结束时间（运维类合同）
     */
    private Date serviceEnd;

    /**
     * 归档标记
     */
    private Boolean archiveTag;

    /**
     * 是否运维合同
     */
    private Boolean operationTag;

    /**
     * 应收款记录id，表示改合同进行到哪一次收款
     */
    private Integer accountId;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getContractType() {
        return contractType;
    }

    public void setContractType(Integer contractType) {
        this.contractType = contractType;
    }

    public Integer getContentType() {
        return contentType;
    }

    public void setContentType(Integer contentType) {
        this.contentType = contentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBlock() {
        return block;
    }

    public void setBlock(Integer block) {
        this.block = block;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getTechnician() {
        return technician;
    }

    public void setTechnician(String technician) {
        this.technician = technician;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    public Date getDue() {
        return due;
    }

    public void setDue(Date due) {
        this.due = due;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getRestAmount() {
        return restAmount;
    }

    public void setRestAmount(Double restAmount) {
        this.restAmount = restAmount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getServiceStart() {
        return serviceStart;
    }

    public void setServiceStart(Date serviceStart) {
        this.serviceStart = serviceStart;
    }

    public Date getServiceEnd() {
        return serviceEnd;
    }

    public void setServiceEnd(Date serviceEnd) {
        this.serviceEnd = serviceEnd;
    }

    public Boolean getArchiveTag() {
        return archiveTag;
    }

    public void setArchiveTag(Boolean archiveTag) {
        this.archiveTag = archiveTag;
    }

    public Boolean getOperationTag() {
        return operationTag;
    }

    public void setOperationTag(Boolean operationTag) {
        this.operationTag = operationTag;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
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
        Contract other = (Contract) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getNumber() == null ? other.getNumber() == null : this.getNumber().equals(other.getNumber()))
            && (this.getCreateDate() == null ? other.getCreateDate() == null : this.getCreateDate().equals(other.getCreateDate()))
            && (this.getContractType() == null ? other.getContractType() == null : this.getContractType().equals(other.getContractType()))
            && (this.getContentType() == null ? other.getContentType() == null : this.getContentType().equals(other.getContentType()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getBlock() == null ? other.getBlock() == null : this.getBlock().equals(other.getBlock()))
            && (this.getOperator() == null ? other.getOperator() == null : this.getOperator().equals(other.getOperator()))
            && (this.getTechnician() == null ? other.getTechnician() == null : this.getTechnician().equals(other.getTechnician()))
            && (this.getCustomer() == null ? other.getCustomer() == null : this.getCustomer().equals(other.getCustomer()))
            && (this.getSignDate() == null ? other.getSignDate() == null : this.getSignDate().equals(other.getSignDate()))
            && (this.getDue() == null ? other.getDue() == null : this.getDue().equals(other.getDue()))
            && (this.getAmount() == null ? other.getAmount() == null : this.getAmount().equals(other.getAmount()))
            && (this.getRestAmount() == null ? other.getRestAmount() == null : this.getRestAmount().equals(other.getRestAmount()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getServiceStart() == null ? other.getServiceStart() == null : this.getServiceStart().equals(other.getServiceStart()))
            && (this.getServiceEnd() == null ? other.getServiceEnd() == null : this.getServiceEnd().equals(other.getServiceEnd()))
            && (this.getArchiveTag() == null ? other.getArchiveTag() == null : this.getArchiveTag().equals(other.getArchiveTag()))
            && (this.getOperationTag() == null ? other.getOperationTag() == null : this.getOperationTag().equals(other.getOperationTag()))
            && (this.getAccountId() == null ? other.getAccountId() == null : this.getAccountId().equals(other.getAccountId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getNumber() == null) ? 0 : getNumber().hashCode());
        result = prime * result + ((getCreateDate() == null) ? 0 : getCreateDate().hashCode());
        result = prime * result + ((getContractType() == null) ? 0 : getContractType().hashCode());
        result = prime * result + ((getContentType() == null) ? 0 : getContentType().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getBlock() == null) ? 0 : getBlock().hashCode());
        result = prime * result + ((getOperator() == null) ? 0 : getOperator().hashCode());
        result = prime * result + ((getTechnician() == null) ? 0 : getTechnician().hashCode());
        result = prime * result + ((getCustomer() == null) ? 0 : getCustomer().hashCode());
        result = prime * result + ((getSignDate() == null) ? 0 : getSignDate().hashCode());
        result = prime * result + ((getDue() == null) ? 0 : getDue().hashCode());
        result = prime * result + ((getAmount() == null) ? 0 : getAmount().hashCode());
        result = prime * result + ((getRestAmount() == null) ? 0 : getRestAmount().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getServiceStart() == null) ? 0 : getServiceStart().hashCode());
        result = prime * result + ((getServiceEnd() == null) ? 0 : getServiceEnd().hashCode());
        result = prime * result + ((getArchiveTag() == null) ? 0 : getArchiveTag().hashCode());
        result = prime * result + ((getOperationTag() == null) ? 0 : getOperationTag().hashCode());
        result = prime * result + ((getAccountId() == null) ? 0 : getAccountId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", createUser=").append(createUser);
        sb.append(", number=").append(number);
        sb.append(", createDate=").append(createDate);
        sb.append(", contractType=").append(contractType);
        sb.append(", contentType=").append(contentType);
        sb.append(", name=").append(name);
        sb.append(", block=").append(block);
        sb.append(", operator=").append(operator);
        sb.append(", technician=").append(technician);
        sb.append(", customer=").append(customer);
        sb.append(", signDate=").append(signDate);
        sb.append(", due=").append(due);
        sb.append(", amount=").append(amount);
        sb.append(", restAmount=").append(restAmount);
        sb.append(", content=").append(content);
        sb.append(", serviceStart=").append(serviceStart);
        sb.append(", serviceEnd=").append(serviceEnd);
        sb.append(", archiveTag=").append(archiveTag);
        sb.append(", operationTag=").append(operationTag);
        sb.append(", accountId=").append(accountId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
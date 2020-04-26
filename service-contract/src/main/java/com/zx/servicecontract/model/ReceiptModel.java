package com.zx.servicecontract.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * ReceiptModel
 */
public class ReceiptModel implements Serializable{

    private static final long serialVersionUID = 1L;

    private Integer id;

    

    @JsonProperty("contract_id")
    private Integer contractId;

    /**
     * 期数
     */
    @JsonProperty("issue_id")
    private Integer issueId;

    /**
     * 
     */
    private Double receipt;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8", locale = "zh")
    @JsonProperty("receipt_date")
    private Date receiptDate;

    private String remark;

    @JsonProperty("sellers")
    private List<Seller> sellers;


    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8", locale = "zh")
    @JsonProperty("due_date")
    private Date dueDate;

    /**
     * @return the contractId
     */
    public Integer getContractId() {
        return contractId;
    }

    /**
     * @param contractId the contractId to set
     */
    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    /**
     * @return the issueId
     */
    public Integer getIssueId() {
        return issueId;
    }

    /**
     * @param issueId the issueId to set
     */
    public void setIssueId(Integer issueId) {
        this.issueId = issueId;
    }

    /**
     * @return the receipt
     */
    public Double getReceipt() {
        return receipt;
    }

    /**
     * @param receipt the receipt to set
     */
    public void setReceipt(Double receipt) {
        this.receipt = receipt;
    }

    /**
     * @return the receiptDate
     */
    public Date getReceiptDate() {
        return receiptDate;
    }

    /**
     * @param receiptDate the receiptDate to set
     */
    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the sellers
     */
    public List<Seller> getSellers() {
        return sellers;
    }

    /**
     * @param sellers the sellers to set
     */
    public void setSellers(List<Seller> sellers) {
        this.sellers = sellers;
    }

    /**
     * @return the dueDate
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * @param dueDate the dueDate to set
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    

    
}
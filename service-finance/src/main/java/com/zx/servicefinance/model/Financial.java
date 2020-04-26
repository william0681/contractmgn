package com.zx.servicefinance.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Financial
 */
public class Financial implements Serializable{

    private static final long serialVersionUID = 1L;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("contract_id")
    private Integer contractId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("total_amount")
    private Double totalAmount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("un_payment")
    private Double unPayment;

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
     * @return the totalAmount
     */
    public Double getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * @return the unPayment
     */
    public Double getUnPayment() {
        return unPayment;
    }

    /**
     * @param unPayment the unPayment to set
     */
    public void setUnPayment(Double unPayment) {
        this.unPayment = unPayment;
    }


    



    
}
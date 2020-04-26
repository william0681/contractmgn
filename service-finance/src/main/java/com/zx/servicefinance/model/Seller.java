package com.zx.servicefinance.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Seller
 */
public class Seller implements Serializable{


    private static final long serialVersionUID = 1L;

    private Integer percentage; 

    @JsonProperty("seller_id")
    private Integer sellerId;

    /**
     * @return the percentage
     */
    public Integer getPercentage() {
        return percentage;
    }

    

    /**
     * @param percentage the percentage to set
     */
    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }

    /**
     * @return the sellerId
     */
    public Integer getSellerId() {
        return sellerId;
    }

    /**
     * @param sellerId the sellerId to set
     */
    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }


    

    
}
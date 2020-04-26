package com.zx.servicegateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * ActureReceiptList
 */
public class ActuralReceiptList implements Serializable {

    private static final long serialVersionUID = 1L;


    

    /**
     * 合同总额
     */

    
    @JsonProperty("total_amount")
    private Double totalAmount;

    /**
     * 实收款列表
     */
    @JsonProperty("acture_receipts")
    private List<ActuralReceipt> actureReceipts;

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
     * @return the actureReceipts
     */
    public List<ActuralReceipt> getActureReceipts() {
        return actureReceipts;
    }

    /**
     * @param actureReceipts the actureReceipts to set
     */
    public void setActureReceipts(List<ActuralReceipt> actureReceipts) {
        this.actureReceipts = actureReceipts;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((actureReceipts == null) ? 0 : actureReceipts.hashCode());
        result = prime * result + ((totalAmount == null) ? 0 : totalAmount.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ActuralReceiptList))
            return false;
        ActuralReceiptList other = (ActuralReceiptList) obj;
        if (actureReceipts == null) {
            if (other.actureReceipts != null)
                return false;
        } else if (!actureReceipts.equals(other.actureReceipts))
            return false;
        if (totalAmount == null) {
            if (other.totalAmount != null)
                return false;
        } else if (!totalAmount.equals(other.totalAmount))
            return false;
        return true;
    }

    

    
}
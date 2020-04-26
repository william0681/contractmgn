package com.zx.servicegateway.vo;

import com.zx.servicegateway.model.Contract;
import com.zx.servicegateway.model.ContractNode;
import com.zx.servicegateway.model.ContractSeller;

import java.util.List;

public class ContractVo extends Contract{


    private static final long serialVersionUID = 1L;

    private List<ContractNode> contractNodeList;

    private List<ContractSeller> contractSellerList;


    public List<ContractSeller> getContractSellerList() {
        return contractSellerList;
    }

    public void setContractSellerList(List<ContractSeller> contractSellerList) {
        this.contractSellerList = contractSellerList;
    }

    public List<ContractNode> getContractNodeList() {
        return contractNodeList;
    }

    public void setContractNodeList(List<ContractNode> contractNodeList) {
        this.contractNodeList = contractNodeList;
    }
}

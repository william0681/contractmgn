package com.zx.servicecontract.vo;

import com.zx.servicecontract.model.Contract;
import com.zx.servicecontract.model.ContractNode;
import com.zx.servicecontract.model.ContractSeller;

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

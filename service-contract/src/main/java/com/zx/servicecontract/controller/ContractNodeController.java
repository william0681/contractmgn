package com.zx.servicecontract.controller;

import com.zx.servicecontract.model.ContractNode;
import com.zx.servicecontract.pojo.Message;
import com.zx.servicecontract.service.ContractNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ContractNodeController {

    @Autowired
    ContractNodeService contractNodeService;

    @GetMapping("/contractNode/getContractNodeList")
    Message getContractNodeList(@RequestParam("contractId") Integer contractId){
        return contractNodeService.getContractNodeList(contractId);
    }

    @GetMapping("/contractNode/updateContractNode")
    Message updateContractNode(@RequestParam("contractNodeList") List<ContractNode> contractNodeList){
        return contractNodeService.updateContractNode(contractNodeList);
    }
}

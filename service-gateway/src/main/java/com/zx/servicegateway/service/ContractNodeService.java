package com.zx.servicegateway.service;

import com.zx.servicegateway.model.ContractNode;
import com.zx.servicegateway.pojo.Message;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("service-contract")
@Repository
public interface ContractNodeService {

    @GetMapping("/contractNode/getContractNodeList")
    Message getContractNodeList(@RequestParam("contractId") Integer contractId);

    @GetMapping("/contractNode/updateContractNode")
    Message updateContractNode(@RequestParam("contractNodeList") List<ContractNode> contractNodeList);

}

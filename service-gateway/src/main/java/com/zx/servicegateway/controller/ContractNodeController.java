package com.zx.servicegateway.controller;

import com.zx.servicegateway.pojo.Message;
import com.zx.servicegateway.service.ContractNodeService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContractNodeController {
    @Autowired
    private ContractNodeService contractNodeService;

    /**
     * 更新合同节点
     * @param contractNodeList 合同节点List对象
     * @return Message
     */
    //@RequiresRoles({"manager","accountant","overallchief"})
//    @ApiOperation(value = "更新合同节点")
//    @PutMapping("/contractNode")
//    public Message updateContractNode(@RequestBody List<ContractNode> contractNodeList) {
//        return contractNodeService.updateContractNode(contractNodeList);
//    }

    /**
     * 获取合同节点list
     * @param contractId
     * @return
     */
    @RequiresRoles(value = {"manager","accountant","overallchief"},logical = Logical.OR)
    @ApiOperation("获取合同节点列表")
    @GetMapping("contractNode")
    public Message getContractNodeList(@RequestParam("contractId")Integer contractId){
        return contractNodeService.getContractNodeList(contractId);
    }
}

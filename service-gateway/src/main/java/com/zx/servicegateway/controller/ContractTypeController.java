package com.zx.servicegateway.controller;

import com.zx.servicegateway.pojo.Message;
import com.zx.servicegateway.service.ContractTypeService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ContractTypeController {

    @Autowired
    ContractTypeService contractTypeService;

    @PostMapping("contractType")
    @RequiresRoles(value = {"manager","accountant","overallchief"},logical = Logical.OR)
    public Message addContractType(@RequestParam String typeName){
        return contractTypeService.addContractType(typeName);
    }

    @GetMapping("contractType")
    public Message getContractType(){
        return contractTypeService.getContractType();
    }

    @PutMapping("contractType")
    @RequiresRoles(value = {"manager","accountant","overallchief"},logical = Logical.OR)
    public Message renameContractType(@RequestParam int id, @RequestParam String typeName){
        return  contractTypeService.renameContractType(id, typeName);
    }
}

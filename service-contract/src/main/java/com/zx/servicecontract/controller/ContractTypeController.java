package com.zx.servicecontract.controller;

import com.zx.servicecontract.pojo.Message;
import com.zx.servicecontract.service.ContractTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContractTypeController {

    @Autowired
    ContractTypeService contractTypeService;

    @GetMapping("/contractType/addContractType")
    Message addContractType(@RequestParam("name") String name){
        return contractTypeService.addContractType(name);
    }
    @GetMapping("/contractType/getContractType")
    Message getContractType(){
        return contractTypeService.getContractType();
    }

    @GetMapping("/contractType/renameContractType")
    Message renameContractType(@RequestParam("id") int id,@RequestParam("name") String name){
        return contractTypeService.renameContractType(id,name);
    }
}

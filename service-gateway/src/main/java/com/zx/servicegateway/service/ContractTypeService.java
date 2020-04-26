package com.zx.servicegateway.service;

import com.zx.servicegateway.pojo.Message;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-contract")
@Repository
public interface ContractTypeService {


    @GetMapping("/contractType/addContractType")
    Message addContractType(@RequestParam("name") String name);

    @GetMapping("/contractType/getContractType")
    Message getContractType();

    @GetMapping("/contractType/renameContractType")
    Message renameContractType(@RequestParam("id") int id, @RequestParam("name") String name);

}

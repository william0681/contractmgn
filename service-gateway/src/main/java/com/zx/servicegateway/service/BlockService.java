package com.zx.servicegateway.service;

import com.zx.servicegateway.pojo.Message;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-contract")
@Repository
public interface BlockService {

    @RequestMapping("/block/addBlock")
    Message addBlock(@RequestParam("name") String name);

    @RequestMapping("/block/getBlock")
    Message getBlock();

    @RequestMapping("/block/renameBlock")
    Message renameBlock(@RequestParam("id") int id,@RequestParam("name") String name);

    @RequestMapping("/block/setBlockResponser")
    Message setBlockResponser(@RequestParam("id") int id,@RequestParam("responser") int responser);

    @RequestMapping("/block/setBlockOperator")
    Message setBlockOperator(@RequestParam("id") int id,@RequestParam("operator") int operator);
}

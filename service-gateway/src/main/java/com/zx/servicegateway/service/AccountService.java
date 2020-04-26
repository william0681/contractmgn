package com.zx.servicegateway.service;

import com.zx.servicegateway.pojo.Message;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@FeignClient("service-finance")
public interface AccountService {

    @GetMapping("/account/getAcList")
    Message getAcList(@RequestParam("contractId") Integer contractId, @RequestParam("pageNum") Integer pageNum, @RequestParam("record") Integer record);

    @GetMapping("/account/getReList")
    Message getReList(@RequestParam("contractId") Integer contractId,@RequestParam("pageNum") Integer pageNum,@RequestParam("record") Integer record);

    @GetMapping("/account/getAccountInfo")
    Message getAccountInfo(@RequestParam("pageNum") Integer pageNum,@RequestParam("record") Integer record,@RequestParam("key") String key,@RequestParam("start") Date start,@RequestParam("end") Date end,@RequestParam("userId") Integer userId);

    @GetMapping("/account/updateContractPeriod")
    Message updateContractPeriod();
}

package com.zx.servicefinance.controller;

import com.zx.servicefinance.pojo.Message;
import com.zx.servicefinance.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/account/getAcList")
    Message getAcList(@RequestParam("contractId") Integer contractId,@RequestParam("pageNum") Integer pageNum,@RequestParam("record") Integer record){
        return accountService.getAcList(contractId, pageNum, record);
    }

    @GetMapping("/account/getReList")
    Message getReList(@RequestParam("contractId") Integer contractId,@RequestParam("pageNum") Integer pageNum,@RequestParam("record") Integer record){
        return accountService.getReList(contractId, pageNum, record);

    }

    @GetMapping("/account/getAccountInfo")
    Message getAccountInfo(@RequestParam("pageNum") Integer pageNum,@RequestParam("record") Integer record,@RequestParam(value = "key",required = false) String key,@RequestParam(value = "start",required = false) Date start,@RequestParam(value = "end",required = false) Date end,@RequestParam(value = "userId",required = false) Integer userId){
        return accountService.getAccountInfo(pageNum, record, key, start, end, userId);
    }

    @GetMapping("/account/updateContractPeriod")
    Message updateContractPeriod(){
        return accountService.updateContractPeriod();
    }
}

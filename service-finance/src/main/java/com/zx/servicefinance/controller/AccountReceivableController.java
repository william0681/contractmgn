package com.zx.servicefinance.controller;

import com.zx.servicefinance.model.AccountReceivable;
import com.zx.servicefinance.pojo.Message;
import com.zx.servicefinance.service.AccountReceivableService;
import com.zx.servicefinance.vo.OperationNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
public class AccountReceivableController {

    @Autowired
    AccountReceivableService accountReceivableService;

    @PostMapping("/accountReceivable/addOperationAccountReceivable")
    Message addOperationAccountReceivable(@RequestBody OperationNode operationNode,@RequestParam("userId") int userId) throws ParseException{
        return accountReceivableService.addOperationAccountReceivable(operationNode, userId);
    }

    @PostMapping("/accountReceivable/addCommonAccountReceivable")
    Message addCommonAccountReceivable(@RequestBody AccountReceivable accountReceivable,@RequestParam("userId") int userId) throws ParseException{
        return accountReceivableService.addCommonAccountReceivable(accountReceivable, userId);
    }

    @GetMapping("/accountReceivable/getNodeInfo")
    Message getNodeInfo(@RequestParam("nodeId") Integer nodeId){
        return accountReceivableService.getNodeInfo(nodeId);
    }

    @GetMapping("/accountReceivable/getOperationReInfo")
    Message getOperationReInfo(@RequestParam("contractId") Integer contractId){
        return accountReceivableService.getOperationReInfo(contractId);
    }

    @GetMapping("/accountReceivable/deleteCommonAccountRe")
    Message deleteCommonAccountRe(@RequestParam("accountReId") Integer accountReId,@RequestParam("nodeId") Integer nodeId){
        return accountReceivableService.deleteCommonAccountRe(accountReId, nodeId);
    }

    @GetMapping("/accountReceivable/deleteOperationAccountRe")
    Message deleteOperationAccountRe(@RequestParam("nodeId") Integer nodeId){
        return accountReceivableService.deleteOperationAccountRe(nodeId);
    }

    @PostMapping("/accountReceivable/updateCommonAccountRe")
    Message updateCommonAccountRe(@RequestBody AccountReceivable accountReceivable){
        return accountReceivableService.updateCommonAccountRe(accountReceivable);
    }
}

package com.zx.servicegateway.service;


import com.zx.servicegateway.model.AccountReceivable;
import com.zx.servicegateway.pojo.Message;
import com.zx.servicegateway.vo.OperationNode;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Service
@FeignClient("service-finance")
public interface AccountReceivableService {

    @RequestMapping(value = "/accountReceivable/addOperationAccountReceivable",method = RequestMethod.POST)
    Message addOperationAccountReceivable(@RequestBody OperationNode operationNode,@RequestParam("userId") int userId) throws ParseException;

    @RequestMapping(value = "/accountReceivable/addCommonAccountReceivable",method = RequestMethod.POST)
    Message addCommonAccountReceivable(@RequestBody AccountReceivable accountReceivable, @RequestParam("userId") int userId) throws ParseException;

    @GetMapping("/accountReceivable/getNodeInfo")
    Message getNodeInfo(@RequestParam("nodeId") Integer nodeId);

    @GetMapping("/accountReceivable/getOperationReInfo")
    Message getOperationReInfo(@RequestParam("contractId") Integer contractId);

    @GetMapping("/accountReceivable/deleteCommonAccountRe")
    Message deleteCommonAccountRe(@RequestParam("accountReId") Integer accountReId,@RequestParam("nodeId") Integer nodeId);

    @GetMapping("/accountReceivable/deleteOperationAccountRe")
    Message deleteOperationAccountRe(@RequestParam("nodeId") Integer nodeId);

    @RequestMapping(value = "/accountReceivable/updateCommonAccountRe",method = RequestMethod.POST)
    Message updateCommonAccountRe(@RequestBody AccountReceivable accountReceivable);

//    List<AccountActual> getAccountActualByContractIId();

}

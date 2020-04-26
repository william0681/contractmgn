package com.zx.servicegateway.service;

import com.zx.servicegateway.model.ExecuteNode;
import com.zx.servicegateway.pojo.Message;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@FeignClient("service-contract")
@Repository
public interface ExecuteNodeService {

     @RequestMapping(value = "/executeNode/addExecuteNode",method = RequestMethod.POST)
     Message addExecuteNode(@RequestBody ExecuteNode executeNode, @RequestParam("userId") int userId);

     @GetMapping("/executeNode/remindExecuteNode")
     Message remindExecuteNode();

     @GetMapping("/executeNode/showExecuteNode")
     Message showExecuteNode(@RequestParam("id") Integer id);

}

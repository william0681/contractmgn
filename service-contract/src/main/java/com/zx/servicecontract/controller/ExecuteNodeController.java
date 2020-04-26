package com.zx.servicecontract.controller;

import com.zx.servicecontract.model.ExecuteNode;
import com.zx.servicecontract.pojo.Message;
import com.zx.servicecontract.service.ExecuteNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ExecuteNodeController {

    @Autowired
    ExecuteNodeService executeNodeService;

    @RequestMapping(value = "/executeNode/addExecuteNode",method = RequestMethod.POST)
    Message addExecuteNode(@RequestBody ExecuteNode executeNode,@RequestParam("userId") int userId){
        return executeNodeService.addExecuteNode(executeNode, userId);
    }

    @GetMapping("/executeNode/remindExecuteNode")
    Message remindExecuteNode(){
        return executeNodeService.remindExecuteNode();
    }

    @GetMapping("/executeNode/showExecuteNode")
    Message showExecuteNode(@RequestParam("id") Integer id){
        return executeNodeService.showExecuteNode(id);
    }
}

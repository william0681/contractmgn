package com.zx.servicegateway.controller;

import com.zx.servicegateway.pojo.Message;
import com.zx.servicegateway.service.BlockService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BlockController {

    @Autowired
    BlockService blockService;

    @PostMapping("block")
    @RequiresRoles(value = {"manager", "accountant", "overallchief"}, logical = Logical.OR)
    public Message addBlock(@RequestParam String name){
        return blockService.addBlock(name);
    }

    @GetMapping("block")
    @RequiresRoles(value = {"manager", "accountant", "overallchief","areachief","saler","buyer","operator"}, logical = Logical.OR)
    public Message getBlock(){
        return blockService.getBlock();
    }

    @PutMapping("block")
    @RequiresRoles(value = {"manager", "accountant", "overallchief"}, logical = Logical.OR)
    public Message renameBlock(@RequestParam int id, @RequestParam String name){
        return blockService.renameBlock(id, name);
    }

    @GetMapping("blockUser")
    @RequiresRoles(value = {"manager", "accountant", "overallchief"}, logical = Logical.OR)
    public Message setBlockResponser(@RequestParam int id, @RequestParam int responser){
        return blockService.setBlockResponser(id, responser);
    }

    @GetMapping("blockOperator")
    @RequiresRoles(value = {"manager", "accountant", "overallchief"}, logical = Logical.OR)
    public Message setBlockOperator(@RequestParam int id, @RequestParam int operator){
        return blockService.setBlockOperator(id, operator);
    }


}

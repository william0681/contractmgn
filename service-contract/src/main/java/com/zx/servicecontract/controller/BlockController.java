package com.zx.servicecontract.controller;

import com.zx.servicecontract.pojo.Message;
import com.zx.servicecontract.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlockController {

    @Autowired
    BlockService blockService;

    @GetMapping("/block/addBlock")
    Message addBlock(@RequestParam String name){
        return blockService.addBlock(name);
    }

    @GetMapping("/block/getBlock")
    Message getBlock(){
        return blockService.getBlock();
    }

    @GetMapping("/block/renameBlock")
    Message renameBlock(@RequestParam int id,@RequestParam String name){
        return blockService.renameBlock(id, name);
    }

    @GetMapping("/block/setBlockResponser")
    Message setBlockResponser(@RequestParam int id,@RequestParam int responser){
        return blockService.setBlockResponser(id, responser);
    }

    @GetMapping("/block/setBlockOperator")
    Message setBlockOperator(@RequestParam int id,@RequestParam int operator){
        return blockService.setBlockOperator(id, operator);
    }
}

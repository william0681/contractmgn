package com.zx.servicegateway.controller;

import com.zx.servicegateway.pojo.Message;
import com.zx.servicegateway.service.ContentTypeService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ContentTypeController {

    @Autowired
    ContentTypeService contentTypeService;

    @PostMapping("contentType")
    @RequiresRoles(value = {"manager","accountant","overallchief"},logical = Logical.OR)
    public Message addContentType(@RequestParam int contractTypeId, @RequestParam String name){
        return contentTypeService.addContentType(contractTypeId, name);
    }

    @GetMapping("contentType")
    public Message getContentType(@RequestParam int contractTypeId){
        return contentTypeService.getContentType(contractTypeId);
    }

    @PutMapping("contentType")
    @RequiresRoles(value = {"manager","accountant","overallchief"},logical = Logical.OR)
    public Message renameContentType(@RequestParam int id, @RequestParam String name){
        return contentTypeService.renameContentType(id, name);
    }
}

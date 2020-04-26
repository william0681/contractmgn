package com.zx.servicecontract.controller;

import com.zx.servicecontract.pojo.Message;
import com.zx.servicecontract.service.ContentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContentTypeController {

    @Autowired
    ContentTypeService contentTypeService;

    @GetMapping("/contentType/addContentType")
    Message addContentType(@RequestParam int contractTypeId,@RequestParam String name){
        return contentTypeService.addContentType(contractTypeId,name);
    }
    @GetMapping("/contentType/getContentType")
    Message getContentType(@RequestParam int contractTypeId){
        return contentTypeService.getContentType(contractTypeId);
    }
    @GetMapping("/contentType/renameContentType")
    Message renameContentType(@RequestParam int id,@RequestParam String name){
        return contentTypeService.renameContentType(id,name);
    }
}

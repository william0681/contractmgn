package com.zx.servicegateway.service;

import com.zx.servicegateway.pojo.Message;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-contract")
@Repository
public interface ContentTypeService {

    @RequestMapping("/contentType/addContentType")
    Message addContentType(@RequestParam("contractTypeId") int contractTypeId,@RequestParam("name") String name);

    @RequestMapping("/contentType/getContentType")
    Message getContentType(@RequestParam("contractTypeId") int contractTypeId);

    @RequestMapping("/contentType/renameContentType")
    Message renameContentType(@RequestParam("id") int id,@RequestParam("name") String name);
}

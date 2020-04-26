package com.zx.servicegateway.service;

import com.zx.servicegateway.pojo.Message;
import org.apache.shiro.subject.Subject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.Date;

@FeignClient("service-finance")
@Repository
public interface CommissionService {

    @RequestMapping(value = "/commission/getCommissionList",method = RequestMethod.GET)
    Message getCommissionList(@RequestParam("pageNum") int pageNum,@RequestParam("record") int record,@RequestParam(value = "key",required = false) String key,@RequestParam(value = "startTime",required = false) String startTime,@RequestParam(value = "endTime",required = false) String endTime,@RequestParam("role") String role,@RequestParam("userId") int userId) throws ParseException;

    @RequestMapping(value = "/commission/insertCommission",method = RequestMethod.GET)
    void insertCommission(@RequestParam("contractId") int contractId,@RequestParam("actualId") int actualId,@RequestParam("percentage") int percentage,@RequestParam("dueDate") Date dueDate,@RequestParam("salerId") int salerId);

    @RequestMapping(value = "/commission/confirmCommission",method = RequestMethod.GET)
    Message confirmCommission(@RequestParam("id") int id,@RequestParam("finishDate") String finishDate,@RequestParam("remark") String remark) throws ParseException;

    @RequestMapping(value = "/commission/getCommissionInfo",method = RequestMethod.GET)
    Message getCommissionInfo(@RequestParam("id") int id);
}

package com.zx.servicefinance.controller;

import com.zx.servicefinance.pojo.Message;
import com.zx.servicefinance.service.CommissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Date;

@RestController
public class CommissionController {

    @Autowired
    CommissionService commissionService;

    @RequestMapping(value = "/commission/getCommissionList",method = RequestMethod.GET)
    Message getCommissionList(@RequestParam("pageNum") int pageNum, @RequestParam("record") int record, @RequestParam(value = "key",required = false) String key, @RequestParam(value = "startTime",required = false) String startTime, @RequestParam(value = "endTime",required = false) String endTime, @RequestParam("role") String role, @RequestParam("userId") int userId) throws ParseException{
        return commissionService.getCommissionList(pageNum, record, key, startTime, endTime, role, userId);
    }

    @RequestMapping(value = "/commission/insertCommission",method = RequestMethod.GET)
    void insertCommission(@RequestParam("contractId") int contractId, @RequestParam("actualId") int actualId, @RequestParam("percentage") int percentage, @RequestParam("dueDate") Date dueDate, @RequestParam("salerId") int salerId){
        commissionService.insertCommission(contractId, actualId, percentage, dueDate, salerId);
    }

    @RequestMapping(value = "/commission/confirmCommission",method = RequestMethod.GET)
    Message confirmCommission(@RequestParam("id") int id,@RequestParam("finishDate") String finishDate,@RequestParam("remark") String remark) throws ParseException{
        return commissionService.confirmCommission(id, finishDate, remark);
    }

    @RequestMapping(value = "/commission/getCommissionInfo",method = RequestMethod.GET)
    Message getCommissionInfo(@RequestParam("id") int id){
        return commissionService.getCommissionInfo(id);
    }
}

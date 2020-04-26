package com.zx.servicegateway.controller;

import com.zx.servicegateway.pojo.Message;
import com.zx.servicegateway.service.CommissionService;
import com.zx.servicegateway.util.RoleUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
public class CommissionController {

    @Autowired
    CommissionService commissionService;

    @GetMapping("commission")
    @RequiresRoles(value = {"manager", "accountant", "overallchief","areachief"}, logical = Logical.OR)
    public Message getCommissionList(@RequestParam int pageNum, @RequestParam String key, @RequestParam String startTime, @RequestParam String endTime, @RequestParam int record) throws ParseException {
        Subject subject = SecurityUtils.getSubject();
        return commissionService.getCommissionList(pageNum,record,key,startTime,endTime, RoleUtil.getRole(subject),(int)subject.getSession().getAttribute("userId"));
    }

    @PutMapping("commission")
    @RequiresRoles(value = {"manager", "accountant", "overallchief"}, logical = Logical.OR)
    public Message confirmCommission(@RequestParam int id, @RequestParam String finishDate, @RequestParam String remark) throws ParseException{
        return commissionService.confirmCommission(id, finishDate, remark);
    }

    @GetMapping("commission/{id}")
    @RequiresRoles(value = {"manager", "accountant", "overallchief"}, logical = Logical.OR)
    public Message getCommissionInfo(@PathVariable(value = "id") int id){
        return commissionService.getCommissionInfo(id);
    }

}

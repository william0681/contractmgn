package com.zx.servicegateway.controller;

import com.zx.servicegateway.pojo.Message;
import com.zx.servicegateway.service.ReminderService;
import com.zx.servicegateway.util.RoleUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReminderController {

    @Autowired
    ReminderService reminderService;

    @GetMapping("reminder")
    @RequiresRoles(value = {"manager", "accountant", "overallchief","areachief","buyer","operator"}, logical = Logical.OR)
    public Message getReminder(@RequestParam int type, @RequestParam int pageNum, @RequestParam int record){
        Subject subject =  SecurityUtils.getSubject();
        return reminderService.getReminder(type,pageNum,record,(int)subject.getSession().getAttribute("userId"), RoleUtil.getRole(subject));
    }

    @GetMapping("reminder/{id}")
    @RequiresRoles(value = {"manager", "accountant", "overallchief","areachief","buyer","operator"}, logical = Logical.OR)
    public Message getReminder(@PathVariable(value = "id") int id){
        Subject subject = SecurityUtils.getSubject();
        return reminderService.readReminderInContract(id,RoleUtil.getRole(subject));
    }

    @GetMapping("reminder/number")
    @RequiresRoles(value = {"manager", "accountant", "overallchief","areachief","buyer","operator"}, logical = Logical.OR)
    public Message countUnreadReminder(){
        Subject subject =  SecurityUtils.getSubject();
        return reminderService.countUnreadReminder((int)subject.getSession().getAttribute("userId"), RoleUtil.getRole(subject));
    }

    @GetMapping("reminder/all")
    @RequiresRoles(value = {"manager", "accountant", "overallchief","areachief","buyer","operator"}, logical = Logical.OR)
    public Message readAll(@RequestParam int type){
        Subject subject =  SecurityUtils.getSubject();
        return reminderService.readAllReminder(type,RoleUtil.getRole(subject));
    }

    @GetMapping("reminder/read")
    @RequiresRoles(value = {"manager", "accountant", "overallchief","areachief","buyer","operator"}, logical = Logical.OR)
    public Message readSingleReminder(@RequestParam int id){
        Subject subject = SecurityUtils.getSubject();
        return reminderService.readSingleReminder(id,RoleUtil.getRole(subject));
    }
}

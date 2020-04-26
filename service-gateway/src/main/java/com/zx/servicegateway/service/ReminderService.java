package com.zx.servicegateway.service;

import com.zx.servicegateway.pojo.Message;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@FeignClient("service-reminder")
@Repository
public interface ReminderService {

    @RequestMapping("/getReminder")
    Message getReminder(@RequestParam("type") int type, @RequestParam("pageNum") int pageNum, @RequestParam("record") int record,@RequestParam("userId") int userId,@RequestParam("role") String role);

    @RequestMapping("/readReminderInContract")
    Message readReminderInContract(@RequestParam("id") int id,@RequestParam("role") String role);

    @RequestMapping("/readAllReminder")
    Message readAllReminder(@RequestParam("type") int type,@RequestParam("role") String role);

    @RequestMapping("/insertReminder")
    void insertReminder(@RequestParam("contractId") int contractId,@RequestParam("type") int type,@RequestParam("operateType") String operateType,@RequestParam("userId") int userId);

    @RequestMapping("/countUnreadReminder")
    Message countUnreadReminder(@RequestParam("userId") int userId,@RequestParam("role") String role);

    @RequestMapping("/readSingleReminder")
    Message readSingleReminder(@RequestParam("id") int id,@RequestParam("role") String role);

}

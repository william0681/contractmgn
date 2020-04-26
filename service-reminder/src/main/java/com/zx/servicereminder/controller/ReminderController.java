package com.zx.servicereminder.controller;

import com.zx.servicereminder.pojo.Message;
import com.zx.servicereminder.service.ReminderService;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReminderController {

    @Autowired
    ReminderService reminderService;

    @GetMapping("/getReminder")
    Message getReminder(@RequestParam("type") int type, @RequestParam("pageNum") int pageNum, @RequestParam("record") int record,@RequestParam("userId") int userId,@RequestParam("role") String role){
        Message message = reminderService.getReminder(type, pageNum, record, userId,role);

        return message;
    }
    @GetMapping("/readReminderInContract")
    Message readReminderInContract(@RequestParam("id") int id,@RequestParam("role") String role){
        return reminderService.readReminderInContract(id, role);
    }

    @GetMapping("/readAllReminder")
    Message readAllReminder(@RequestParam("type") int type,@RequestParam("role") String role){
        return reminderService.readAllReminder(type, role);
    }

    @GetMapping("/insertReminder")
    void insertReminder(@RequestParam("contractId") int contractId,@RequestParam("type") int type,@RequestParam("operateType") String operateType,@RequestParam("userId") int userId){
        reminderService.insertReminder(contractId, type, operateType, userId);
    }

    @GetMapping("/countUnreadReminder")
    Message countUnreadReminder(@RequestParam("userId") int userId,@RequestParam("role") String role){
        return reminderService.countUnreadReminder(userId, role);
    }

    @GetMapping("/readSingleReminder")
    Message readSingleReminder(@RequestParam("id") int id,@RequestParam("role") String role){
        return reminderService.readSingleReminder(id,role);
    }


}

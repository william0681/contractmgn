package com.zx.serviceschedule.controller;

import com.zx.serviceschedule.service.ReceivableScheduleTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScheduleController {

    @Autowired
    ReceivableScheduleTask receivableScheduleTask;

    @RequestMapping("/checkoutReceivableStatus")
    public void checkoutReceivableStatus(){
        receivableScheduleTask.checkoutReceivableStatus();
    }

    @RequestMapping("/checkCommissionStatus")
    public void checkCommissionStatus(){
        receivableScheduleTask.checkCommissionStatus();
    }
}

package com.zx.servicegateway.schedule;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "service-schedule")
@Repository
public interface ReceivableScheduleTask {

    @RequestMapping("/checkoutReceivableStatus")
    void checkoutReceivableStatus();

    @RequestMapping("/checkCommissionStatus")
    void checkCommissionStatus();
}

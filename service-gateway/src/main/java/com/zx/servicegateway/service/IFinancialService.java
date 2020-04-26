package com.zx.servicegateway.service;

import com.zx.servicegateway.model.Financial;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * IFinancialService
 */
@FeignClient("service-finance")
public interface IFinancialService {

    @RequestMapping(value = "/finance/showMoney",method = RequestMethod.GET)
    Financial showMoney(@RequestParam("id") Integer id);

}
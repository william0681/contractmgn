package com.zx.servicefinance.controller;

import com.zx.servicefinance.model.Financial;
import com.zx.servicefinance.service.FinancialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FinanceController {

    @Autowired
    FinancialService financialService;

    @RequestMapping(value = "/finance/showMoney",method = RequestMethod.GET)
    Financial showMoney(@RequestParam("id") Integer id){
        return financialService.showMoney(id);
    }

}

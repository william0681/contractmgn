package com.zx.servicegateway.controller;

import com.zx.servicegateway.model.Financial;
import com.zx.servicegateway.pojo.Message;
import com.zx.servicegateway.service.IFinancialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * FinancialController
 */
@RestController
@RequestMapping("/finance")
@Api(description = "财务相关操作")
public class FinancialController {
    @Autowired
    private IFinancialService financialService;

    @ApiOperation("显示合同总额和剩余的钱")
    @GetMapping("money")
    public Message showMoney(@RequestParam("contract_id") Integer id) {
        Financial fc = financialService.showMoney(id);
        return fc == null ? Message.createErr(-1, "改合同没有财务信息") : Message.createSuc(fc);
    }

    @ApiOperation("测试response")
    @PostMapping(value = "get_response")
    public String postMethodName(String entity, HttpServletResponse response) {
        System.out.println(response.getHeaderNames());
        return entity;
    }

}
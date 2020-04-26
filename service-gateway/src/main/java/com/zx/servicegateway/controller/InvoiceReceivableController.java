package com.zx.servicegateway.controller;

import com.zx.servicegateway.model.InvoiceReceivable;
import com.zx.servicegateway.pojo.Message;
import com.zx.servicegateway.service.InvoiceReceivableService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class InvoiceReceivableController {
    @Autowired
    private InvoiceReceivableService invoiceReceivableService;

    /**
     * 获取合同总金额和应开票剩余金额
     *
     * @param contractId
     * @return
     */
    @RequiresRoles(value = {"manager", "accountant", "overallchief", "areachief","operator"}, logical = Logical.OR)
    @ApiOperation("获取合同总金额和应开票剩余金额")
    @GetMapping("invoiceReceivable")
    public Message getInvoiceMoney(@RequestParam("contractId") Integer contractId) {
        return invoiceReceivableService.getInvoiceReInfo(contractId);

    }

    /**
     * 添加应开票
     *
     * @param invoiceReceivable
     * @return
     */
    @RequiresRoles(value = {"manager", "accountant", "overallchief", "areachief","operator"}, logical = Logical.OR)
    @ApiOperation("添加应开票")
    @PostMapping("invoiceReceivable")
    public Message addInvoiceRe(@RequestBody InvoiceReceivable invoiceReceivable) {
        Subject subject = SecurityUtils.getSubject();

        return invoiceReceivableService.addInvoiceReceivable(invoiceReceivable,(int)subject.getSession().getAttribute("userId"));
    }

    /**
     * 删除应开票
     *
     * @param invoiceId
     * @return
     */
    @RequiresRoles(value = {"manager", "accountant", "overallchief", "areachief","operator"}, logical = Logical.OR)
    @ApiOperation("删除应开票")
    @DeleteMapping("invoiceReceivable")
    public Message deleteInvoiceRe(@RequestParam("invoiceReId") Integer invoiceId) {
        return invoiceReceivableService.deleteInvoiceRe(invoiceId);
    }

    /**
     * 更新应开票
     * @param invoiceReceivable
     * @return
     */
    @RequiresRoles(value = {"manager", "accountant", "overallchief", "areachief","operator"}, logical = Logical.OR)
    @ApiOperation("更新应开票")
    @PutMapping("invoiceReceivable")
    public Message updateInvoiceRe(@RequestBody InvoiceReceivable invoiceReceivable) {
        return invoiceReceivableService.updateInvoiceRe(invoiceReceivable);
    }


}

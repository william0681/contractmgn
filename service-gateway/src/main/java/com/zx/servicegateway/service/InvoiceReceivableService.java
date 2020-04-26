package com.zx.servicegateway.service;

import com.zx.servicegateway.model.InvoiceReceivable;
import com.zx.servicegateway.pojo.Message;
import org.apache.shiro.subject.Subject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-finance")
public interface InvoiceReceivableService {

    @RequestMapping(value = "invoiceReceivable/getInvoiceReInfo",method = RequestMethod.GET)
    Message getInvoiceReInfo(@RequestParam("contractId") Integer contractId);//获取合同总额以及剩余开票金额

    @RequestMapping(value = "invoiceReceivable/addInvoiceReceivable",method = RequestMethod.POST)
    Message addInvoiceReceivable(@RequestBody InvoiceReceivable invoiceReceivable,@RequestParam("userId") int userId);//添加应开票

    @RequestMapping(value = "invoiceReceivable/deleteInvoiceRe",method = RequestMethod.GET)
    Message deleteInvoiceRe(@RequestParam("invoiceReId") Integer invoiceReId);//删除应开票

    @RequestMapping(value = "invoiceReceivable/updateInvoiceRe",method = RequestMethod.POST)
    Message updateInvoiceRe(@RequestBody InvoiceReceivable invoiceReceivable);//更新应开票
}

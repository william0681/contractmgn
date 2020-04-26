package com.zx.servicefinance.controller;

import com.zx.servicefinance.model.InvoiceReceivable;
import com.zx.servicefinance.pojo.Message;
import com.zx.servicefinance.service.InvoiceReceivableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class InvoiceReceivableController {

    @Autowired
    InvoiceReceivableService invoiceReceivableService;

    @RequestMapping(value = "invoiceReceivable/getInvoiceReInfo",method = RequestMethod.GET)
    Message getInvoiceReInfo(@RequestParam("contractId") Integer contractId){
        return invoiceReceivableService.getInvoiceReInfo(contractId);
    }

    @RequestMapping(value = "invoiceReceivable/addInvoiceReceivable",method = RequestMethod.POST)
    Message addInvoiceReceivable(@RequestBody InvoiceReceivable invoiceReceivable, @RequestParam("userId") int userId){
        return invoiceReceivableService.addInvoiceReceivable(invoiceReceivable, userId);
    }

    @RequestMapping(value = "invoiceReceivable/deleteInvoiceRe",method = RequestMethod.GET)
    Message deleteInvoiceRe(@RequestParam("invoiceReId") Integer invoiceReId){
        return invoiceReceivableService.deleteInvoiceRe(invoiceReId);
    }

    @RequestMapping(value = "invoiceReceivable/updateInvoiceRe",method = RequestMethod.POST)
    Message updateInvoiceRe(@RequestBody InvoiceReceivable invoiceReceivable){
        return invoiceReceivableService.updateInvoiceRe(invoiceReceivable);
    }
}

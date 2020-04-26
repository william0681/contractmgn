package com.zx.servicefinance.controller;

import com.zx.servicefinance.model.ActuralReceiptList;
import com.zx.servicefinance.model.ActuralTicket;
import com.zx.servicefinance.model.ReceiptModel;
import com.zx.servicefinance.pojo.Message;
import com.zx.servicefinance.service.ActuralReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ActualReceiptController {

    @Autowired
    ActuralReceiptService acturalReceiptService;

    @RequestMapping(value = "/acturalReceipt/deleteActualTicket",method = RequestMethod.GET)
    Message deleteActualTicket(@RequestParam("actualticketid") Integer actualticketid){
        return acturalReceiptService.deleteActualTicket(actualticketid);
    }

    @RequestMapping(value = "/acturalReceipt/deleteReceipt",method = RequestMethod.GET)
    Integer deleteReceipt(@RequestParam("actrualId") Integer actrualId){
        return acturalReceiptService.deleteReceipt(actrualId);
    }

    @RequestMapping(value = "/acturalReceipt/getReceiptList",method = RequestMethod.GET)
    ActuralReceiptList getReceiptList(@RequestParam("id") Integer id){
        return acturalReceiptService.getReceiptList(id);
    }

    @RequestMapping(value = "/acturalReceipt/isPlan",method = RequestMethod.GET)
    Double isPlan(@RequestParam("contractId") Integer contractId){
        return acturalReceiptService.isPlan(contractId);
    }

    @RequestMapping(value = "/acturalReceipt/isPlan",method = RequestMethod.POST)
    Integer insertReceipt(@RequestBody ReceiptModel model, @RequestParam("opratorTag") Integer opratorTag, @RequestParam("userId") int userId){
        return acturalReceiptService.insertReceipt(model, opratorTag, userId);
    }

    @RequestMapping(value = "/acturalReceipt/getAcuralTicket",method = RequestMethod.GET)
    List<Map<String, Object>> getAcuralTicket(@RequestParam("id") Integer id){
        return acturalReceiptService.getAcuralTicket(id);
    }

    @RequestMapping(value = "/acturalReceipt/addActralTicket",method = RequestMethod.POST)
    Integer addActralTicket(@RequestBody ActuralTicket model, @RequestParam("userId") int userId){
        return acturalReceiptService.addActralTicket(model, userId);
    }

    @RequestMapping(value = "/acturalReceipt/isPlanTicket",method = RequestMethod.GET)
    Double isPlanTicket(@RequestParam("contractId") Integer contractId) {
        return acturalReceiptService.isPlanTicket(contractId);
    }

    @RequestMapping(value = "/acturalReceipt/getTicketList",method = RequestMethod.GET)
    List<Map<String, Object>> getTicketList(@RequestParam(value = "key",required = false) String key, @RequestParam(value = "startDate",required = false) Date startDate, @RequestParam(value = "endDate",required = false) Date endDate, @RequestParam(value = "blockId",required = false) Integer blockId){
        return acturalReceiptService.getTicketList(key, startDate, endDate, blockId);
    }

    @RequestMapping(value = "/acturalReceipt/getTicketDetail",method = RequestMethod.GET)
    List<Map<String, Object>> getTicketDetail(@RequestParam("contractId") Integer contractId){
        return acturalReceiptService.getTicketDetail(contractId);
    }

    @RequestMapping(value = "/acturalReceipt/getActralDetail",method = RequestMethod.GET)
    List<Map<String, Object>> getActralDetail(@RequestParam("id") Integer id){
        return acturalReceiptService.getActralDetail(id);
    }

    @RequestMapping(value = "/acturalReceipt/getOutput",method = RequestMethod.GET)
    List<LinkedHashMap<String, Object>> getOutput(@RequestParam("blockId") Integer blockId, @RequestParam("contractTypeId") Integer contractTypeId, @RequestParam("sql") String sql, @RequestParam("startDate") String startDate,
                                                  @RequestParam("endDate") String endDate, @RequestParam("changeStartDate") String changeStartDate, @RequestParam("changeEndDate") String changeEndDate){
        return acturalReceiptService.getOutput(blockId, contractTypeId, sql, startDate, endDate, changeStartDate, changeEndDate);
    }

    @RequestMapping(value = "/acturalReceipt/getNodeList",method = RequestMethod.GET)
    List<Map<String, Object>> getNodeList(@RequestParam("contractId") Integer contractId){
        return acturalReceiptService.getNodeList(contractId);
    }
}

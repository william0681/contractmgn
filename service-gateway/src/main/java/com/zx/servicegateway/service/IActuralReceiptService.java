package com.zx.servicegateway.service;

import com.zx.servicegateway.model.ActuralReceiptList;
import com.zx.servicegateway.model.ActuralTicket;
import com.zx.servicegateway.model.ReceiptModel;
import com.zx.servicegateway.pojo.Message;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * IActuralReceiptService
 */
@Repository
@FeignClient("service-finance")
public interface IActuralReceiptService {

	@RequestMapping(value = "/acturalReceipt/deleteActualTicket",method = RequestMethod.GET)
	Message deleteActualTicket(@RequestParam("actualticketid") Integer actualticketid);//min,删除实开票;

	@RequestMapping(value = "/acturalReceipt/deleteReceipt",method = RequestMethod.GET)
	Integer deleteReceipt(@RequestParam("actrualId") Integer actrualId);//删除实收款，min

	@RequestMapping(value = "/acturalReceipt/getReceiptList",method = RequestMethod.GET)
	ActuralReceiptList getReceiptList(@RequestParam("id") Integer id);

	@RequestMapping(value = "/acturalReceipt/isPlan",method = RequestMethod.GET)
	Double isPlan(@RequestParam("contractId") Integer contractId);

	@RequestMapping(value = "/acturalReceipt/isPlan",method = RequestMethod.POST)
	Integer insertReceipt(@RequestBody ReceiptModel model,@RequestParam("opratorTag") Integer opratorTag,@RequestParam("userId") int userId);

	@RequestMapping(value = "/acturalReceipt/getAcuralTicket",method = RequestMethod.GET)
	List<Map<String, Object>> getAcuralTicket(@RequestParam("id") Integer id);

	@RequestMapping(value = "/acturalReceipt/addActralTicket",method = RequestMethod.POST)
	Integer addActralTicket(@RequestBody ActuralTicket model,@RequestParam("userId") int userId);

	@RequestMapping(value = "/acturalReceipt/isPlanTicket",method = RequestMethod.GET)
	Double isPlanTicket(@RequestParam("contractId") Integer contractId);

	@RequestMapping(value = "/acturalReceipt/getTicketList",method = RequestMethod.GET)
	List<Map<String, Object>> getTicketList(@RequestParam("key") String key,@RequestParam("startDate") Date startDate,@RequestParam("endDate") Date endDate,@RequestParam("blockId") Integer blockId);

	@RequestMapping(value = "/acturalReceipt/getTicketDetail",method = RequestMethod.GET)
	List<Map<String, Object>> getTicketDetail(@RequestParam("contractId") Integer contractId);

	@RequestMapping(value = "/acturalReceipt/getActralDetail",method = RequestMethod.GET)
	List<Map<String, Object>> getActralDetail(@RequestParam("id") Integer id);

	@RequestMapping(value = "/acturalReceipt/getOutput",method = RequestMethod.GET)
	List<LinkedHashMap<String, Object>> getOutput(@RequestParam("blockId") Integer blockId,@RequestParam("contractTypeId") Integer contractTypeId,@RequestParam("sql") String sql,@RequestParam("startDate") String startDate,
                                                  @RequestParam("endDate") String endDate,@RequestParam("changeStartDate") String changeStartDate,@RequestParam("changeEndDate") String changeEndDate);

	@RequestMapping(value = "/acturalReceipt/getNodeList",method = RequestMethod.GET)
	List<Map<String, Object>> getNodeList(@RequestParam("contractId") Integer contractId);

}
package com.zx.servicefinance.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zx.servicefinance.mapper.ContractDAO;
import com.zx.servicefinance.mapper.IActuralReceiptDAO;
import com.zx.servicefinance.mapper.InvoiceReceivableDAO;
import com.zx.servicefinance.model.InvoiceReceivable;
import com.zx.servicefinance.model.InvoiceReceivableExample;
import com.zx.servicefinance.pojo.Message;
import com.zx.servicefinance.pojo.StatueCode;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InvoiceReceivableService {
    @Autowired
    private ContractDAO contractDAO;
    @Autowired
    private InvoiceReceivableDAO invoiceReceivableDAO;
    @Autowired
    private ReminderService reminderService;

    @Autowired
    private IActuralReceiptDAO acturalReceiptDao;

    @SuppressWarnings("deprecation")
    public Message getInvoiceReInfo(Integer contractId) {
        Double amount = contractDAO.getMoney(contractId);
        Double invoiceMoney = invoiceReceivableDAO.countMoneyByContractId(contractId);//合同剩余应开票金额
        if (invoiceMoney == null)
            invoiceMoney = 0.0;
        Map<String, Object> map = new HashMap<>();
        InvoiceReceivableExample invoiceReceivableExample = new InvoiceReceivableExample();
        invoiceReceivableExample.createCriteria().andContractIdEqualTo(contractId);
        invoiceReceivableExample.setOrderByClause("date");
        List<InvoiceReceivable> invoiceReceivableList = invoiceReceivableDAO.selectByExample(invoiceReceivableExample);
        JSONArray jsonArray = new JSONArray();
        for (InvoiceReceivable invoiceReceivable : invoiceReceivableList) {

            invoiceReceivable.getDate().setHours(8);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("invoiceReId", invoiceReceivable.getId());
            jsonObject.put("invoice_money", invoiceReceivable.getInvoiceMoney());
            jsonObject.put("date", invoiceReceivable.getDate());
            jsonObject.put("remark", invoiceReceivable.getRemark());
            jsonArray.add(jsonObject);
        }
        map.put("contractAmount", amount);
        map.put("resetInvoiceMoney", amount - invoiceMoney);
        map.put("invoiceList", jsonArray);
        return Message.createSuc(map);
    }

    @Transactional
    public Message addInvoiceReceivable(InvoiceReceivable invoiceReceivable,int userId) {
//        if(invoiceReceivable.getDate().before(new Date())){
//            return Message.createErr(StatueCode.DATE_MISTAKE,"所选日期不能早于今天");
//        }
        Byte unstart = 0;
        InvoiceReceivableExample invoiceReceivableExample = new InvoiceReceivableExample();
        invoiceReceivableExample.createCriteria().andContractIdEqualTo(invoiceReceivable.getContractId()).andDateGreaterThan(invoiceReceivable.getDate()).andCompleteTagNotEqualTo(unstart);
        List<InvoiceReceivable> invoiceReceivableList = invoiceReceivableDAO.selectByExample(invoiceReceivableExample);
        if (invoiceReceivableList.size() > 0) {//控制开票日期不能早于已开始或完成的期数时间
            return Message.createErr(StatueCode.DATE_MISTAKE, "所选日期早于应开票进行中日期");
        }
        Integer issue = invoiceReceivableDAO.getInvoiceReNum(invoiceReceivable.getContractId());//获取已有期数
        invoiceReceivable.setIssueId(issue + 1);//期数加一
        invoiceReceivableDAO.insertSelective(invoiceReceivable);

        reminderService.insertReminder(invoiceReceivable.getContractId(), 2, "应开票更新",userId);
        if (invoiceReceivable.getIssueId() != null && invoiceReceivable.getIssueId() == 1)
            acturalReceiptDao.updateTicketExecute(invoiceReceivable.getContractId(), acturalReceiptDao.getLastInvoiceId(), 0);
        else if (acturalReceiptDao.getState(invoiceReceivable.getContractId()) == 2) {
            acturalReceiptDao.updateTicketExecute(invoiceReceivable.getContractId(), acturalReceiptDao.getLastInvoiceId(), 0);
        }
        updateIssueId(invoiceReceivable.getContractId());
        return Message.createSuc(null);
    }
    //重新按照时间顺序维护期数
    public void updateIssueId(Integer contractId) {
        InvoiceReceivableExample invoiceReceivableExample = new InvoiceReceivableExample();
        invoiceReceivableExample.createCriteria().andContractIdEqualTo(contractId);
        invoiceReceivableExample.setOrderByClause("date");
        List<InvoiceReceivable> invoiceReceivableList = invoiceReceivableDAO.selectByExample(invoiceReceivableExample);
        int i=1;
        for(InvoiceReceivable invoiceReceivable:invoiceReceivableList){
            invoiceReceivable.setIssueId(i);
            invoiceReceivableDAO.updateByPrimaryKeySelective(invoiceReceivable);
            i++;
        }
    }

    @Transactional
    public Message deleteInvoiceRe(Integer invoiceReId) {
        Byte completeTag = invoiceReceivableDAO.selectByPrimaryKey(invoiceReId).getCompleteTag();
        if (completeTag != 0) {
            return Message.createErr(StatueCode.DELETE_FAILURE, "本期开票已开始，无法删除");
        }
        invoiceReceivableDAO.deleteByPrimaryKey(invoiceReId);
        Integer contractId = acturalReceiptDao.searchInvoiceReId(invoiceReId);//在删除应开票前检查是否有进行中的记录
        if (contractId != null) {
            InvoiceReceivableExample invoiceReceivableExample = new InvoiceReceivableExample();
            invoiceReceivableExample.createCriteria().andContractIdEqualTo(contractId);
            invoiceReceivableExample.setOrderByClause("id desc");
            List<InvoiceReceivable> invoiceReceivableList = invoiceReceivableDAO.selectByExample(invoiceReceivableExample);
            if (invoiceReceivableList.size() > 0) {
                acturalReceiptDao.updateTicketExecute(contractId, invoiceReceivableList.get(0).getId(), 2);
            } else {
                acturalReceiptDao.updateTicketExecute(contractId, 0, 1);
            }

        }

        return Message.createSuc(null);
    }

    @Transactional
    public Message updateInvoiceRe(InvoiceReceivable invoiceReceivable) {
        Byte completeTag = invoiceReceivableDAO.selectByPrimaryKey(invoiceReceivable.getId()).getCompleteTag();
        if (completeTag != 0) {
            return Message.createErr(511, "本期开票已开始，无法修改");
        }
        Byte unstart = 0;
        InvoiceReceivableExample invoiceReceivableExample = new InvoiceReceivableExample();
        invoiceReceivableExample.createCriteria().andContractIdEqualTo(invoiceReceivable.getContractId()).andDateGreaterThan(invoiceReceivable.getDate()).andCompleteTagNotEqualTo(unstart);
        List<InvoiceReceivable> invoiceReceivableList = invoiceReceivableDAO.selectByExample(invoiceReceivableExample);
        if (
                invoiceReceivableList.size() > 0) {//控制开票日期不能早于已开始或完成的期数时间
            return Message.createErr(StatueCode.DATE_MISTAKE, "修改的日期早于应开票进行中日期");
        }
        invoiceReceivableDAO.updateByPrimaryKeySelective(invoiceReceivable);
        acturalReceiptDao.updateTicket2(invoiceReceivable.getId(), invoiceReceivable.getInvoiceMoney(), invoiceReceivable.getDate());
        return Message.createSuc(null);
    }
}

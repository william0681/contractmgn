package com.zx.servicefinance.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zx.servicefinance.mapper.AccountReceivableDAO;
import com.zx.servicefinance.mapper.ContractDAO;
import com.zx.servicefinance.mapper.ContractNodeDAO;
import com.zx.servicefinance.model.*;
import com.zx.servicefinance.pojo.Message;
import com.zx.servicefinance.pojo.StatueCode;
import com.zx.servicefinance.schedule.ReceivableScheduleTask;
import com.zx.servicefinance.vo.OperationNode;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AccountReceivableService{
    @Autowired
    private AccountReceivableDAO accountReceivableDAO;
    @Autowired
    private ContractNodeDAO contractNodeDAO;
    @Autowired
    private ContractDAO contractDAO;
    @Autowired
    private ReceivableScheduleTask receivableScheduleTask;
    @Autowired
    private ReminderService reminderService;
    @Autowired
    private AccountService accountService;

    
    public Message addOperationAccountReceivable(OperationNode operationNode,int userId) throws ParseException {
        ContractNode contractNode = new ContractNode();
        contractNode.setContractId(operationNode.getContractId());
        AccountReceivable accountReceivable = new AccountReceivable();//创建应收款对象
        BeanUtils.copyProperties(operationNode, accountReceivable);//字段值复制
        JSONObject description = new JSONObject();//创建节点表描述json数据
        description.put("type", operationNode.getType());//type表示运维合同新建应收款类别
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<AccountReceivable> accountReceivableList = new ArrayList<>();
        Date startTime = null;//运维类合同收款开始时间
        Date endTime = null;//运维类合同收款截止时间
        Calendar calendar = null;
        AccountReceivableExample accountReceivableExample=new AccountReceivableExample();
        switch (operationNode.getType()) {//根据type创建运维类合同节点名称
            case 1://具体日期+n个工作日之内
                contractNode.setMoney(operationNode.getMoney());
                contractNode.setName(operationNode.getTime() + "日起" + operationNode.getDays() + "个工作日内");
                description.put("time", operationNode.getTime());
                description.put("days", operationNode.getDays());
                try {
                    startTime = simpleDateFormat.parse(operationNode.getTime());
                    accountReceivable.setStartDate(startTime);
                    calendar = Calendar.getInstance();
                    calendar.setTime(startTime);
                    calendar.add(Calendar.DATE, operationNode.getDays());
                    endTime = calendar.getTime();
                    accountReceivable.setEndDate(endTime);
                    accountReceivable.setStage(1);
                    accountReceivableExample.createCriteria().andContractIdEqualTo(accountReceivable.getContractId()).andActualMoneyGreaterThan(0.0).andEndDateGreaterThanOrEqualTo(accountReceivable.getEndDate());
                    List<AccountReceivable>accountReceivableJudge=accountReceivableDAO.selectByExample(accountReceivableExample);
                    if(accountReceivableJudge.size()>0){
                        return Message.createErr(505,"节点时间不能早于之前节点");
                    }
                } catch (ParseException e) {
                    return Message.createErr(501, "时间格式错误");
                }
                accountReceivableList.add(accountReceivable);
                break;
            case 2://合同签订后/服务开始后开始/截止+n个工作日之内
                contractNode.setMoney(operationNode.getMoney());
                contractNode.setName(operationNode.getTime() + operationNode.getDays() + "个工作日内");
                description.put("time", operationNode.getTime());
                description.put("days", operationNode.getDays());
                if (operationNode.getTime().equals("服务开始")) {
                    startTime = contractDAO.getContractServiceStartDate(operationNode.getContractId());
                } else if (operationNode.getTime().equals("合同签订")) {
                    startTime = contractDAO.getContractSignDate(operationNode.getContractId());
                } else if (operationNode.getTime().equals("服务结束")) {
                    startTime = contractDAO.getContractServiceEndDate(operationNode.getContractId());
                }
                accountReceivable.setStartDate(startTime);
                calendar = Calendar.getInstance();
                calendar.setTime(startTime);
                calendar.add(Calendar.DATE, operationNode.getDays());
                endTime = calendar.getTime();
                accountReceivable.setEndDate(endTime);
                accountReceivable.setStage(1);
                //判断后面加的节点时间是否比前面节点时间早
                accountReceivableExample.createCriteria().andContractIdEqualTo(accountReceivable.getContractId()).andActualMoneyGreaterThan(0.0).andEndDateGreaterThanOrEqualTo(accountReceivable.getEndDate());
                List<AccountReceivable>accountReceivableJudge=accountReceivableDAO.selectByExample(accountReceivableExample);
                if(accountReceivableJudge.size()>0){
                    return Message.createErr(505,"节点时间不能早于之前节点");
                }
                accountReceivableList.add(accountReceivable);
                break;
            case 3://具体日期前
                contractNode.setMoney(operationNode.getMoney());
                contractNode.setName(operationNode.getTime() + "日前");
                description.put("time", operationNode.getTime());
                description.put("days", operationNode.getDays());
                try {
                    endTime = simpleDateFormat.parse(operationNode.getTime());
                    accountReceivable.setEndDate(endTime);
                    Calendar calendars = null;
                    calendars = Calendar.getInstance();
                    calendars.setTime(endTime);
                    calendars.add(Calendar.MONTH, -1);
                    accountReceivable.setStartDate(calendars.getTime());
                    accountReceivable.setStage(1);
                    accountReceivableExample.createCriteria().andContractIdEqualTo(accountReceivable.getContractId()).andActualMoneyGreaterThan(0.0).andEndDateGreaterThanOrEqualTo(accountReceivable.getEndDate());
                    accountReceivableJudge=accountReceivableDAO.selectByExample(accountReceivableExample);
                    if(accountReceivableJudge.size()>0){
                        return Message.createErr(505,"节点时间不能早于之前节点");
                    }
//                    calendar = Calendar.getInstance();
//                    calendar.setTime(endTime);
//                    calendar.add(Calendar.DATE, -operationNode.getDays());
//                    startTime = calendar.getTime();
//                    accountReceivable.setStartDate(startTime);
                } catch (ParseException e) {
                    return Message.createErr(501, "时间格式错误");
                }
                accountReceivableList.add(accountReceivable);
                break;
            case 4://合同签订后/服务开始后开始/截止前
                contractNode.setMoney(operationNode.getMoney());
                contractNode.setName(operationNode.getTime() + "前");
                description.put("time", operationNode.getTime());
                description.put("days", operationNode.getDays());
                if (operationNode.getTime().equals("服务开始")) {
                    endTime = contractDAO.getContractServiceStartDate(operationNode.getContractId());
                } else if (operationNode.getTime().equals("合同签订")) {
                    endTime = contractDAO.getContractSignDate(operationNode.getContractId());
                } else if (operationNode.getTime().equals("服务结束")) {
                    endTime = contractDAO.getContractServiceEndDate(operationNode.getContractId());
                }
                accountReceivable.setEndDate(endTime);
                Calendar calendars = null;
                calendars = Calendar.getInstance();
                calendars.setTime(endTime);
                calendars.add(Calendar.MONTH, -1);
                accountReceivable.setStartDate(calendars.getTime());
                accountReceivable.setStage(1);
                accountReceivableExample.createCriteria().andContractIdEqualTo(accountReceivable.getContractId()).andActualMoneyGreaterThan(0.0).andEndDateGreaterThanOrEqualTo(accountReceivable.getEndDate());
                accountReceivableJudge=accountReceivableDAO.selectByExample(accountReceivableExample);
                if(accountReceivableJudge.size()>0){
                    return Message.createErr(505,"节点时间不能早于之前节点");
                }
//                calendar = Calendar.getInstance();
//                calendar.setTime(endTime);
//                calendar.add(Calendar.DATE, -operationNode.getDays());
//                startTime = calendar.getTime();
//                accountReceivable.setStartDate(startTime);
                accountReceivableList.add(accountReceivable);
                break;
            case 5://每年+具体日期起+n个工作日内+共m年
                contractNode.setMoney(operationNode.getMoney()*operationNode.getYears());
                String md = operationNode.getTime().substring(5, operationNode.getTime().length());
                contractNode.setName("每年" + md + "日起" + operationNode.getDays() + "个工作日内");
                description.put("time", md);
                description.put("days", operationNode.getDays());
                description.put("years", operationNode.getYears());
                try {
                    for (int i = 0; i < operationNode.getYears(); i++) {
                        AccountReceivable accountReceivableYear = new AccountReceivable();//因为带年份的节点需要多条记录，每次遍历都要创建一个AccountActual对象
                        accountReceivableYear.setContractId(operationNode.getContractId());
                        accountReceivableYear.setMoney(operationNode.getMoney());
                        accountReceivableYear.setRemark(operationNode.getRemark());
                        accountReceivableYear.setStage(i + 1);//每年生成一条应收款记录，stage字段表示第几年
                        startTime = simpleDateFormat.parse(operationNode.getTime());
                        calendar = Calendar.getInstance();
                        calendar.setTime(startTime);
                        calendar.add(Calendar.YEAR, i);//i初始为0，对年份逐次加i，每年一条应收款记录
                        startTime = calendar.getTime();
                        accountReceivableYear.setStartDate(startTime);
                        calendar.add(Calendar.DATE, operationNode.getDays());//根据几个工作日内算出截止时间
                        endTime = calendar.getTime();
                        accountReceivableYear.setEndDate(endTime);
                        accountReceivableExample.createCriteria().andContractIdEqualTo(accountReceivable.getContractId()).andActualMoneyGreaterThan(0.0).andEndDateGreaterThanOrEqualTo(accountReceivableYear.getEndDate());
                        accountReceivableJudge=accountReceivableDAO.selectByExample(accountReceivableExample);
                        if(accountReceivableJudge.size()>0){
                            return Message.createErr(505,"节点时间不能早于之前节点");
                        }
                        accountReceivableList.add(accountReceivableYear);
                    }

                } catch (ParseException e) {
                    return Message.createErr(501, "时间格式错误");
                }
                break;
        }

        String describe = JSONObject.toJSONString(description);
        contractNode.setDescribe(describe);
        contractNodeDAO.insertSelective(contractNode);//往合同节点表中插入一条运维类合同节点数据
        Integer contractNodeId = contractNodeDAO.lastRecordId();//获取刚刚插入的数据id
        for (AccountReceivable ar : accountReceivableList) {
            ar.setNodeId(contractNodeId);
            accountReceivableDAO.insertSelective(ar);
        }
        receivableScheduleTask.checkoutReceivableStatus();
        reminderService.insertReminder(operationNode.getContractId(), 2, "应收款更新",userId);
        Integer accountId = contractDAO.selectByPrimaryKey(accountReceivable.getContractId()).getAccountId();
        if (accountId == 1) {
            Integer accountReceivableId = accountReceivableDAO.lastRecordId();
            contractDAO.updateContractAccountId(accountReceivableId, accountReceivable.getContractId());
        }
        accountService.updateContractPeriod();
        return Message.createSuc(null);
    }

    
    public Message addCommonAccountReceivable(AccountReceivable accountReceivable,int userId) {
       Date endTime = accountReceivable.getEndDate();//应收款截止日期
//        if (endTime.before(new Date())) {
//            return Message.createErr(StatueCode.DATE_MISTAKE, "所选时间不能早于今天");
//        }
        AccountReceivableExample accountReceivableExample = new AccountReceivableExample();
        accountReceivableExample.createCriteria().andNodeIdEqualTo(accountReceivable.getNodeId()).andEndDateGreaterThan(accountReceivable.getEndDate());//在数据库查找该节点下是否有比要插入时间晚的记录
        List<AccountReceivable> accountReceivableList = accountReceivableDAO.selectByExample(accountReceivableExample);
        if (accountReceivableList.size() > 0) {
            return Message.createErr(StatueCode.DATE_MISTAKE, "所选日期不能早于前几期应收款日期");
        }
        ContractNodeExample contractNodeExample = new ContractNodeExample();
        contractNodeExample.createCriteria().andIdEqualTo(accountReceivable.getNodeId());
        String nodeName = contractNodeDAO.selectByExample(contractNodeExample).get(0).getName();//获取所要插入应收款记录的节点
        if (isEarlier(accountReceivable.getId(), nodeName, accountReceivable.getContractId(), accountReceivable.getEndDate())) {//判断此次添加应收款日期是否比前面已有的应收款时间晚
            return Message.createErr(StatueCode.DATE_MISTAKE, "所选日期早于之前节点应收款日期");
        }
        if (isLater(accountReceivable.getId(), nodeName, accountReceivable.getContractId(), accountReceivable.getEndDate())) {//判断此次添加应收款日期是否比后面节点中已有的节点时间晚（因为前面一个节点先完成收款才能对下一个节点收款）
            return Message.createErr(StatueCode.DATE_MISTAKE, "所选日期晚于后面节点应收款日期");
        }
        Calendar calendar = null;
        calendar = Calendar.getInstance();
        calendar.setTime(endTime);
        calendar.add(Calendar.MONTH, -1);
        accountReceivable.setStartDate(calendar.getTime());
        Integer stage = accountReceivableDAO.getRecordNum(accountReceivable.getContractId(), accountReceivable.getNodeId());
        accountReceivable.setStage(stage + 1);
        accountReceivableDAO.insertSelective(accountReceivable);
        receivableScheduleTask.checkoutReceivableStatus();
        reminderService.insertReminder(accountReceivable.getContractId(), 2, "应收款更新",userId);
        Integer accountId = contractDAO.selectByPrimaryKey(accountReceivable.getContractId()).getAccountId();
        if (accountId == 1) {
            Integer accountReceivableId = accountReceivableDAO.lastRecordId();
            contractDAO.updateContractAccountId(accountReceivableId, accountReceivable.getContractId());
        }
        accountService.updateContractPeriod();
        return Message.createSuc(null);
    }

    public Boolean isLater(Integer accountReId, String nodeName, Integer contractId, Date nodeDate) {
        if (accountReId == null) {//更新应收款日期控制和新日期和旧日期的对比
            accountReId = -1;
        }
        List<Integer> integerList = new ArrayList<>();
        switch (nodeName) {
            case "定金":
                integerList = contractNodeDAO.getAfterDeposit(contractId);
                break;
            case "预收款":
                integerList = contractNodeDAO.getAfterReceivedAdvance(contractId);
                break;
            case "进度款":
                integerList = contractNodeDAO.getAfterProgressPay(contractId);
                break;
            case "发货款":
                integerList = contractNodeDAO.getAfterDeliveryPay(contractId);
                break;
            case "验收款":
                integerList = contractNodeDAO.getAfterAcceptPay(contractId);
                break;
            case "质保金":
                break;
        }
        if (integerList.size() > 0) {
            AccountReceivableExample are = new AccountReceivableExample();
            are.createCriteria().andNodeIdIn(integerList);
            are.setOrderByClause("end_date");
            if (accountReceivableDAO.selectByExample(are).size() > 0) {
                AccountReceivable accountRe = accountReceivableDAO.selectByExample(are).get(0);
                if (!accountRe.getEndDate().after(nodeDate) && accountRe.getId() != accountReId) {//与后面节点最早收款日期比较
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean isEarlier(Integer accountReId, String nodeName, Integer contractId, Date nodeDate) {
        if (accountReId == null) {
            accountReId = -1;
        }
        List<Integer> integerList = new ArrayList<>();
        switch (nodeName) {
            case "定金":
                break;
            case "预收款":
                integerList = contractNodeDAO.getBeforeReceivedAdvance(contractId);
                break;
            case "进度款":
                integerList = contractNodeDAO.getBeforeProgressPay(contractId);
                break;
            case "发货款":
                integerList = contractNodeDAO.getBeforeDeliveryPay(contractId);
                break;
            case "验收款":
                integerList = contractNodeDAO.getBeforeAcceptPay(contractId);
                break;
            case "质保金":
                integerList = contractNodeDAO.getBeforeRetention(contractId);
                break;
        }
        if (integerList.size() > 0) {
            AccountReceivableExample are = new AccountReceivableExample();
            are.createCriteria().andNodeIdIn(integerList);
            are.setOrderByClause("end_date desc");
            if (accountReceivableDAO.selectByExample(are).size() > 0) {
                AccountReceivable accountRe = accountReceivableDAO.selectByExample(are).get(0);
                if (!accountRe.getEndDate().before(nodeDate) && accountRe.getId() != accountReId) {//与前面节点最晚收款日期比较
                    return true;
                }
            }
        }
        return false;
    }

    public Message getNodeInfo(Integer nodeId) {
        ContractNode contractNode = contractNodeDAO.selectByPrimaryKey(nodeId);
        Double nodeMoney = contractNode.getMoney();
        Double moneyActual = accountReceivableDAO.countNodeActualMoney(nodeId);
        if (moneyActual == null)
            moneyActual = 0.0;
        Double restNodeMoney = nodeMoney - moneyActual;
        Map<String, Object> result = new HashMap<>();
        result.put("nodeMoney", nodeMoney);
        result.put("restNodeMoney", restNodeMoney);
        AccountReceivableExample accountReceivableExample = new AccountReceivableExample();
        accountReceivableExample.createCriteria().andNodeIdEqualTo(nodeId);
        accountReceivableExample.setOrderByClause("end_date");
        List<AccountReceivable> accountReceivableList = accountReceivableDAO.selectByExample(accountReceivableExample);
        JSONArray jsonArray = new JSONArray();
        if (accountReceivableList.size() > 0) {
            for (AccountReceivable accountReceivable : accountReceivableList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("accountReceivableId", accountReceivable.getId());
                //jsonObject.put("stage", accountReceivable.getStage());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                jsonObject.put("date", simpleDateFormat.format(accountReceivable.getEndDate()));
                jsonObject.put("remark", accountReceivable.getRemark());
                jsonObject.put("money", accountReceivable.getMoney());
                jsonArray.add(jsonObject);
            }

        }
        result.put("receivableRecord", jsonArray);
        return Message.createSuc(result);
    }

    
    public Message getOperationReInfo(Integer contractId) {
        Map<String, Object> result = new HashMap<>();
        Double amount = contractDAO.getMoney(contractId);
        Double amountPlay = accountReceivableDAO.countMoneyByContractId(contractId);
        if (amountPlay == null) {
            amountPlay = 0.0;
        }
        result.put("contractMoney", amount);
        result.put("restMoney", amount - amountPlay);
        ContractNodeExample contractNodeExample = new ContractNodeExample();
        contractNodeExample.createCriteria().andContractIdEqualTo(contractId);
        List<ContractNode> contractNodeList = contractNodeDAO.selectByExample(contractNodeExample);
        JSONArray jsonArray = new JSONArray();
        if (contractNodeList.size() > 0) {
            for (ContractNode contractNode : contractNodeList) {
                AccountReceivableExample accountReceivableExample = new AccountReceivableExample();
                accountReceivableExample.createCriteria().andNodeIdEqualTo(contractNode.getId());
                accountReceivableExample.setOrderByClause("end_date");
                List<AccountReceivable> accountReceivableList = accountReceivableDAO.selectByExample(accountReceivableExample);
                if(accountReceivableList.size()>0){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("remark", accountReceivableList.get(0).getRemark());
                    jsonObject.put("nodeId", contractNode.getId());
                    jsonObject.put("money", contractNode.getMoney());
                    jsonObject.put("nodeInfo", JSONObject.parse(contractNode.getDescribe()));
                    jsonArray.add(jsonObject);
                }

            }
        }
        result.put("operationReList", jsonArray);
        return Message.createSuc(result);
    }

    
    public Message deleteCommonAccountRe(Integer accountReId, Integer nodeId) {
        AccountReceivableExample accountReceivableExample = new AccountReceivableExample();
        accountReceivableExample.createCriteria().andNodeIdEqualTo(nodeId);
        accountReceivableExample.setOrderByClause("end_date desc");
        Integer id = accountReceivableDAO.selectByExample(accountReceivableExample).get(0).getId();//查找该节点下时间最晚应收款期的id
        if (id != accountReId) {//要删除的不是该节点下最后一期
            return Message.createErr(StatueCode.DELETE_FAILURE, "请先删除该节点下的最后一期");
        }
        Double actualMoney = accountReceivableDAO.selectByPrimaryKey(accountReId).getActualMoney();
        if (actualMoney > 0) {
            return Message.createErr(StatueCode.DELETE_FAILURE, "该期已有实收款登记，无法删除");
        }
        accountReceivableDAO.deleteByPrimaryKey(accountReId);
        //应收款期数删除时，对于合同进行阶段也要更新
        accountService.updateContractPeriod();
        return Message.createSuc(null);
    }

    
    public Message deleteOperationAccountRe(Integer nodeId) {
        AccountReceivableExample accountReceivableExample = new AccountReceivableExample();
        accountReceivableExample.createCriteria().andNodeIdEqualTo(nodeId).andActualMoneyGreaterThan(0.0);
        List<AccountReceivable> accountReceivableList = accountReceivableDAO.selectByExample(accountReceivableExample);
        if (accountReceivableList.size() > 0) {
            return Message.createErr(StatueCode.DELETE_FAILURE, "已有实收款记录，无法删除");
        }
        AccountReceivableExample are = new AccountReceivableExample();
        are.createCriteria().andNodeIdEqualTo(nodeId);
        accountReceivableDAO.deleteByExample(are);
        contractNodeDAO.deleteByPrimaryKey(nodeId);
        accountService.updateContractPeriod();
        return Message.createSuc(null);
    }

    
    public Message updateCommonAccountRe(AccountReceivable accountReceivable) {
        AccountReceivableExample accountReceivableExample = new AccountReceivableExample();
        accountReceivableExample.createCriteria().andNodeIdEqualTo(accountReceivable.getNodeId()).andStageLessThan(accountReceivable.getStage()).andEndDateGreaterThan(accountReceivable.getEndDate()).andIdNotEqualTo(accountReceivable.getId());//在数据库查找该节点下是否有比要插入时间晚的记录
        List<AccountReceivable> accountReceivableList = accountReceivableDAO.selectByExample(accountReceivableExample);
        if (accountReceivableList.size() > 0) {
            return Message.createErr(StatueCode.DATE_MISTAKE, "所选日期不能早于前几期应收款日期");
        }
        AccountReceivableExample accountReceivableExamples = new AccountReceivableExample();
        accountReceivableExamples.createCriteria().andNodeIdEqualTo(accountReceivable.getNodeId()).andStageGreaterThan(accountReceivable.getStage()).andEndDateLessThan(accountReceivable.getEndDate());//在数据库查找该节点下大于所要编辑期数是否有比要插入时间早的记录
        List<AccountReceivable> accountReceivableLists = accountReceivableDAO.selectByExample(accountReceivableExamples);
        if (accountReceivableLists.size() > 0) {
            return Message.createErr(StatueCode.DATE_MISTAKE, "所选日期不能晚于后几期应收款日期");
        }
        ContractNodeExample contractNodeExample = new ContractNodeExample();
        contractNodeExample.createCriteria().andIdEqualTo(accountReceivable.getNodeId());
        String nodeName = contractNodeDAO.selectByExample(contractNodeExample).get(0).getName();//获取所要更新应收款记录的节点
        if (isEarlier(accountReceivable.getId(), nodeName, accountReceivable.getContractId(), accountReceivable.getEndDate())) {//判断此次添加应收款日期是否比前面已有的应收款时间晚
            return Message.createErr(StatueCode.DATE_MISTAKE, "所选日期不能早于前面节点应收款日期");
        }
        if (isLater(accountReceivable.getId(), nodeName, accountReceivable.getContractId(), accountReceivable.getEndDate())) {//判断此次添加应收款日期是否比后面节点中已有的节点时间晚（因为前面一个节点先完成收款才能对下一个节点收款）
            return Message.createErr(StatueCode.DATE_MISTAKE, "所选日期不能晚于后面节点应收款日期");
        }
        Calendar calendar = null;
        calendar = Calendar.getInstance();
        calendar.setTime(accountReceivable.getEndDate());
        calendar.add(Calendar.MONTH, -1);
        accountReceivable.setStartDate(calendar.getTime());
        accountReceivableDAO.updateByPrimaryKeySelective(accountReceivable);
        receivableScheduleTask.checkoutReceivableStatus();
        return Message.createSuc(null);
    }

}

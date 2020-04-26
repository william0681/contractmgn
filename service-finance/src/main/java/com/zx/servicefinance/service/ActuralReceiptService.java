package com.zx.servicefinance.service;

import com.zx.servicefinance.mapper.AccountReceivableDAO;
import com.zx.servicefinance.mapper.ContractDAO;
import com.zx.servicefinance.mapper.ContractNodeDAO;
import com.zx.servicefinance.mapper.IActuralReceiptDAO;
import com.zx.servicefinance.model.*;
import com.zx.servicefinance.pojo.Message;
import com.zx.servicefinance.schedule.ReceivableScheduleTask;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * ActuralReceiptService
 */
@Service
public class ActuralReceiptService{

    @Autowired
    private IActuralReceiptDAO acturalReceiptDAO;
    @Autowired
    ReminderService reminderService;

    @Autowired
    private CommissionService commissionService;

    @Autowired
    private ContractDAO contractDao;
    @Autowired
    ReceivableScheduleTask task;
    @Autowired
    private AccountReceivableDAO accountReceivableDAO;
    @Autowired
    private ContractNodeDAO contractNodeDAO;



    public Message deleteActualTicket(Integer actualticketid) {//阿民做
        ActuralTicket acturalTicket= acturalReceiptDAO.findactualtickedByid(actualticketid);
        Double amount=acturalTicket.getAmount();
        List<Map<String, Object>> map = acturalReceiptDAO.getTicketMoney1(acturalTicket.getContractId());//找到所有的当前合同应开票的记录,按照期数降序
        for(int index = 0; index < map.size(); index++){
            Map<String, Object> item = map.get(index);
            Double money = (Double) item.get("money");
            Double irmoney=(Double)item.get("irmoney") ;
//            System.out.println("irmoney:"+irmoney);
//            System.out.println("money:"+money);
            Integer id = (Integer) item.get("id");
            Integer shouldId = (Integer) item.get("should_id");
            if(Double.doubleToLongBits(money)!=Double.doubleToLongBits(irmoney))
            {
//                System.out.println("irmoney:"+irmoney);
//                System.out.println("money:"+money);
                acturalReceiptDAO.deleteShouldActual(shouldId,actualticketid);//删除应开票实开票的关联记录
                if(money+amount<irmoney)
                {
                    acturalReceiptDAO.deleteactualTicket(actualticketid);
                    Double temp=money+amount;
                    acturalReceiptDAO.updateMoney(temp, id);//更新ticket的钱
                    acturalReceiptDAO.updateTicketExecute(acturalTicket.getContractId(), shouldId, 0);//更新当前节点,0,date,1,未添加，2待添加应开票，3已完成
                    acturalReceiptDAO.updateShouldTicket(shouldId, 1);//更新应开票的完成状态,开票是否完成标志0未开始 1进行中 2已完成
                    break;
                }
                else if(Double.doubleToLongBits(money+amount)==Double.doubleToLongBits(irmoney))
                {
                    acturalReceiptDAO.deleteactualTicket(actualticketid);
                    Double temp=money+amount;
                    acturalReceiptDAO.updateMoney(temp, id);//更新ticket的钱
                    if(index!=map.size()-1)
                    {
                        acturalReceiptDAO.updateShouldTicket(shouldId, 0);//更新应开票的完成状态,开票是否完成标志0未开始 1进行中 2已完成
                        Map<String, Object> item1 = map.get(index+1);
                        Integer shouldId1 = (Integer) item1.get("should_id");
                        acturalReceiptDAO.updateShouldTicket(shouldId1,2);
                        acturalReceiptDAO.updateTicketExecute(acturalTicket.getContractId(), shouldId, 0);//更新当前节点,0,date,1,未添加，2待添加应开票，3已完成
                        break;
                    }
                    else
                    {
                        acturalReceiptDAO.updateShouldTicket(shouldId, 0);
                        acturalReceiptDAO.updateTicketExecute(acturalTicket.getContractId(), shouldId, 0);
                        break;
                    }
                }
                else{
                    acturalReceiptDAO.updateMoney(irmoney, id);//更新ticket的钱
                    acturalReceiptDAO.updateTicketExecute(acturalTicket.getContractId(), shouldId, 0);//更新当前节点,0,date,1,未添加，2待添加应开票，3已完成
                    acturalReceiptDAO.updateShouldTicket(shouldId, 0);
                    amount=amount-(irmoney-money);
                }
            }
        }
        return Message.createSuc(null);
    }


    public Integer deleteReceipt(Integer actrualId) {//阿民做
        AccountActual accountActual = acturalReceiptDAO.getAccountacturalByid(actrualId);
        double amount = accountActual.getReceivable();
        Integer contractId = accountActual.getContractId();
        Contract contract = contractDao.selectByPrimaryKey(contractId);
        List<Map<String, Object>> listmin = new ArrayList<>();
        if (contract.getOperationTag() == false) {
            listmin = acturalReceiptDAO.getShouldReceiptMoneymin(contractId);
        } else {
            listmin = acturalReceiptDAO.getShouldReceiptMoneymin2(contractId);
        }
        Collections.reverse(listmin);
        for (Map<String, Object> item : listmin) {
            Integer id = (Integer) item.get("id");
            // Double shouldMoney = (Double) item.get("should_money");
            Double acturalMoney = (Double) item.get("actural_money");
            if (acturalMoney != 0) {
                if (acturalMoney - amount > 0) {
                    acturalReceiptDAO.updateReceiptMoney1(id, acturalMoney - amount);
                    break;
                } else if (acturalMoney - amount == 0) {
                    acturalReceiptDAO.updateReceipt1(id);
                    break;
                } else {
                    acturalReceiptDAO.updateReceipt1(id);
                    amount -= acturalMoney;
                }
            }
        }
        Double restAmount = contract.getRestAmount();
        restAmount += accountActual.getReceivable();
        contract.setRestAmount(restAmount);
        contractDao.updateByPrimaryKeySelective(contract);
        //contractDao.updateRestAmount(model.getReceipt(), model.getContractId());
        return acturalReceiptDAO.deleteReceipt(actrualId);
    }


    public ActuralReceiptList getReceiptList(Integer id) {
        ActuralReceiptList list = new ActuralReceiptList();
        list.setTotalAmount(contractDao.getMoney(id));

        List<ActuralReceipt> alist = acturalReceiptDAO.getListByContractId1(id);
        for(ActuralReceipt acturalReceipt:alist)
        {
            acturalReceipt.setSellers(acturalReceiptDAO.getSellers(acturalReceipt.getId()));
        }
        list.setActureReceipts(alist);
        return list;
    }


    public Double isPlan(Integer contractId) {
        return acturalReceiptDAO.isPlan(contractId);
    }


    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public Integer insertReceipt(ReceiptModel model, Integer opratorTag, int userId) {

        acturalReceiptDAO.insertReceipt(model);
        reminderService.insertReminder(model.getContractId(), 2, "实收款更新",userId);
        double amount = model.getReceipt().doubleValue();
        List<Map<String, Object>> list = new ArrayList<>();
        if(opratorTag == 0) {
            list = acturalReceiptDAO.getShouldReceiptMoney(model.getContractId());
        }
        else
        {
            list = acturalReceiptDAO.getShouldReceiptMoney2(model.getContractId());
        }
        for (Map<String, Object> item : list) {
            Integer id = (Integer) item.get("id");
            Double shouldMoney = (Double) item.get("should_money");
            Double acturalMoney = (Double) item.get("actural_money");
            if (amount + acturalMoney < shouldMoney) {
                acturalReceiptDAO.updateReceiptMoney(id, amount + acturalMoney);
                break;
            } else {
                amount -= (shouldMoney - acturalMoney);
                acturalReceiptDAO.updateReceipt(id);
            }

        }

        Contract contract = contractDao.selectByPrimaryKey(model.getContractId());
        Double restAmount = contract.getRestAmount();
        restAmount-=model.getReceipt();
        contract.setRestAmount(restAmount);
        contractDao.updateByPrimaryKeySelective(contract);
        //contractDao.updateRestAmount(model.getReceipt(), model.getContractId());
        for (Seller item : model.getSellers()) {
            commissionService.insertCommission(model.getContractId(), model.getId(), item.getPercentage(),
                    model.getDueDate(), item.getSellerId());
        }
        task.checkCommissionStatus();
        task.checkoutReceivableStatus();

        return model.getId();
    }


    public List<Map<String, Object>> getAcuralTicket(Integer id) {
        return acturalReceiptDAO.getTicket(id);
    }


    @Transactional
    public Integer addActralTicket(ActuralTicket model,int userId) {
        acturalReceiptDAO.addActralTicket(model);
        reminderService.insertReminder(model.getContractId(), 2, "实开票更新",userId);
        List<Map<String, Object>> map = acturalReceiptDAO.getTicketMoney(model.getContractId());
        double amount = model.getAmount().doubleValue();
        if (map != null)
            for (int index = 0; index < map.size(); index++) {
                Map<String, Object> item = map.get(index);
                Double money = (Double) item.get("money");
                Integer id = (Integer) item.get("id");
                Integer shouldId = (Integer) item.get("should_id");
                if (money > amount) {
                    Double temp = money - amount;

                    acturalReceiptDAO.updateMoney(temp, id);//更新ticket的钱
                    acturalReceiptDAO.addShouldActral(shouldId, model.getId(), amount);//添加应开票实开票的关联记录
                    acturalReceiptDAO.updateTicketExecute(model.getContractId(), shouldId, 0);//
                    acturalReceiptDAO.updateShouldTicket(shouldId, 1);//更新应开票的完成状态
                    break;
                } else if (money < amount) {
                    amount -= money;
                    acturalReceiptDAO.updateMoney(0.0, id);
                    acturalReceiptDAO.updateTicket(shouldId);
                    acturalReceiptDAO.updateShouldTicket(shouldId, 2);
                } else {
                    Double allTicket = acturalReceiptDAO.getAllTicket(model.getContractId()),
                            contractMoney = contractDao.getMoney(model.getContractId());//应开票总金额，合同总金额
                    if (allTicket == null)
                        allTicket = 0d;
                    if (contractMoney == null)
                        contractMoney = 0d;
                    if (index == map.size() - 1 && allTicket.equals( contractMoney)) {
                        Double temp = money - amount;
                        acturalReceiptDAO.updateMoney(temp, id);
                        acturalReceiptDAO.addShouldActral(shouldId, model.getId(), amount);
                        acturalReceiptDAO.updateTicketExecute(model.getContractId(), shouldId, 3);//0,date,1,未添加，2待添加应开票，3已完成
                        acturalReceiptDAO.updateShouldTicket(shouldId, 1);//开票是否完成标志0未开始 1进行中 2已完成
                    } else if (index == map.size() - 1) {
                        Double temp = money - amount;
                        acturalReceiptDAO.updateMoney(temp, id);
                        acturalReceiptDAO.addShouldActral(shouldId, model.getId(), amount);//应开票id，实开票id，和实开票的钱
                        acturalReceiptDAO.updateTicketExecute(model.getContractId(), shouldId, 2);
                        acturalReceiptDAO.updateShouldTicket(shouldId, 1);
                    } else {
                        Double temp = money - amount;
                        acturalReceiptDAO.updateMoney(temp, id);
                        acturalReceiptDAO.addShouldActral(shouldId, model.getId(), amount);

                        acturalReceiptDAO.updateShouldTicket(shouldId, 1);
                        item = map.get(index + 1);
                        shouldId = (Integer) item.get("should_id");
                        acturalReceiptDAO.updateTicketExecute(model.getContractId(), shouldId, 0);

                    }

                    break;
                }

            }
        return model.getId();
    }


    public Double isPlanTicket(Integer contractId) {
        return acturalReceiptDAO.isPlanTicket(contractId);
    }


    public List<Map<String, Object>> getTicketList(String key, Date startDate, Date endDate, Integer blockId) {
        List<Map<String, Object>> list = acturalReceiptDAO.getTicketList(key, startDate, endDate, blockId);
        for (Map<String,Object> item : list) {
            Integer contractId = (Integer) item.get( "contract_id") ;
            Integer state = (Integer) item.get("state");
            Date date = (Date) item.get("date");
            switch(state)
            {
                case 0: item.put("state", getState(date));
                    break;
                case 1: item.put("state", "未添加");
                    break;
                case 2 : item.put("state", "待添加应开票");
                    break;
                case 3 : item.put("state","已完成");
                    item.put("actural",contractDao.getMoney(contractId));
                    item.put("should",contractDao.getMoney(contractId));
                    item.remove("issue");
                    break;
            }
            item.remove("date");
            //曾庆民添加
            Integer contractType=(Integer) item.get("contract_type");
            if(contractType==3){
                Double amount = contractDao.getMoney(contractId);
                Double amountPlay = accountReceivableDAO.countMoneyByContractId(contractId);
                if (amountPlay == null) {
                    amountPlay = 0.0;
                }
                item.put("restamount",amount-amountPlay);
            }
            else{
                Double restamount=0D;
                ContractNodeExample contractNodeExample = new ContractNodeExample();
                contractNodeExample.createCriteria().andContractIdEqualTo(contractId).andMoneyNotEqualTo(0D);
                List<ContractNode> contractNodeList = contractNodeDAO.selectByExample(contractNodeExample);
                for(ContractNode contractNode:contractNodeList){
                    Double nodeMoney = contractNode.getMoney();
                    Double moneyActual = accountReceivableDAO.countNodeActualMoney(contractNode.getId());
                    if (moneyActual == null)
                        moneyActual = 0.0;
                    Double restNodeMoney = nodeMoney - moneyActual;
                    restamount=restamount+restNodeMoney;
                }
                item.put("restamount",restamount);
            }
            //曾庆民添加
        }

        return list;
    }


    public List<Map<String, Object>> getTicketDetail(Integer contractId) {
        List<Map<String, Object>> list = acturalReceiptDAO.getTicketDetail(contractId);
        if(list == null)
            list = new ArrayList<>();
        Map<String,Object> temp = acturalReceiptDAO.getTicketExecute(contractId);
        if(temp == null)
        {
            return  list;
        }
        Integer issueId =  (Integer) temp.get("issue_id");
        Double money = (Double) temp.get("money");
        for( Map<String,Object> item : list)
        {
            Double shouldMoney = (Double) item.get("should_money");
            Integer issueId1 = (Integer) item.get("issue_id");
            if(issueId1< issueId )
                item.put("actural_money",shouldMoney);
            else if(issueId1.equals(issueId))
            {
                item.put("actural_money", money);
            }
            else
            {
                item.put("actural_money", 0);
            }
        }
        return  list;
    }


    public List<Map<String, Object>> getActralDetail(Integer id) {
        return acturalReceiptDAO.getActralDetail(id);
    }


    public List<LinkedHashMap<String, Object>> getOutput(Integer blockId, Integer contractTypeId, String sql,
                                                         String startDate, String endDate, String changeStartDate, String changeEndDate) {

        List<LinkedHashMap<String, Object>> contracts = acturalReceiptDAO.getOutput(blockId, contractTypeId, startDate,
                endDate, changeStartDate, changeEndDate);
        if (contracts == null || contracts.size() != 0) {
            for (LinkedHashMap<String, Object> item1 : contracts) {
                Integer contractId = (Integer) item1.get("id");
                if (sql == null)
                    sql = "";
                if (sql.contains("1")) {
                    List<LinkedHashMap<String, Object>> receives = acturalReceiptDAO.getreceiptByContractId(contractId);
                    if (receives != null && receives.size() != 0) {
                        List<LinkedHashMap<String, Object>> acturals = acturalReceiptDAO
                                .getActuralContractId(contractId);
                        if (acturals == null)
                            acturals = new ArrayList<>();
                        List<LinkedHashMap<String, Object>> list = new ArrayList<>();
                        item1.put("receive", list);
                        int index1, index2 = index1 = 0;
                        double money1, money2 = money1 = 0;
                        Date date1 = null;
                        String nodeName = null, issue = null;
                        LinkedHashMap<String, Object> item2, item3 = item2 = null;
                        while (index1 < receives.size()) {

                            if (item2 == null || money1 == 0 || index2 > acturals.size()) {
                                item2 = receives.get(index1);
                                money1 = item2.get("money") == null ? 0.0 : (double) item2.get("money");
                                date1 = (Date) item2.get("date");
                                nodeName = (String) item2.get("node_name");
                                issue = (String) item2.get("issue");
                            }

                            if ((item3 == null || money2 == 0) && index2 < acturals.size()) {
                                item3 = acturals.get(index2);
                                money2 = item3.get("money") == null ? 0.0 : (double) item3.get("money");
                            }

                            LinkedHashMap<String, Object> temp = new LinkedHashMap<>();

                            if (index2 >= acturals.size()) {
                                temp.put("node_name", nodeName);
                                temp.put("issue", issue);
                                temp.put("should_money", money1);
                                temp.put("actral_money", 0.0);

                                money1 = 0;
                                if (date1 != null)
                                    temp.put("state", getState(date1));
                                else
                                    temp.put("state", "未开始");
                                index1++;
                            } else if (money1 > money2) {

                                temp.put("node_name", nodeName);
                                temp.put("issue", issue);
                                temp.put("should_money", money2);
                                temp.put("actral_money", money2);
                                temp.put("state", "已完成");
                                money1 -= money2;
                                money2 = 0.0;
                                index2++;

                            } else {
                                temp.put("node_name", nodeName);
                                temp.put("issue", issue);
                                temp.put("should_money", money1);
                                temp.put("actral_money", money1);
                                temp.put("state", "已完成");
                                money2 -= money1;
                                money1 = 0.0;
                                index1++;
                                if (money1 == money2)
                                    index2++;
                            }

                            list.add(temp);

                        }

                    }

                } else {
                    item1.put("receive", null);
                }
                if (sql.contains("2")) {
                    List<LinkedHashMap<String, Object>> tickets = acturalReceiptDAO.getTicketByContractId(contractId);
                    if (tickets != null && tickets.size() != 0) {
                        List<LinkedHashMap<String, Object>> acturals = acturalReceiptDAO
                                .getActuralTicketContractId(contractId);
                        if (acturals == null)
                            acturals = new ArrayList<>();
                        List<LinkedHashMap<String, Object>> list = new ArrayList<>();
                        item1.put("tickets", list);
                        int index1, index2 = index1 = 0;
                        double money1, money2 = money1 = 0;
                        Date date = null;
                        String issue = null;
                        LinkedHashMap<String, Object> item2, item3 = item2 = null;
                        while (index1 < tickets.size()) {

                            if (item2 == null || money1 == 0 || index2 > acturals.size()) {
                                item2 = tickets.get(index1);
                                money1 = item2.get("money") == null ? 0.0 : (double) item2.get("money");
                                date = (Date) item2.get("date");
                                issue = (String) item2.get("issue");
                            }

                            if ((item3 == null || money2 == 0) && index2 < acturals.size()) {
                                item3 = acturals.get(index2);
                                money2 = item3.get("money") == null ? 0.0 : (double) item3.get("money");
                            }

                            LinkedHashMap<String, Object> temp = new LinkedHashMap<>();

                            if (index2 >= acturals.size()) {
                                temp.put("issue", issue);
                                temp.put("should_money", money1);
                                temp.put("actral_money", 0.0);

                                money1 = 0;
                                temp.put("state", getState(date));
                                index1++;
                            } else if (money1 > money2) {
                                temp.put("issue", issue);
                                temp.put("should_money", money2);
                                temp.put("actral_money", money2);
                                temp.put("state", "已完成");
                                money1 -= money2;
                                money2 = 0;
                                index2++;
                            } else {
                                temp.put("issue", issue);
                                temp.put("should_money", money1);
                                temp.put("actral_money", money1);
                                temp.put("state", "已完成");
                                money2 -= money1;
                                money1 = 0;
                                index1++;
                                if (money1 == money2)
                                    index2++;
                            }

                            list.add(temp);

                        }

                    }
                } else {
                    item1.put("tickets", null);
                }
                if (sql.contains("3")) {
                    Double shouldMoney = acturalReceiptDAO.getShouldReceiptTotalMoney(contractId);
                    Double acturalMoney = acturalReceiptDAO.getActuralTotalMoney(contractId);
                    item1.put("should_total_money", shouldMoney);
                    item1.put("actural_total_money", acturalMoney);
                } else {
                    item1.put("should_total_money", null);
                    item1.put("actural_total_money", null);
                }
                if (sql.contains("4")) {
                    Double shouldTicket = acturalReceiptDAO.getShouldTicket(contractId);
                    Double actralTicket = acturalReceiptDAO.getActuralTicket(contractId);
                    item1.put("should_total_ticket", shouldTicket);
                    item1.put("actural_total_ticket", actralTicket);
                } else {
                    item1.put("should_total_ticket", null);
                    item1.put("actural_total_ticket", null);
                }

            }

        }

        return contracts;
    }

    private static String getState(Date date) {
        int days = days(date);
        if (days < -30)
            return "进行中";
        else if (days <= 0)
            return String.format("离逾期还有%d天", -days);
        else
            return String.format("已逾期%d天", days);
    }

    private static int days(Date date) {
        int days =(int) Math.floor( ((new Date().getTime() - date.getTime()) / (1000 * 3600 * 24)));
        return days ;
    }


    public List<Map<String, Object>> getNodeList(Integer contractId) {
        return acturalReceiptDAO.getNodeList(contractId);
    }

}
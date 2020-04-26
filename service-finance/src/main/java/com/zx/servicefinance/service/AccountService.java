package com.zx.servicefinance.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zx.servicefinance.model.*;
import com.zx.servicefinance.mapper.AccountActualDAO;
import com.zx.servicefinance.mapper.AccountReceivableDAO;
import com.zx.servicefinance.mapper.ContractDAO;
import com.zx.servicefinance.mapper.ContractNodeDAO;
import com.zx.servicefinance.pojo.Message;
import com.zx.servicefinance.vo.AccountInfo;
import com.zx.servicefinance.vo.AccountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountService{
    @Autowired
    private AccountReceivableDAO accountReceivableDAO;
    @Autowired
    private ContractDAO contractDAO;
    @Autowired
    private AccountActualDAO accountActualDAO;
    @Autowired
    private ContractNodeDAO contractNodeDAO;

    
    public Message getAcList(Integer contractId, Integer pageNum, Integer record) {
        AccountActualExample accountActualExample = new AccountActualExample();
        accountActualExample.createCriteria().andContractIdEqualTo(contractId);
        PageHelper.startPage(pageNum, record);
        PageInfo<AccountActual> pageInfo = new PageInfo<>(accountActualDAO.selectByExample(accountActualExample));
        // List<AccountActual> accountActualList =
        // accountActualDAO.selectByExample(accountActualExample);
        JSONArray actualList = new JSONArray();
        if (pageInfo.getList().size() > 0) {
            for (AccountActual accountActual : pageInfo.getList()) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("actualDate", simpleDateFormat.format(accountActual.getReceiveDate()));
                jsonObject.put("actualAmount", accountActual.getReceivable());
                jsonObject.put("remark", accountActual.getRemark());
                actualList.add(jsonObject);
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("all_record", pageInfo.getTotal());
        result.put("all_page", pageInfo.getPages());
        result.put("current_page", pageNum);
        result.put("data", actualList);
        return Message.createSuc(result);
    }

    
    public Message getReList(Integer contractId, Integer pageNum, Integer record) {
        PageHelper.startPage(pageNum, record);
        PageInfo<AccountVo> pageInfo = new PageInfo<>(accountReceivableDAO.getAccountList(contractId));
        // Map<String, List<AccountVo>> map = new HashMap<>();
        // if (pageInfo.getList().size() > 0) {
        // for (AccountVo accountVo : pageInfo.getList()) {
        // if (map.containsKey(accountVo.getNodeName())) {
        // map.get(accountVo.getNodeName()).add(accountVo);
        // } else {
        // List<AccountVo>accountVoLists=new ArrayList<>();
        // accountVoLists.add(accountVo);
        // map.put(accountVo.getNodeName(),accountVoLists);
        // }
        // }
        // }
        Map<String, Object> result = new HashMap<>();
        result.put("all_record", pageInfo.getTotal());
        result.put("all_page", pageInfo.getPages());
        result.put("current_page", pageNum);
        result.put("data", pageInfo.getList());
        return Message.createSuc(result);
    }

    
    public Message getAccountInfo(Integer pageNum, Integer record, String key, Date start, Date end, Integer userId) {
        PageHelper.startPage(pageNum, record);
        PageInfo<AccountInfo> pageInfo = new PageInfo<>(accountReceivableDAO.getAccountInfo(key, start, end, userId));
        //min
        for (AccountInfo accountInfo : pageInfo.getList()){
            //根据合同的类别分情况讨论
            if(accountInfo.getContractType().equals("运维技术服务")){
                int contractId=accountInfo.getContractId();
                Double amount = contractDAO.getMoney(contractId);
                Double amountPlay = accountReceivableDAO.countMoneyByContractId(contractId);
                if (amountPlay == null) {
                    amountPlay = 0.0;
                }
                accountInfo.setRestamount(amount-amountPlay);
            }
            else
            {
                int contractId=accountInfo.getContractId();
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
                accountInfo.setRestamount(restamount);
            }
        }
        //min

        // 合同完成时，即状态返回是5时，将当期应收款和当期实收款改为合同总金额
        for (AccountInfo accountInfo : pageInfo.getList()) {
            if (accountInfo.getStatus() == 5) {
                accountInfo.setContractNode("");
                accountInfo.setStage(null);
                Contract contract = contractDAO.selectByPrimaryKey(accountInfo.getContractId());
                accountInfo.setAccountReceivable(contract.getAmount());
                accountInfo.setAccountActual(contract.getAmount());
            }
            if (!StringUtils.isEmpty(accountInfo.getContractNode()) && accountInfo.getContractNode().contains("每年")) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String year = ",第" + simpleDateFormat.format(accountInfo.getEndDate()).substring(0, 4) + "年";
                accountInfo.setContractNode(accountInfo.getContractNode() + year);
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("all_record", pageInfo.getTotal());
        result.put("all_page", pageInfo.getPages());
        result.put("current_page", pageNum);
        result.put("data", pageInfo.getList());
        return Message.createSuc(result);

    }

    
    public Message updateContractPeriod() {
        ContractExample contractExample = new ContractExample();
        contractExample.createCriteria().andAccountIdNotEqualTo(0);
        List<Contract> contractList = contractDAO.selectByExample(contractExample);
        for (Contract contract : contractList) {
            AccountReceivableExample accountReceivableExample = new AccountReceivableExample();
            accountReceivableExample.createCriteria().andContractIdEqualTo(contract.getId()).andStatusNotEqualTo(5);
            accountReceivableExample.setOrderByClause("end_date");
            List<AccountReceivable> accountReceivableList = accountReceivableDAO
                    .selectByExample(accountReceivableExample);
            if (accountReceivableList.size() > 0) {
                int flag = 0;
                ContractNodeExample contractNodeExample = new ContractNodeExample();
                contractNodeExample.createCriteria().andContractIdEqualTo(contract.getId()).andMoneyNotEqualTo(0.0);
                List<ContractNode> contractNodeList = contractNodeDAO.selectByExample(contractNodeExample);
                ContractNode contractNode = new ContractNode();
                if (contractNodeList.size() > 0 && contractNodeList.get(0).getDescribe() == null) {
                    Double money = 0.0;// 该节点已收金额
                    for (ContractNode contractNodes : contractNodeList) {
                        money = accountReceivableDAO.countActualMoney(contractNodes.getId());// 计算该节点已收金额
                        if (money == null) {
                            money = 0.0;
                        }
                        if (contractNodes.getMoney() - money > 1e-6) {
                            contractNode = contractNodes;// 从前往后找到第一个钱未付清的节点
                            break;
                        }
                    }

                    // Integer type = 0;
                    // JSONObject jsonObject = new JSONObject();
                    // if (contractNode.getDescribe() != null) {
                    // jsonObject = JSONObject.parseObject(contractNode.getDescribe());
                    // type = jsonObject.getInteger("type");
                    // }
                    // 处理运维合同 每年收一笔款的情况
                    // if (type == 5) {
                    // Integer years = jsonObject.getInteger("years");
                    // AccountReceivableExample accountReceivableYear = new
                    // AccountReceivableExample();
                    // accountReceivableYear.createCriteria().andNodeIdEqualTo(contractNode.getId());
                    // List<AccountReceivable> accountReceivablesYear =
                    // accountReceivableDAO.selectByExample(accountReceivableYear);
                    // Calendar now = Calendar.getInstance();
                    // now.setTime(accountReceivableList.get(0).getEndDate());
                    // Calendar receivable = Calendar.getInstance();
                    // receivable.setTime(accountReceivablesYear.get(0).getEndDate());
                    // Integer yearNow = now.get(Calendar.YEAR);
                    // Integer yearRe = receivable.get(Calendar.YEAR);
                    // if (money<contractNode.getMoney() / years * (yearNow - yearRe + 1) ) {
                    // if
                    // (!contractNode.getName().equals(contractNodeDAO.selectByPrimaryKey(accountReceivableList.get(0).getNodeId()).getName()))
                    // {
                    // contract.setAccountId(2);
                    // contractDAO.updateByPrimaryKeySelective(contract);
                    // flag = 1;
                    // }
                    // break;
                    // }
                    //
                    //
                    // } else
                    if (contractNode.getContractId() != null) {// 该节点a钱未收起，比较此时节点a名称和将要进行应收款阶段的节点名称是否一致，不一致表示应该等待添加a节点应收款
                        String name = contractNodeDAO.selectByPrimaryKey(accountReceivableList.get(0).getNodeId())
                                .getName();//
                        if (!contractNode.getName().equals(name)) {
                            contract.setAccountId(2);
                            contractDAO.updateByPrimaryKeySelective(contract);
                            flag = 1;
                        }
                    }
                }

                if (flag == 0) {
                    contract.setAccountId(accountReceivableList.get(0).getId());
                    contractDAO.updateByPrimaryKeySelective(contract);
                }

            } else {// 录入系统的应收款已完成，但整个合同收款未完成，使该合同accountId设为2，系统设定状态进行中
                if (contract.getRestAmount() > 0 && contract.getRestAmount() < contract.getAmount()) {
                    contract.setAccountId(2);
                    contractDAO.updateByPrimaryKeySelective(contract);
                }
                if (contract.getAmount() - contract.getRestAmount() < 1e-6) {// 删除了所有应收款且该合同未有实收款的的条件下，状态重新变回未添加
                    contract.setAccountId(1);
                    contractDAO.updateByPrimaryKeySelective(contract);
                }
            }

        }
        return Message.createSuc(null);

    }
}

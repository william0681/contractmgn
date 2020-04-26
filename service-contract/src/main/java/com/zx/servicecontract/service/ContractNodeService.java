package com.zx.servicecontract.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zx.servicecontract.mapper.AccountReceivableDAO;
import com.zx.servicecontract.mapper.ContractNodeDAO;
import com.zx.servicecontract.model.ContractNode;
import com.zx.servicecontract.model.ContractNodeExample;
import com.zx.servicecontract.pojo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContractNodeService{

    @Autowired
    private ContractNodeDAO contractNodeDAO;
    @Autowired
    private AccountReceivableDAO accountReceivableDAO;

    public Message getContractNodeList(Integer contractId) {
        ContractNodeExample contractNodeExample = new ContractNodeExample();
        contractNodeExample.createCriteria().andContractIdEqualTo(contractId);
        List<ContractNode> contractNodeList = contractNodeDAO.selectByExample(contractNodeExample);//获得合同节点
//        AccountActualExample accountActualExample=new AccountActualExample();
//        accountActualExample.createCriteria().andContractIdEqualTo(contractId);
//        List<AccountActual> accountActualList=accountActualDAO.selectByExample(accountActualExample);
//        if(accountActualList.size()>0){
//
//        }
        Map<String, Object> map = new HashMap<>();
        JSONArray array = new JSONArray();

        for (ContractNode contractNode : contractNodeList) {
            Double moneyActual = accountReceivableDAO.countMoneyByNodeId(contractNode.getId());
            if (moneyActual == null)
                moneyActual = 0.0;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("nodeId", contractNode.getId());
            jsonObject.put("name", contractNode.getName());
            jsonObject.put("money", contractNode.getMoney());
            jsonObject.put("restMoney", contractNode.getMoney() - moneyActual);
            array.add(jsonObject);
        }
        map.put("nodeList", array);
        return Message.createSuc(map);
    }

    @Transactional
    public Message updateContractNode(List<ContractNode> contractNodeList) {
        for (ContractNode contractNode : contractNodeList) {
            try {
                contractNodeDAO.updateByPrimaryKeySelective(contractNode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return Message.createSuc(null);
    }
}

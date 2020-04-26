package com.zx.servicefinance.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zx.servicefinance.mapper.*;
import com.zx.servicefinance.model.*;
import com.zx.servicefinance.pojo.Message;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class CommissionService{

    @Autowired
    CommissionDAO commissionDAO;
    @Autowired
    ContractDAO contractDAO;
    @Autowired
    BlockDAO blockDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    AccountActualDAO accountActualDAO;
    @Autowired
    BlockUserDAO blockUserDAO;
    @Autowired
    ContractTypeDAO contractTypeDAO;

    //曾庆民添加
    @Autowired
    private AccountReceivableDAO accountReceivableDAO;

    @Autowired
    private ContractNodeDAO contractNodeDAO;

//曾庆民添加

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");


    public Message getCommissionList(int pageNum,int record,String key,String startTime,String endTime,String role,int userId) throws ParseException {

        HashMap<String,Object> result = new HashMap<>();

        CommissionExample example = new CommissionExample();
        CommissionExample.Criteria criteria1 = example.createCriteria();
        CommissionExample.Criteria criteria2 = example.createCriteria();
        example.setOrderByClause("status asc,due_date asc,id asc");
        if(!startTime.equals("")){
            Date startDate = format.parse(startTime);
            criteria1.andSignDateGreaterThanOrEqualTo(startDate);
            criteria2.andSignDateGreaterThanOrEqualTo(startDate);
        }
        if(!endTime.equals("")){
            Date endDate = format.parse(endTime);
            criteria1.andSignDateLessThanOrEqualTo(endDate);
            criteria2.andSignDateLessThanOrEqualTo(endDate);
        }
        if(!key.equals("")){
            ContractExample contractExample = new ContractExample();
            contractExample.or().andNameLike("%"+key+"%");

            BlockExample blockExample = new BlockExample();
            blockExample.or().andNameLike("%"+key+"%");
            List<Block> blocks = blockDAO.selectByExample(blockExample);
            List<Integer> blockIdList = new ArrayList<>();
            for(Block block: blocks){
                blockIdList.add(block.getId());
            }
            if(blockIdList.size()!=0) {
                ContractExample.Criteria criteria = contractExample.createCriteria();
                criteria.andBlockIn(blockIdList);
                contractExample.or(criteria);
            }

            List<Contract> contracts = contractDAO.selectByExample(contractExample);
            List<Integer> contractIdList = new ArrayList<>();
            for(Contract contract : contracts){
                contractIdList.add(contract.getId());
            }
            if(contractIdList.size()!=0) {
                criteria1.andContractIdIn(contractIdList);
            }

            UserExample userExample = new UserExample();
            userExample.or().andRoleEqualTo(5).andNameLike("%"+key+"%");
            List<User> userList = userDAO.selectByExample(userExample);
            List<Integer> salerIdList = new ArrayList<>();
            for(User user : userList){
                salerIdList.add(user.getId());
            }
            if(salerIdList.size()!=0) {
                if(contractIdList.size()!=0) {
                    criteria2.andSalerIdIn(salerIdList);
                    example.or(criteria2);
                }else{
                    criteria1.andSalerIdIn(salerIdList);
                }
            }

        }

        if(role.equals("areachief")){
            BlockUserExample blockUserExample = new BlockUserExample();
            blockUserExample.or().andUserIdEqualTo(userId);
            List<BlockUser> blockUsers = blockUserDAO.selectByExample(blockUserExample);
            if(blockUsers.size()==1){
                int blockId = blockUsers.get(0).getBlockId();
                ContractExample contractExample = new ContractExample();
                contractExample.or().andBlockEqualTo(blockId);
                List<Contract> contracts = contractDAO.selectByExample(contractExample);
                if(contracts.size()!=0){
                    List<Integer> contractIdList = new ArrayList<>();
                    for(Contract contract : contracts){
                        contractIdList.add(contract.getId());
                    }
                    List<CommissionExample.Criteria> oredCriteria = example.getOredCriteria();
                    for(CommissionExample.Criteria criteria : oredCriteria){
                        criteria.andContractIdIn(contractIdList);
                    }
                }
            }
        }

        JSONArray array = new JSONArray();
        PageHelper.startPage(pageNum,record);
        PageInfo<Commission> commissions = new PageInfo<>(commissionDAO.selectByExample(example));
        for(Commission commission : commissions.getList()){
            Contract contract = contractDAO.selectByPrimaryKey(commission.getContractId());
            if(contract!=null) {
                JSONObject obj = new JSONObject();
                obj.put("id", commission.getId());
                obj.put("number", contract.getNumber());
                obj.put("signDate", format.format(commission.getSignDate()));
                obj.put("contractName", contract.getName());
                obj.put("block", blockDAO.selectByPrimaryKey(contract.getBlock()).getName());
                obj.put("money", commission.getMoney());
                obj.put("saler", userDAO.selectByPrimaryKey(commission.getSalerId()).getName());
                obj.put("status", commission.getStatus());
                obj.put("days", commission.getDays());

                //曾庆民添加
                Integer contractType=(Integer) contract.getContractType();
                int contractId=contract.getId();
                if(contractType==3){
                    Double amount = contractDAO.getMoney(contractId);
                    Double amountPlay = accountReceivableDAO.countMoneyByContractId(contractId);
                    if (amountPlay == null) {
                        amountPlay = 0.0;
                    }
                    obj.put("restamount",amount-amountPlay);
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
                    obj.put("restamount",restamount);
                }
                obj.put("amount",contract.getAmount());
                obj.put("customer",contract.getCustomer());
                //曾庆民添加

                array.add(obj);
            }
        }
        result.put("arr",array);
        result.put("totalRecords",commissions.getTotal());

        return Message.createSuc(result);
    }


    public void insertCommission(int contractId, int actualId, int percentage, Date dueDate,int salerId) {
        Commission commission = new Commission();
        commission.setActualId(actualId);
        commission.setContractId(contractId);
        commission.setSignDate(contractDAO.selectByPrimaryKey(contractId).getSignDate());
        commission.setDueDate(dueDate);
        commission.setPercentage(percentage);
        commission.setMoney(accountActualDAO.selectByPrimaryKey(actualId).getReceivable()*percentage/100);
        commission.setSalerId(salerId);
        commissionDAO.insertSelective(commission);
    }


    public Message confirmCommission(int id,String finishDate, String remark) throws ParseException {

        Commission commission = commissionDAO.selectByPrimaryKey(id);
        commission.setFinishDate(format.parse(finishDate));
        commission.setStatus(4);
        commission.setRemark(remark);
        commission.setPayTag(true);
        commissionDAO.updateByPrimaryKey(commission);

        return Message.createSuc(null);
    }


    public Message getCommissionInfo(int id) {

        Commission commission = commissionDAO.selectByPrimaryKey(id);
        JSONObject obj = new JSONObject();
        obj.put("actualMoney",accountActualDAO.selectByPrimaryKey(commission.getActualId()).getReceivable());
        obj.put("salerName",userDAO.selectByPrimaryKey(commission.getSalerId()).getName());
        obj.put("percentage",commission.getPercentage());
        obj.put("commission",commission.getMoney());

        return Message.createSuc(obj);
    }


}

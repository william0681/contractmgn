package com.zx.servicereminder.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zx.servicereminder.pojo.Message;
import com.zx.servicereminder.mapper.*;
import com.zx.servicereminder.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class ReminderService  {

    @Autowired
    ReminderDAO reminderDAO;
    @Autowired
    ContractDAO contractDAO;
    @Autowired
    BlockDAO blockDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    BlockUserDAO blockUserDAO;


    public Message getReminder(int type, int pageNum,int record,int userId,String role) {

        HashMap<String,Object> result = new HashMap<>();
        JSONArray arr = new JSONArray();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        ReminderExample example = new ReminderExample();
        if(role.equals("areachief")||role.equals("operator")) {
            BlockUserExample blockUserExample = new BlockUserExample();
            if(role.equals("areachief")) {
                blockUserExample.or().andUserIdEqualTo(userId);
            }else{
                blockUserExample.or().andOperateIdEqualTo(userId);
            }
            List<BlockUser> blockUsers = blockUserDAO.selectByExample(blockUserExample);
            if (blockUsers.size() == 1) {
                int blockId = blockUsers.get(0).getBlockId();
                ContractExample contractExample = new ContractExample();
                contractExample.or().andBlockEqualTo(blockId);
                List<Contract> contracts = contractDAO.selectByExample(contractExample);
                if (contracts.size() != 0) {
                    List<Integer> contractIdList = new ArrayList<>();
                    for (Contract contract : contracts) {
                        contractIdList.add(contract.getId());
                    }
                    example.or().andContractIdIn(contractIdList).andTypeEqualTo(type).andOperatorNotEqualTo(userId);
                }else{
                    result.put("arr",arr);
                    result.put("totalRecord",0);
                    return Message.createSuc(result);
                }
            }else{
                result.put("arr",arr);
                result.put("totalRecord",0);
                return Message.createSuc(result);
            }
        }else {
            example.or().andTypeEqualTo(type).andOperatorNotEqualTo(userId);
        }
        if (role.equals("manager")) {
            example.setOrderByClause("manager asc,operate_time ,id desc");
        }
        if (role.equals("accountant")) {
            example.setOrderByClause("accountant asc,operate_time desc,id desc");
        }
        if (role.equals("overallchief")) {
            example.setOrderByClause("overallchief asc,operate_time desc,id desc");
        }
        if (role.equals("areachief")) {
            example.setOrderByClause("areachief asc,operate_time desc,id desc");
        }
        if (role.equals("buyer")) {
            example.setOrderByClause("buyer asc,operate_time desc,id desc");
        }
        if (role.equals("operator")) {
            example.setOrderByClause("responser asc,operate_time desc,id desc");
        }

        PageHelper.startPage(pageNum,record);
        List<Reminder> messages = reminderDAO.selectByExample(example);
        PageInfo<Reminder> messageList = new PageInfo<>(messages);
        List<Reminder> list = messageList.getList();
        for(Reminder reminder : list){
            JSONObject obj = new JSONObject();
            Contract contract = contractDAO.selectByPrimaryKey(reminder.getContractId());
            if(contract!=null) {
                obj.put("id", reminder.getId());
                obj.put("number", contract.getNumber());
                obj.put("name", contract.getName());
                obj.put("contract_id", contract.getId());
                obj.put("tag", contract.getOperationTag());
                obj.put("block", blockDAO.selectByPrimaryKey(contract.getBlock()).getName());
                obj.put("updateType", reminder.getOperateType());
                obj.put("operater", userDAO.selectByPrimaryKey(reminder.getOperator()).getName());
                obj.put("operaterName", userDAO.selectByPrimaryKey(userId).getName());
                obj.put("operaterTime", format.format(reminder.getOperateTime()));
                if (role.equals("manager")) {
                    obj.put("read", reminder.getManager());
                }
                if (role.equals("accountant")) {
                    obj.put("read", reminder.getAccountant());
                }
                if (role.equals("overallchief")) {
                    obj.put("read", reminder.getOverallchief());
                }
                if (role.equals("areachief")) {
                    obj.put("read", reminder.getAreachief());
                }
                if (role.equals("buyer")) {
                    obj.put("read", reminder.getBuyer());
                }
                if (role.equals("operator")) {
                    obj.put("read", reminder.getResponser());
                }
                arr.add(obj);
            }
        }


        result.put("arr",arr);
        result.put("totalRecord",messageList.getTotal());

        return Message.createSuc(result);
    }


    public Message readReminderInContract(int id,String role) {

        Reminder reminder = reminderDAO.selectByPrimaryKey(id);
        ReminderExample example = new ReminderExample();
        example.or().andContractIdEqualTo(reminder.getContractId()).andTypeEqualTo(reminder.getType());
        List<Reminder> reminders = reminderDAO.selectByExample(example);
        if (role.equals("manager")) {
            for(Reminder reminder1 : reminders) {
                reminder1.setManager(true);
                reminderDAO.updateByPrimaryKey(reminder1);
            }
        }
        if (role.equals("accountant")) {
            for(Reminder reminder1 : reminders) {
                reminder1.setAccountant(true);
                reminderDAO.updateByPrimaryKey(reminder1);
            }
        }
        if (role.equals("overallchief")) {
            for(Reminder reminder1 : reminders) {
                reminder1.setOverallchief(true);
                reminderDAO.updateByPrimaryKey(reminder1);
            }
        }
        if(role.equals("areachief")){
            for(Reminder reminder1 : reminders) {
                reminder1.setAreachief(true);
                reminderDAO.updateByPrimaryKey(reminder1);
            }
        }
        if(role.equals("buyer")){
            for(Reminder reminder1 : reminders) {
                reminder1.setBuyer(true);
                reminderDAO.updateByPrimaryKey(reminder1);
            }
        }
        if(role.equals("operator")){
            for(Reminder reminder1 : reminders) {
                reminder1.setResponser(true);
                reminderDAO.updateByPrimaryKey(reminder1);
            }
        }

        return Message.createSuc(null);
    }


    public Message readAllReminder(int type,String role) {

        ReminderExample example = new ReminderExample();
        if (role.equals("manager")) {
            example.or().andManagerEqualTo(false).andTypeEqualTo(type);
            List<Reminder> reminders = reminderDAO.selectByExample(example);
            for(Reminder reminder : reminders){
                reminder.setManager(true);
                reminderDAO.updateByPrimaryKey(reminder);
            }
        }
        if (role.equals("accountant")) {
            example.or().andAccountantEqualTo(false).andTypeEqualTo(type);
            List<Reminder> reminders = reminderDAO.selectByExample(example);
            for(Reminder reminder : reminders){
                reminder.setAccountant(true);
                reminderDAO.updateByPrimaryKey(reminder);
            }
        }
        if (role.equals("overallchief")) {
            example.or().andOverallchiefEqualTo(false).andTypeEqualTo(type);
            List<Reminder> reminders = reminderDAO.selectByExample(example);
            for(Reminder reminder : reminders){
                reminder.setOverallchief(true);
                reminderDAO.updateByPrimaryKey(reminder);
            }
        }
        if(role.equals("areachief")){
            example.or().andAreachiefEqualTo(false).andTypeEqualTo(type);
            List<Reminder> reminders = reminderDAO.selectByExample(example);
            for(Reminder reminder : reminders){
                reminder.setAreachief(true);
                reminderDAO.updateByPrimaryKey(reminder);
            }
        }
        if(role.equals("buyer")){
            example.or().andBuyerEqualTo(false).andTypeEqualTo(type);
            List<Reminder> reminders = reminderDAO.selectByExample(example);
            for(Reminder reminder : reminders){
                reminder.setBuyer(true);
                reminderDAO.updateByPrimaryKey(reminder);
            }
        }
        if(role.equals("operator")){
            example.or().andResponserEqualTo(false).andTypeEqualTo(type);
            List<Reminder> reminders = reminderDAO.selectByExample(example);
            for(Reminder reminder : reminders){
                reminder.setResponser(true);
                reminderDAO.updateByPrimaryKey(reminder);
            }
        }

        return Message.createSuc(null);
    }


    public void insertReminder(int contractId, int type, String operateType,int userId) {

        Reminder reminder = new Reminder();

        reminder.setContractId(contractId);
        reminder.setOperateTime(new Date());
        reminder.setOperateType(operateType);
        reminder.setOperator(userId);
        reminder.setType(type);

        reminderDAO.insertSelective(reminder);

    }


    public Message countUnreadReminder(int userId,String role) {

        int unread = 0;

        ReminderExample example = new ReminderExample();
        if(role.equals("manager")){
            example.or().andManagerEqualTo(false).andOperatorNotEqualTo(userId);
            unread = (int)reminderDAO.countByExample(example);
        }
        if(role.equals("accountant")){
            example.or().andAccountantEqualTo(false).andOperatorNotEqualTo(userId);
            unread = (int)reminderDAO.countByExample(example);
        }
        if(role.equals("overallchief")){
            example.or().andOverallchiefEqualTo(false).andOperatorNotEqualTo(userId);
            unread = (int)reminderDAO.countByExample(example);
        }
        if(role.equals("areachief")){
            BlockUserExample example1 = new BlockUserExample();
            example1.or().andUserIdEqualTo(userId);
            List<BlockUser> blockUsers = blockUserDAO.selectByExample(example1);
            if(blockUsers.size()==1){
                Integer blockId = blockUsers.get(0).getBlockId();
                ContractExample contractExample = new ContractExample();
                contractExample.or().andBlockEqualTo(blockId);
                List<Contract> contracts = contractDAO.selectByExample(contractExample);
                if(contracts.size()!=0){
                    List<Integer> contractIdList = new ArrayList<>();
                    for(Contract contract : contracts){
                        contractIdList.add(contract.getId());
                    }

                    example.or().andContractIdIn(contractIdList).andAreachiefEqualTo(false).andOperatorNotEqualTo(userId);
                    unread = (int)reminderDAO.countByExample(example);
                }
            }
        }
        if(role.equals("buyer")){
            example.or().andBuyerEqualTo(false).andTypeEqualTo(1).andOperatorNotEqualTo(userId);
            unread = (int)reminderDAO.countByExample(example);
        }
        if(role.equals("operator")){
            BlockUserExample example1 = new BlockUserExample();
            example1.or().andOperateIdEqualTo(userId);
            List<BlockUser> blockUsers = blockUserDAO.selectByExample(example1);
            if(blockUsers.size()==1){
                Integer blockId = blockUsers.get(0).getBlockId();
                ContractExample contractExample = new ContractExample();
                contractExample.or().andBlockEqualTo(blockId);
                List<Contract> contracts = contractDAO.selectByExample(contractExample);
                if(contracts.size()!=0){
                    List<Integer> contractIdList = new ArrayList<>();
                    for(Contract contract : contracts){
                        contractIdList.add(contract.getId());
                    }

                    example.or().andContractIdIn(contractIdList).andResponserEqualTo(false).andOperatorNotEqualTo(userId);
                    unread = (int)reminderDAO.countByExample(example);
                }
            }
        }

        JSONObject object = new JSONObject();
        object.put("count",unread);

        return Message.createSuc(object);
    }


    public Message readSingleReminder(int id,String role) {

        Reminder reminder = reminderDAO.selectByPrimaryKey(id);
        if (role.equals("manager")) {
            reminder.setManager(true);
        }
        if (role.equals("accountant")) {
            reminder.setAccountant(true);
        }
        if (role.equals("overallchief")) {
            reminder.setOverallchief(true);
        }
        if(role.equals("areachief")){
            reminder.setAreachief(true);
        }
        if(role.equals("buyer")){
            reminder.setBuyer(true);
        }
        if(role.equals("operator")){
            reminder.setResponser(true);
        }

        reminderDAO.updateByPrimaryKey(reminder);

        return Message.createSuc(null);
    }


}

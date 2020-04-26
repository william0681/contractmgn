package com.zx.servicegateway.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zx.servicegateway.mapper.BlockUserDAO;
import com.zx.servicegateway.mapper.ContractSellerDAO;
import com.zx.servicegateway.mapper.UserDAO;
import com.zx.servicegateway.model.*;
import com.zx.servicegateway.pojo.Message;
import com.zx.servicegateway.service.LoginService;
import com.zx.servicegateway.service.UserService;
import com.zx.servicegateway.util.Md5Util;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    LoginService loginService;
    @Autowired
    UserDAO userDAO;
    @Autowired
    ContractSellerDAO contractSellerDAO;
    @Autowired
    BlockUserDAO blockUserDAO;

    @Override
    public Message addStaff(User user) {

        String password = user.getPassword();
        if(user.getUsername().equals("")||getUserByUserName(user.getUsername())!=null){
            return Message.createErr(501,"username exist or null");
        }
        if(user.getPhone().equals("")||getUserByPhone(user.getPhone())!=null){
            return Message.createErr(502,"phone exist or null");
        }
        if(user.getName().equals("")||getUserByName(user.getName())!=null){
            return Message.createErr(503,"name exist or null");
        }
        user.setPassword(Md5Util.getMD5(password));
        userDAO.insertSelective(user);

        return Message.createSuc(null);
    }

    @Override
    public Message updateStaff(User user) {

        if(user.getRole()!=null) {
            return Message.createErr(503, "cant change role");
        }
        if(user.getUsername()!=null) {
            return Message.createErr(504, "cant change username");
        }

        UserExample example = new UserExample();
        example.or().andNameEqualTo(user.getName());
        List<User> userList = userDAO.selectByExample(example);
        if(userList.size()==1&&!userList.get(0).getId().equals(user.getId())){
            return Message.createErr(501,"name repeat");
        }

        UserExample example1 = new UserExample();
        example1.or().andPhoneEqualTo(user.getPhone());
        List<User> userList1 = userDAO.selectByExample(example1);
        if(userList1.size()==1&&!userList1.get(0).getId().equals(user.getId())){
            return Message.createErr(502,"phone repeat");
        }

        userDAO.updateByPrimaryKeySelective(user);

        return Message.createSuc(null);
    }

    @Override
    public Message getStaffs(Integer role,String departMent, int pageNum,Subject subject) {

        HashMap<String,Object> result = new HashMap<>();

        PageHelper.startPage(pageNum,5);

        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        if(role!=null){
            criteria.andRoleEqualTo(role);
        }
        if(!departMent.equals("")){
            criteria.andDepartmentEqualTo(departMent);
        }
        criteria.andIdNotEqualTo((int) subject.getSession().getAttribute("userId"));
        List<User> userList = userDAO.selectByExample(example);
        PageInfo<User> users = new PageInfo<>(userList);
        List<User> list = users.getList();
        JSONArray array = new JSONArray();
        for(User user : list){
            JSONObject obj = new JSONObject();
            obj.put("id",user.getId());
            obj.put("name",user.getName());
            obj.put("departMent",user.getDepartment());
            obj.put("phone",user.getPhone());
            obj.put("role",user.getRole());
            obj.put("sex",user.getSex());
            array.add(obj);
        }
        result.put("array",array);
        result.put("totalRecord",users.getTotal());

        List<String> departMents = new ArrayList<>();
        List<User> userList1 = userDAO.selectByExample(new UserExample());
        for(User user : userList1){
            if(!departMents.contains(user.getDepartment())){
                departMents.add(user.getDepartment());
            }
        }

        result.put("departMents",departMents);


        return Message.createSuc(result);
    }

    @Override
    public Message resetPassword(int userId) {

        HashMap<String,Object> result = new HashMap<>();

        User user = userDAO.selectByPrimaryKey(userId);
        String resetPWD = RandomStringUtils.randomNumeric(6);
        user.setPassword(Md5Util.getMD5(resetPWD));
        user.setState(false);
        user.setReset(true);
        userDAO.updateByPrimaryKeySelective(user);
        result.put("pwd",resetPWD);

        return Message.createSuc(result);
    }

    @Override
    public Message getStaffInfo(int userId) {

        User user = userDAO.selectByPrimaryKey(userId);
        JSONObject obj = new JSONObject();
        obj.put("name",user.getName());
        obj.put("departMent",user.getDepartment());
        obj.put("phone",user.getPhone());
        obj.put("role",user.getRole());
        obj.put("username",user.getUsername());
        obj.put("sex",user.getSex());

        return Message.createSuc(obj);
    }

    @Override
    public Message setPassWordAfterReset(String originPassword, String password,Subject subject) {

        int userId = (int) subject.getSession().getAttribute("userId");
        User user = userDAO.selectByPrimaryKey(userId);
        if(Md5Util.getMD5(originPassword).equals(user.getPassword())){
            user.setPassword(Md5Util.getMD5(password));
            user.setReset(false);
            userDAO.updateByPrimaryKeySelective(user);
        }
        else{
            return Message.createErr(501,"originPWD wrong");
        }

        return Message.createSuc(null);
    }

    @Override
    public Message searchStaffByName(String name,int pageNum,Subject subject) {

        PageHelper.startPage(pageNum,5);
        HashMap<String,Object> res = new HashMap<>();

        int userId = (int)subject.getSession().getAttribute("userId");

        UserExample example = new UserExample();
        example.or().andNameLike("%"+name+"%");
        List<User> userList = userDAO.selectByExample(example);
        PageInfo<User> users = new PageInfo<>(userList);
        List<User> list = users.getList();
        JSONArray array = new JSONArray();
        for(User user : list){
            if(!user.getId().equals(userId)) {
                JSONObject obj = new JSONObject();
                obj.put("id", user.getId());
                obj.put("name", user.getName());
                obj.put("departMent", user.getDepartment());
                obj.put("phone", user.getPhone());
                obj.put("role", user.getRole());
                array.add(obj);
            }
        }
        res.put("array",array);
        res.put("totalRecord",users.getTotal());

        return Message.createSuc(res);
    }

    @Override
    public Message getSaler() {

        UserExample example = new UserExample();
        example.or().andRoleEqualTo(5);
        List<User> userList = userDAO.selectByExample(example);
        JSONArray array = new JSONArray();
        for(User user: userList){
            JSONObject obj = new JSONObject();
            obj.put("id",user.getId());
            obj.put("name",user.getName());
            array.add(obj);
        }

        return Message.createSuc(array);
    }

    @Override
    public Message getAvailableAreaChief() {

        List<BlockUser> blockUsers = blockUserDAO.selectByExample(new BlockUserExample());
        List<Integer> idList = new ArrayList<>();
        for(BlockUser blockUser : blockUsers){
            if(blockUser.getUserId()!=null&&!idList.contains(blockUser.getUserId())){
                idList.add(blockUser.getUserId());
            }
        }

        UserExample example = new UserExample();
        if(idList.size()==0) {
            example.or().andRoleEqualTo(4);
        }else{
            example.or().andRoleEqualTo(4).andIdNotIn(idList);
        }
        List<User> userList = userDAO.selectByExample(example);
        JSONArray array = new JSONArray();
        for(User user: userList){
            JSONObject obj = new JSONObject();
            obj.put("id",user.getId());
            obj.put("name",user.getName());
            array.add(obj);
        }

        return Message.createSuc(array);
    }

    @Override
    public Message getSalerInContract(int id) {

        ContractSellerExample contractSellerExample = new ContractSellerExample();
        contractSellerExample.or().andContractIdEqualTo(id);
        List<ContractSeller> contractSellers = contractSellerDAO.selectByExample(contractSellerExample);
        JSONArray array = new JSONArray();
        for(ContractSeller contractSeller: contractSellers){
            User user = userDAO.selectByPrimaryKey(contractSeller.getSellerId());
            JSONObject obj = new JSONObject();
            obj.put("id",user.getId());
            obj.put("name",user.getName());
            array.add(obj);
        }

        return Message.createSuc(array);
    }

    @Override
    public Message getAvailableOperator() {
        List<BlockUser> blockUsers = blockUserDAO.selectByExample(new BlockUserExample());
        List<Integer> idList = new ArrayList<>();
        for(BlockUser blockUser : blockUsers){
            if(blockUser.getOperateId()!=null&&!idList.contains(blockUser.getOperateId())){
                idList.add(blockUser.getOperateId());
            }
        }

        UserExample example = new UserExample();
        if(idList.size()==0) {
            example.or().andRoleEqualTo(7);
        }else{
            example.or().andRoleEqualTo(7).andIdNotIn(idList);
        }
        List<User> userList = userDAO.selectByExample(example);
        JSONArray array = new JSONArray();
        for(User user: userList){
            JSONObject obj = new JSONObject();
            obj.put("id",user.getId());
            obj.put("name",user.getName());
            array.add(obj);
        }

        return Message.createSuc(array);
    }

    @Override
    public User getUserByUserName(String username) {

        UserExample userExample = new UserExample();
        userExample.or().andUsernameEqualTo(username);
        List<User> users = userDAO.selectByExample(userExample);
        if(users.size()!=0){
            return users.get(0);
        }
        return null;
    }

    @Override
    public User getUserByPhone(String phone) {

        UserExample userExample = new UserExample();
        userExample.or().andPhoneEqualTo(phone);
        List<User> users = userDAO.selectByExample(userExample);
        if(users.size()!=0){
            return users.get(0);
        }
        return null;
    }

    @Override
    public User getUserByName(String name) {

        UserExample userExample = new UserExample();
        userExample.or().andNameEqualTo(name);
        List<User> users = userDAO.selectByExample(userExample);
        if(users.size()!=0){
            return users.get(0);
        }
        return null;
    }




}

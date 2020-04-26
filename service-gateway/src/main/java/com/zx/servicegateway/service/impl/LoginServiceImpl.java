package com.zx.servicegateway.service.impl;

import com.zx.servicegateway.mapper.BlockUserDAO;
import com.zx.servicegateway.mapper.ContractDAO;
import com.zx.servicegateway.mapper.ReminderDAO;
import com.zx.servicegateway.mapper.UserDAO;
import com.zx.servicegateway.model.*;
import com.zx.servicegateway.pojo.Message;
import com.zx.servicegateway.service.LoginService;
import com.zx.servicegateway.service.UserService;
import com.zx.servicegateway.util.Md5Util;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Autowired
    UserDAO userDAO;

    @Autowired
    UserService userService;
    @Autowired
    ReminderDAO reminderDAO;
    @Autowired
    BlockUserDAO blockUserDAO;
    @Autowired
    ContractDAO contractDAO;

    @Override
    public Message userLogin(User user,HttpServletResponse response) throws UnsupportedEncodingException {

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), Md5Util.getMD5(user.getPassword()));
        try {
            subject.login(token);
        }catch (Exception e){
            if(e instanceof LockedAccountException){
                return Message.createErr(501,"user locked");
            }
            if(e instanceof IncorrectCredentialsException){
                return Message.createErr(502,"wrong pwd");
            }
            if(e instanceof UnknownAccountException){
                return Message.createErr(503,"wrong username");
            }
        }

        User user1 = userService.getUserByUserName(user.getUsername());

        subject.getSession().setAttribute("userId",user1.getId());
        subject.getSession().setAttribute("role",user1.getRole());
        HashMap<String,Object> result = new HashMap<>();
        result.put("reset",user1.getReset());
        result.put("role",user1.getRole());
        result.put("nickname",user1.getName());
        result.put("unread",getUnReadMessageAccount());
        result.put("userId",user1.getId());

        return Message.createSuc(result);
    }



    @Override
    public Message logout() {

        SecurityUtils.getSubject().logout();

        return Message.createSuc(null);
    }

    private int getUnReadMessageAccount(){

        int unread = 0;

        Subject subject = SecurityUtils.getSubject();
        int userId = (int)subject.getSession().getAttribute("userId");
        ReminderExample example = new ReminderExample();
        if(subject.hasRole("manager")){
            example.or().andManagerEqualTo(false).andOperatorNotEqualTo(userId);
            unread = (int)reminderDAO.countByExample(example);
        }
        if(subject.hasRole("accountant")){
            example.or().andAccountantEqualTo(false).andOperatorNotEqualTo(userId);
            unread = (int)reminderDAO.countByExample(example);
        }
        if(subject.hasRole("overallchief")){
            example.or().andOverallchiefEqualTo(false).andOperatorNotEqualTo(userId);
            unread = (int)reminderDAO.countByExample(example);
        }
        if(subject.hasRole("areachief")){
            BlockUserExample example1 = new BlockUserExample();
            example1.or().andUserIdEqualTo((int)SecurityUtils.getSubject().getSession().getAttribute("userId"));
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
        if(subject.hasRole("buyer")){
           example.or().andBuyerEqualTo(false).andTypeEqualTo(1).andOperatorNotEqualTo(userId);
           unread = (int)reminderDAO.countByExample(example);
        }
        if(subject.hasRole("operator")){
            BlockUserExample example1 = new BlockUserExample();
            example1.or().andOperateIdEqualTo((int)SecurityUtils.getSubject().getSession().getAttribute("userId"));
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

        return unread;
    }
}

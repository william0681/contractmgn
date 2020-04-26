package com.zx.servicegateway.controller;

import com.alibaba.fastjson.JSONObject;
import com.zx.servicegateway.mapper.BlockDAO;
import com.zx.servicegateway.mapper.BlockUserDAO;
import com.zx.servicegateway.model.BlockUser;
import com.zx.servicegateway.model.BlockUserExample;
import com.zx.servicegateway.model.User;
import com.zx.servicegateway.pojo.Message;
import com.zx.servicegateway.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    BlockUserDAO blockUserDAO;
    @Autowired
    BlockDAO blockDAO;

    @PostMapping("staff")
    @RequiresRoles(value = {"manager","accountant","overallchief"},logical = Logical.OR)
    public Message addStaff(@RequestBody User user){
    return userService.addStaff(user);
}

    @PutMapping("staff")
    @RequiresRoles(value = {"manager","accountant","overallchief"},logical = Logical.OR)
    public Message updateStaff(@RequestBody User user){
        return userService.updateStaff(user);
    }

    @GetMapping("staff")
    @RequiresRoles(value = {"manager","accountant","overallchief"},logical = Logical.OR)
    Message getStaffs(@RequestParam Integer role, @RequestParam String departMent, @RequestParam int pageNum){
        Subject subject = SecurityUtils.getSubject();
        return userService.getStaffs(role, departMent, pageNum,subject);
    }

    @GetMapping("staff/{id}")
    @RequiresRoles(value = {"manager","accountant","overallchief"},logical = Logical.OR)
    Message getStaffInfo(@PathVariable(value = "id") int id){
        return userService.getStaffInfo(id);
    }

    @GetMapping("password")
    @RequiresRoles(value = {"manager","accountant","overallchief"},logical = Logical.OR)
    Message resetPassword(@RequestParam int id){
        return userService.resetPassword(id);
    }

    @PostMapping("password")
    Message setPassWordAfterReset(@RequestParam String originPassword, @RequestParam String password){
        Subject subject = SecurityUtils.getSubject();
        return userService.setPassWordAfterReset(originPassword, password,subject);
    }

    @GetMapping("staff/key")
    @RequiresRoles(value = {"manager","accountant","overallchief"},logical = Logical.OR)
    Message searchStaffByName(@RequestParam String name, @RequestParam int pageNum){
        Subject subject = SecurityUtils.getSubject();
        return userService.searchStaffByName(name,pageNum,subject);
    }

    @GetMapping("staff/saler")
    @RequiresRoles(value = {"manager","accountant","overallchief","areachief","operator"},logical = Logical.OR)
    Message getSaler(){
        return userService.getSaler();
    }

    @GetMapping("staff/saler/{id}")
    @RequiresRoles(value = {"manager","accountant","overallchief","areachief","operator"},logical = Logical.OR)
    Message getSalerInContract(@PathVariable(value = "id")int id){
        return userService.getSalerInContract(id);
    }

    @GetMapping("staff/areachief")
    @RequiresRoles(value = {"manager","accountant","overallchief"},logical = Logical.OR)
    Message getAreaChief(){
        return userService.getAvailableAreaChief();
    }

    @GetMapping("staff/operator")
    @RequiresRoles(value = {"manager","accountant","overallchief"},logical = Logical.OR)
    Message getOperator(){
        return userService.getAvailableOperator();
    }

    @GetMapping("staff/block")
    @RequiresRoles(value = {"areachief","operator"},logical = Logical.OR)
    Message getBelongBlock(){

        int blockId = 0;

        Subject subject = SecurityUtils.getSubject();
        int userId = (int)subject.getSession().getAttribute("userId");
        BlockUserExample example = new BlockUserExample();
        if(subject.hasRole("areachief")) {
            example.or().andUserIdEqualTo(userId);
        }else {
            example.or().andOperateIdEqualTo(userId);
        }
        List<BlockUser> blockUsers = blockUserDAO.selectByExample(example);
        if(blockUsers.size()==1){
            blockId = blockUsers.get(0).getBlockId();
        }

        JSONObject object = new JSONObject();
        object.put("blockId",blockId);
        if(blockId!=0) {
            object.put("blockName", blockDAO.selectByPrimaryKey(blockId).getName());
        }

        return Message.createSuc(object);

    }

    //检测登录状态
    @GetMapping("staff/hh")
    @RequiresRoles(value = {"manager", "accountant", "overallchief","areachief","saler","buyer","operator"},logical = Logical.OR)
    Message hahah(){
        return Message.createSuc(null);

    }

}

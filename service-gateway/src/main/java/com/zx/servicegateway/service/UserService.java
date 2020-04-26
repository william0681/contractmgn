package com.zx.servicegateway.service;

import com.zx.servicegateway.model.User;
import com.zx.servicegateway.pojo.Message;
import org.apache.shiro.subject.Subject;

public interface UserService {

    Message addStaff(User user);

    Message updateStaff(User user);

    Message getStaffs(Integer role, String departMent, int pageNum, Subject subject);

    Message resetPassword(int userId);

    Message getStaffInfo(int userId);

    Message setPassWordAfterReset(String originPassword, String password, Subject subject);

    Message searchStaffByName(String name, int pageNum, Subject subject);

    Message getSaler();

    Message getAvailableAreaChief();

    Message getSalerInContract(int id);

    Message getAvailableOperator();

    User getUserByUserName(String username);

    User getUserByPhone(String number);

    User getUserByName(String name);
}
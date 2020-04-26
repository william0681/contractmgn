package com.zx.servicegateway.controller;

import com.zx.servicegateway.model.User;
import com.zx.servicegateway.pojo.Message;
import com.zx.servicegateway.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

@RestController
public class LoginController {

    @Autowired
    LoginService loginService;

    @PostMapping("login")
    public Message userLogin(User user,HttpServletResponse response)throws UnsupportedEncodingException {
        return loginService.userLogin(user,response);
    }

    @GetMapping("logout")
    public Message logout(){
        return loginService.logout();
    }

}

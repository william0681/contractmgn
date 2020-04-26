package com.zx.servicegateway.config;

import com.zx.servicegateway.pojo.Message;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class ExceptionHandlerAdvice {

    @ExceptionHandler(Exception.class)
    public Message handleException(Exception e) {
        e.printStackTrace();
        return Message.createErr(500,"服务器出错");
    }

    @ExceptionHandler(AuthorizationException.class)
    public Message handleAuthorizationException(AuthorizationException e){
        e.printStackTrace();
        return Message.createErr(403,"没有权限");
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public Message handleAuthorizationException(UnauthenticatedException e){
        e.printStackTrace();
        return Message.createErr(401,"未登录");
    }

}

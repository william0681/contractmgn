package com.zx.servicegateway.service;

import com.zx.servicegateway.model.User;
import com.zx.servicegateway.pojo.Message;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

public interface LoginService {

    Message userLogin(User user, HttpServletResponse response) throws UnsupportedEncodingException;

    Message logout();
}

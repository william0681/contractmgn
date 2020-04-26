package com.zx.servicegateway.mapper;

import com.zx.servicegateway.model.User;
import com.zx.servicegateway.model.UserExample;
import org.springframework.stereotype.Repository;

/**
 * UserDAO继承基类
 */
@Repository
public interface UserDAO extends MyBatisBaseDao<User, Integer, UserExample> {
}
package com.zx.servicecontract.mapper;

import com.zx.servicecontract.model.User;
import com.zx.servicecontract.model.UserExample;
import org.springframework.stereotype.Repository;

/**
 * UserDAO继承基类
 */
@Repository
public interface UserDAO extends MyBatisBaseDao<User, Integer, UserExample> {
}
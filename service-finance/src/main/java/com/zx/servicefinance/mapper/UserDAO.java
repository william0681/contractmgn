package com.zx.servicefinance.mapper;

import com.zx.servicefinance.model.User;
import com.zx.servicefinance.model.UserExample;
import org.springframework.stereotype.Repository;

/**
 * UserDAO继承基类
 */
@Repository
public interface UserDAO extends MyBatisBaseDao<User, Integer, UserExample> {
}
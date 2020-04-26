package com.zx.servicegateway.mapper;

import com.zx.servicegateway.model.BlockUser;
import com.zx.servicegateway.model.BlockUserExample;
import org.springframework.stereotype.Repository;

/**
 * BlockUserDAO继承基类
 */
@Repository
public interface BlockUserDAO extends MyBatisBaseDao<BlockUser, Integer, BlockUserExample> {
}
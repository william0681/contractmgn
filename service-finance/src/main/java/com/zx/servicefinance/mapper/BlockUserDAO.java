package com.zx.servicefinance.mapper;

import com.zx.servicefinance.model.BlockUser;
import com.zx.servicefinance.model.BlockUserExample;
import org.springframework.stereotype.Repository;

/**
 * BlockUserDAO继承基类
 */
@Repository
public interface BlockUserDAO extends MyBatisBaseDao<BlockUser, Integer, BlockUserExample> {
}
package com.zx.servicecontract.mapper;

import com.zx.servicecontract.model.BlockUser;
import com.zx.servicecontract.model.BlockUserExample;
import org.springframework.stereotype.Repository;

/**
 * BlockUserDAO继承基类
 */
@Repository
public interface BlockUserDAO extends MyBatisBaseDao<BlockUser, Integer, BlockUserExample> {
}
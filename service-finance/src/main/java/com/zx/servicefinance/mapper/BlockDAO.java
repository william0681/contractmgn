package com.zx.servicefinance.mapper;

import com.zx.servicefinance.model.Block;
import com.zx.servicefinance.model.BlockExample;
import org.springframework.stereotype.Repository;

/**
 * BlockDAO继承基类
 */
@Repository
public interface BlockDAO extends MyBatisBaseDao<Block, Integer, BlockExample> {
}
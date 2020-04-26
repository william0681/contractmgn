package com.zx.servicecontract.mapper;

import com.zx.servicecontract.model.ExecuteNode;
import com.zx.servicecontract.model.ExecuteNodeExample;
import org.springframework.stereotype.Repository;

/**
 * ExecuteNodeDAO继承基类
 */
@Repository
public interface ExecuteNodeDAO extends MyBatisBaseDao<ExecuteNode, Integer, ExecuteNodeExample> {
}
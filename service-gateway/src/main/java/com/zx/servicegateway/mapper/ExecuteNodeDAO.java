package com.zx.servicegateway.mapper;

import com.zx.servicegateway.model.ExecuteNode;
import com.zx.servicegateway.model.ExecuteNodeExample;
import org.springframework.stereotype.Repository;

/**
 * ExecuteNodeDAO继承基类
 */
@Repository
public interface ExecuteNodeDAO extends MyBatisBaseDao<ExecuteNode, Integer, ExecuteNodeExample> {
}
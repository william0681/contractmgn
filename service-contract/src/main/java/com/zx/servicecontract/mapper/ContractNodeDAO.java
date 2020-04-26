package com.zx.servicecontract.mapper;

import com.zx.servicecontract.model.ContractNode;
import com.zx.servicecontract.model.ContractNodeExample;
import org.springframework.stereotype.Repository;

/**
 * ContractNodeDAO继承基类
 */
@Repository
public interface ContractNodeDAO extends MyBatisBaseDao<ContractNode, Integer, ContractNodeExample> {
}
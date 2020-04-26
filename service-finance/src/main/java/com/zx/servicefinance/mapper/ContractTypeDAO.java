package com.zx.servicefinance.mapper;

import com.zx.servicefinance.model.ContractType;
import com.zx.servicefinance.model.ContractTypeExample;
import org.springframework.stereotype.Repository;

/**
 * ContractTypeDAO继承基类
 */
@Repository
public interface ContractTypeDAO extends MyBatisBaseDao<ContractType, Integer, ContractTypeExample> {
}
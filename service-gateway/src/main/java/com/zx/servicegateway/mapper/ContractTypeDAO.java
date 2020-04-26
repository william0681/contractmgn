package com.zx.servicegateway.mapper;

import com.zx.servicegateway.model.ContractType;
import com.zx.servicegateway.model.ContractTypeExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * ContractTypeDAO继承基类
 */
@Repository
public interface ContractTypeDAO extends MyBatisBaseDao<ContractType, Integer, ContractTypeExample> {

    @Select("select * from contract_type where id=#{contractTypeId}")
    ContractType selectContractTypeById(@Param("contractTypeId") Integer contractTypeId);
}
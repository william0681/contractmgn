package com.zx.servicecontract.mapper;

import com.zx.servicecontract.model.ContractSeller;
import com.zx.servicecontract.model.ContractSellerExample;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * ContractSellerDAO继承基类
 */
@Repository
public interface ContractSellerDAO extends MyBatisBaseDao<ContractSeller, Integer, ContractSellerExample> {
    @Delete("delete from contract_seller where contract_id=#{contractId}")
    void deleteSellerByContractId(@Param("contractId") Integer contractId);
}
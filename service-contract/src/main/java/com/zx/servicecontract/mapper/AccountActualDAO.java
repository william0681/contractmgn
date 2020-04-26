package com.zx.servicecontract.mapper;

import com.zx.servicecontract.model.AccountActual;
import com.zx.servicecontract.model.AccountActualExample;
import org.springframework.stereotype.Repository;

/**
 * AccountActualDAO继承基类
 */
@Repository
public interface AccountActualDAO extends MyBatisBaseDao<AccountActual, Integer, AccountActualExample> {
}
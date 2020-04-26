package com.zx.servicegateway.mapper;

import com.zx.servicegateway.model.AccountActual;
import com.zx.servicegateway.model.AccountActualExample;
import org.springframework.stereotype.Repository;

/**
 * AccountActualDAO继承基类
 */
@Repository
public interface AccountActualDAO extends MyBatisBaseDao<AccountActual, Integer, AccountActualExample> {
}
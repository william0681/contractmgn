package com.zx.servicefinance.mapper;

import com.zx.servicefinance.model.AccountActual;
import com.zx.servicefinance.model.AccountActualExample;
import org.springframework.stereotype.Repository;

/**
 * AccountActualDAO继承基类
 */
@Repository
public interface AccountActualDAO extends MyBatisBaseDao<AccountActual, Integer, AccountActualExample> {
}
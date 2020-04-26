package com.zx.servicefinance.mapper;

import com.zx.servicefinance.model.Commission;
import com.zx.servicefinance.model.CommissionExample;
import org.springframework.stereotype.Repository;

/**
 * CommissionDAO继承基类
 */
@Repository
public interface CommissionDAO extends MyBatisBaseDao<Commission, Integer, CommissionExample> {
}
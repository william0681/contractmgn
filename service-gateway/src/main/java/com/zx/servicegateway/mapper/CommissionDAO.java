package com.zx.servicegateway.mapper;

import com.zx.servicegateway.model.Commission;
import com.zx.servicegateway.model.CommissionExample;
import org.springframework.stereotype.Repository;

/**
 * CommissionDAO继承基类
 */
@Repository
public interface CommissionDAO extends MyBatisBaseDao<Commission, Integer, CommissionExample> {
    
}
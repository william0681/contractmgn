package com.zx.serviceschedule.mapper;

import com.zx.serviceschedule.model.Commission;
import com.zx.serviceschedule.model.CommissionExample;
import org.springframework.stereotype.Repository;

/**
 * CommissionDAO继承基类
 */
@Repository
public interface CommissionDAO extends MyBatisBaseDao<Commission, Integer, CommissionExample> {
    
}
package com.zx.servicegateway.mapper;

import com.zx.servicegateway.model.Reminder;
import com.zx.servicegateway.model.ReminderExample;
import org.springframework.stereotype.Repository;

/**
 * ReminderDAO继承基类
 */
@Repository
public interface ReminderDAO extends MyBatisBaseDao<Reminder, Integer, ReminderExample> {
}
package com.zx.servicecontract.mapper;

import com.zx.servicecontract.model.ActuralTicket;
import com.zx.servicecontract.model.ActuralTicketExample;
import org.springframework.stereotype.Repository;

/**
 * ActuralTicketDAO继承基类
 */
@Repository
public interface ActuralTicketDAO extends MyBatisBaseDao<ActuralTicket, Integer, ActuralTicketExample> {
}
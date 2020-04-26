package com.zx.servicecontract.mapper;

import com.zx.servicecontract.model.InvoiceReceivable;
import com.zx.servicecontract.model.InvoiceReceivableExample;
import org.springframework.stereotype.Repository;

/**
 * InvoiceReceivableDAO继承基类
 */
@Repository
public interface InvoiceReceivableDAO extends MyBatisBaseDao<InvoiceReceivable, Integer, InvoiceReceivableExample> {
}
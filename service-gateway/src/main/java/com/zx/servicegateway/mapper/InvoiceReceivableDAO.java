package com.zx.servicegateway.mapper;

import com.zx.servicegateway.model.InvoiceReceivable;
import com.zx.servicegateway.model.InvoiceReceivableExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * InvoiceReceivableDAO继承基类
 */
@Repository
public interface InvoiceReceivableDAO extends MyBatisBaseDao<InvoiceReceivable, Integer, InvoiceReceivableExample> {
    @Select("select sum(invoice_money) from invoice_receivable where contract_id=#{contractId}")
    Double countMoneyByContractId(@Param("contractId") Integer contractId);//计算合同剩余应开票金额

    @Select("select count(id) from invoice_receivable where contract_id=#{contractId}")
    Integer getInvoiceReNum(@Param("contractId") Integer contractId);//获取该合同应已开票记录
}
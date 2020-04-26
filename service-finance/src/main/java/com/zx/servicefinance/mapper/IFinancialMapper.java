package com.zx.servicefinance.mapper;

import com.zx.servicefinance.model.Financial;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * IFinancialMapper
 */
@Repository
public interface IFinancialMapper {

    /**
     *
     */

    /**
     * 查询钱财
     */
    public static final String SHOWMONEY = "select contract_id , total_amount, (total_amount - payment) as un_payment from financial where contract_id = #{value} ";

    @Select(SHOWMONEY)
    Financial showMoney(Integer id);

}
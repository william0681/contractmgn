package com.zx.servicefinance.service;

import com.zx.servicefinance.mapper.IFinancialMapper;
import com.zx.servicefinance.model.Financial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * FinancialService
 */
@Service
public class FinancialService{

    @Autowired
    private IFinancialMapper financialMapper;
    
    public Financial showMoney(Integer id) {
        return financialMapper.showMoney(id);
    }
    
}
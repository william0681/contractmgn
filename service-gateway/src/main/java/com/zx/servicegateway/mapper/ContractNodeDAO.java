package com.zx.servicegateway.mapper;

import com.zx.servicegateway.model.ContractNode;
import com.zx.servicegateway.model.ContractNodeExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ContractNodeDAO继承基类
 */
@Repository
public interface ContractNodeDAO extends MyBatisBaseDao<ContractNode, Integer, ContractNodeExample> {
    @Select("select LAST_INSERT_ID()")
    int lastRecordId();

    @Select("select id from contract_node where contract_id=#{contractId} and(name='预收款' " +
            "or name='进度款' " +
            "or name='发货款' " +
            "or name='验收款'" +
            "or name='质保款')")
    List<Integer> getAfterDeposit(@Param("contractId") Integer contractId);//定金之后的节点id

    @Select("select id from contract_node where contract_id=#{contractId} and(name='进度款' " +
            "or name='发货款' " +
            "or name='验收款'" +
            "or name='质保款')")
    List<Integer> getAfterReceivedAdvance(@Param("contractId") Integer contractId);//预收款之后的节点id

    @Select("select id from contract_node where contract_id=#{contractId} and(name='发货款' " +
            "or name='验收款'" +
            "or name='质保款')")
    List<Integer> getAfterProgressPay(@Param("contractId") Integer contractId);//进度款之后的节点id

    @Select("select id from contract_node where contract_id=#{contractId} and(name='验收款' " +
            "or name='质保款')")
    List<Integer> getAfterDeliveryPay(@Param("contractId") Integer contractId);//发货款之后的节点id

    @Select("select id from contract_node where contract_id=#{contractId} and(name='质保款' )")
    List<Integer> getAfterAcceptPay(@Param("contractId") Integer contractId);//验收款之后的节点id

    @Select("select id from contract_node where contract_id=#{contractId} and(name='定金'" +
            " or name='预收款')")
    List<Integer> getBeforeReceivedAdvance(@Param("contractId") Integer contractId);//预收款之前的节点id

    @Select("select id from contract_node where contract_id=#{contractId} and(name='定金' " +
            "or name='预收款'" +
            "or name='进度款')")
    List<Integer> getBeforeProgressPay(@Param("contractId") Integer contractId);//进度款之前的节点id

    @Select("select id from contract_node where contract_id=#{contractId} and(name='定金' " +
            "or name='预收款'" +
            "or name='进度款'" +
            "or name='发货款')")
    List<Integer> getBeforeDeliveryPay(@Param("contractId") Integer contractId);//发货款之前的节点id

    @Select("select id from contract_node where contract_id=#{contractId} and(name='定金' " +
            "or name='预收款' " +
            "or name='进度款'" +
            "or name='发货款'" +
            "or name='验收款')")
    List<Integer> getBeforeAcceptPay(@Param("contractId") Integer contractId);//验收款之前的节点id

    @Select("select id from contract_node where contract_id=#{contractId} and(name='定金' " +
            "or name='预收款' " +
            "or name='进度款'" +
            "or name='发货款'" +
            "or name='验收款'" +
            "or name='质保金')")
    List<Integer> getBeforeRetention(@Param("contractId") Integer contractId);//质保金之前的节点id


}
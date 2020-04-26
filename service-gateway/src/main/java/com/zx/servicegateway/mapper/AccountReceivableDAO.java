package com.zx.servicegateway.mapper;

import com.zx.servicegateway.model.AccountReceivable;
import com.zx.servicegateway.model.AccountReceivableExample;
import com.zx.servicegateway.provider.AccountProvider;
import com.zx.servicegateway.vo.AccountInfo;
import com.zx.servicegateway.vo.AccountVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * AccountReceivableDAO继承基类
 */
@Repository
public interface AccountReceivableDAO extends MyBatisBaseDao<AccountReceivable, Integer, AccountReceivableExample> {
    @Select("select LAST_INSERT_ID()")
    int lastRecordId();

    @Select("select sum(money) from account_receivable where node_id=#{nodeId}")
    Double countMoneyByNodeId(@Param("nodeId") Integer nodeId);

    @Select("select sum(money) from account_receivable where node_id=#{nodeId}")
    Double countNodeActualMoney(@Param("nodeId") Integer nodeId);

    @Select("select sum(actual_money) from account_receivable where node_id=#{nodeId}")
    Double countActualMoney(@Param("nodeId") Integer nodeId);

    @Select("select * from account_receivable where id=1")
    AccountReceivable checkRecord();//确保应收款表中有id=1这条用于暂时连表的数据
    @Select("select * from account_receivable where id=2")
    AccountReceivable checkRecord2();//确保应收款表中有id=2这条用于表示录入系统的应收款已完成，但整个合同未完成的状态

    @Insert("insert into account_receivable (id,contract_id,node_id,start_date,end_date,remark,status) values(1,0,0,'2019-1-1','2019-1-2','系统生成数据，表示未添加',4)")
    Integer insertRecord();//往应收款表插入用于连表的数据
    @Insert("insert into account_receivable (id,contract_id,node_id,stage,start_date,end_date,remark,status) values(2,0,0,-1,'2019-1-1','2019-1-2','系统生成数据，表示进行中',3)")
    Integer insertRecord2();//往应收款表插入用于表示录入系统的应收款已完成，但整个合同未完成的状态的数据

    @Select("select a.stage stage," +
            "a.end_date receivable_date," +
            "a.money receivable_money," +
            "a.actual_money actual_money," +
            "a.status status," +
            "a.remark remark," +
            "cn.name node_name" +
            " from account_receivable a" +
            " join contract_node cn on cn.id=a.node_id" +
            " where a.contract_id=#{contract_id} order by a.end_date")//   " order by a.end_date"

    List<AccountVo> getAccountList(@Param("contract_id") Integer contract_id);

    //    @Select("select c.id contract_id," +
//            "c.number number," +
//            "c.name name," +
//            "c.amount account_receivable," +
//            "(c.amount-c.rest_amount) account_actual," +
//            "c.operation_tag contractTag," +
//            "b.name block," +
//            "t.name contract_type," +
//            "ct.name content_type," +
//            "cn.name contract_node," +
//            "ar.stage stage," +
//            "ar.status status," +
//            "ar.days days " +
//            "from contract c " +
//            " join contract_type t on t.id=c.contract_type " +
//            " join content_type ct on ct.id=c.content_type" +
//            " join account_receivable ar on ar.id=c.account_id" +
//            " left join contract_node cn on cn.id=ar.node_id" +
//            " join block b on b.id = c.block " +
//            " order by ar.end_date asc")
    @SelectProvider(type = AccountProvider.class, method = "getAccounInfo")
    List<AccountInfo> getAccountInfo(@Param("key") String key, @Param("start") Date start, @Param("end") Date end, @Param("userId") Integer userId);//

    @Select("select count(id) from account_receivable where contract_id=#{contractId} and node_id=#{nodeId}")
    Integer getRecordNum(@Param("contractId") Integer contractId, @Param("nodeId") Integer nodeId);

    @Select("select sum(money) from account_receivable where contract_id=#{contractId}")
    Double countMoneyByContractId(@Param("contractId") Integer contractId);
}
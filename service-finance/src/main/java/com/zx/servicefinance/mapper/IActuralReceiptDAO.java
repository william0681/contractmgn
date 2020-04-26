package com.zx.servicefinance.mapper;

import com.zx.servicefinance.model.AccountActual;
import com.zx.servicefinance.model.ActuralReceipt;
import com.zx.servicefinance.model.ActuralTicket;
import com.zx.servicefinance.model.ReceiptModel;
import com.zx.servicefinance.provider.ContractProvider;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * IActuralReceiptDAO
 */
@Repository
public interface IActuralReceiptDAO {

    /**
     *
     */

    @Delete("delete from account_actual where id=#{id}")
    Integer deleteReceipt(@Param("id") Integer id);//删除实收款，min

    @Results({@Result(column = "id", property = "sellers", one = @One(select = "com.zx.servicefinance.mapper.IActuralReceiptDAO.getSellers"))})
    @Select(" select  id , " + " receivable receipt ," + " receive_date receipt_date," + " remark ," + " issue_id "
            + " from account_actual " + " where contract_id = #{value} " + " order by issue_id ")
    List<ActuralReceipt> getListByContractId(Integer id);

    @Select(" select  id id, " + " receivable receipt ," + " receive_date receipt_date," + " remark ," + " issue_id "
            + " from account_actual " + " where contract_id = #{value} " + " order by issue_id ")
    List<ActuralReceipt> getListByContractId1(Integer id);//阿民修改

    @Select("select * from account_actual where id=#{id}")
    AccountActual getAccountacturalByid(@Param("id") Integer id);//阿民修改


    @Select(" select c.saler_id seller_id ," + " u.name seller_name , " + " c.percentage percentage, "
            + " c.due_date due_date " + " from commission c " + " join user u on u.id = c.saler_id "
            + " where c.actual_id = #{value} ")
    List<Map<String, Object>> getSellers(Integer id);

    @Select(" select ifnull(sum(money - actual_money ),0) from account_receivable where contract_id = #{value}")
    Double isPlan(Integer contractId);

    @Options(useGeneratedKeys = true)
    @Insert("insert into account_actual values(null,#{issueId},#{contractId},#{receipt},#{receiptDate},#{remark})")
    void insertReceipt(ReceiptModel model);

    @Select("select id,issue_id , remark , date , amount from actural_ticket where contract_id = #{value}")
    List<Map<String, Object>> getTicket(Integer id);

    @Select("select * from actural_ticket where id=#{value}")
    ActuralTicket findactualtickedByid(Integer id);//min do,根据实开票id得到实开票

    @Delete("delete from actural_ticket where id=#{value}")
    Integer deleteactualTicket(Integer id);//min do 删除实开票


    @Options(useGeneratedKeys = true)
    @Insert("insert into actural_ticket values(null,#{amount},#{date},#{remark},#{contractId},#{issueId})")
    Integer addActralTicket(ActuralTicket model);

    @Select(" select sum(money) from ticket where contract_id = #{value}")
    Double isPlanTicket(Integer contractId);

    @SelectProvider(type = ContractProvider.class, method = "ticketList")
    List<Map<String, Object>> getTicketList(@Param("key") String key, @Param("start_date") Date startDate,
                                            @Param("end_date") Date endDate, @Param("block_id") Integer blockId);

    @Select("select ticket.money money, ticket.id id,should_id  from ticket join invoice_receivable ir on ir.id = ticket.should_id  where ticket.contract_id = #{value} and ticket.money != 0 order by ir.issue_id ")
    List<Map<String, Object>> getTicketMoney(Integer contractId);
    @Select("select ticket.money money, ticket.id id,should_id,ir.invoice_money irmoney  from ticket join invoice_receivable ir on ir.id = ticket.should_id  where ticket.contract_id = #{value} order by ir.issue_id desc")
    List<Map<String, Object>> getTicketMoney1(Integer contractId);

    @Update("update ticket set money = #{param1} where id = #{param2}")
    void updateMoney(Double money, Integer id);

    @Delete("delete from ticket where id = #{id}")
    void deleteTicket(Integer id);

    @Update("update invoice_receivable set complete_tag = 1 where id = #{value}")
    void updateTicket(Integer shouldId);


//    @Select("select id , money should_money,actual_money actural_money from account_receivable where contract_id = #{value} and money > actual_money order by end_date ")
//    List<Map<String, Object>> getShouldReceiptMoney(Integer contractId);

    @Select("select  id , money should_money,actual_money actural_money from account_receivable  where contract_id = #{value} and money > actual_money order by node_id , stage ")
    List<Map<String, Object>> getShouldReceiptMoney(Integer contractId);

    @Select("select  id , money should_money,actual_money actural_money from account_receivable  where contract_id = #{value} and money >= actual_money order by node_id asc, stage desc ")
    List<Map<String, Object>> getShouldReceiptMoneymin(Integer contractId);


    @Update("update account_receivable set actual_money = #{money} where id = #{id}")
    void updateReceiptMoney(@Param("id") Integer id, @Param("money") Double money);

    @Update("update account_receivable set actual_money = #{money} ,status = 3 where id = #{id}")
    void updateReceiptMoney1(@Param("id") Integer id, @Param("money") Double money);

    @Update("update account_receivable set actual_money = money , status = 5 where id = #{id} ")
    void updateReceipt(Integer id);

    @Update("update account_receivable set actual_money = 0 , status = 3 where id = #{id} ")
    void updateReceipt1(Integer id);

    @Insert("insert into should_actural values(null,#{param1},#{param2},#{param3}) ")
    void addShouldActral(Integer shouldId, Integer id, Double money);

    @Delete("delete from should_actural where should_id=#{should_id} and actural_id=#{actural_id}")
    Integer deleteShouldActual(@Param("should_id") Integer shouldid, @Param("actural_id") Integer acturalid);//删除实开票应开票关联的记录,min do

    @Select("select id,  issue_id , invoice_money should_money, date  should_date , remark should_remark  from invoice_receivable where contract_id = #{value} order by issue_id   ")
    List<Map<String, Object>> getTicketDetail(Integer contractId);

    @Update("update ticket_execute set should_id = #{param2},state = #{param3} where contract_id = #{param1}")
    void updateTicketExecute(Integer contractId, Integer shouldId, Integer state);

    @Select("select count(id) from actural_ticket where contract_id=#{contractId}")
    Integer getActualTicketRecordByContractId(@Param("contractId") Integer contractId);

    @Select("select amount money , date ,remark from actural_ticket where contract_id = #{id}")
    List<Map<String, Object>> getActralDetail(Integer id);

    @Update("update invoice_receivable set complete_tag = #{param2} where id =#{param1}")
    void updateShouldTicket(Integer shouldId, int i);

    @SelectProvider(type = ContractProvider.class, method = "getFinancialOutput")
    List<LinkedHashMap<String, Object>> getOutput(Integer blockId, Integer contractTypeId, String startDate,
                                                  String endDate, String changeStartDate, String changeEndDate);

    @Select(" select distinct cn.name node_name  ,concat('第',ar.stage ,'期') issue, ar.start_date date, ar.money money from contract_node cn left join account_receivable ar on ar.node_id = cn.id where cn.contract_id = #{value} order by cn.id , ar.stage ")
    List<LinkedHashMap<String, Object>> getreceiptByContractId(Integer contractId);

    @Select("select receivable money , receive_date from account_actual where contract_id = #{value} order by id  ")
    List<LinkedHashMap<String, Object>> getActuralContractId(Integer contractId);

    @Select("select concat('第',issue_id ,'期') issue,invoice_money money ,date  from invoice_receivable where contract_id = #{value} order by issue_id ")
    List<LinkedHashMap<String, Object>> getTicketByContractId(Integer contractId);

    @Select("select amount money from actural_ticket where contract_id = #{value} order by id")
    List<LinkedHashMap<String, Object>> getActuralTicketContractId(Integer contractId);

    @Select("select sum(money) from account_receivable where contract_id = #{value}")
    Double getShouldReceiptTotalMoney(Integer contractId);

    @Select("select sum(receivable) from account_actual where contract_id = #{value} ")
    Double getActuralTotalMoney(Integer contractId);

    @Select("select sum(invoice_money) from invoice_receivable where contract_id = #{value} ")
    Double getShouldTicket(Integer contractId);

    @Select("select sum(amount) from actural_ticket where contract_id = #{value}")
    Double getActuralTicket(Integer contractId);

    @Select("select concat('第',issue_id ,'期')  issue , invoice_money money , date from invoice_receivable where contract_id = #{value} order by issue_id ")
    List<Map<String, Object>> getShouldTicket1(Integer contractId);

    @Select(" select cn.name node_name , cn.money node_money , should , actural from contract_node cn left join (select node_id ,  sum(money)  should, sum(actual_money) actural from account_receivable group by node_id ) r on r.node_id = cn.id where cn.contract_id = #{value}   order by cn.id ")
    List<Map<String, Object>> getNodeList(Integer contractId);

    @Select("select sum(invoice_money) from invoice_receivable where contract_id = #{contractId} ")
    Double getAllTicket(Integer contractId);

    @Select("select ifnull(id,0) from invoice_receivable  order by id desc limit 1 ")
    Integer getLastInvoiceId();

    @Select(" select ifnull(state , -1) from ticket_execute where contract_id = #{value} ")
    Integer getState(Integer contractId);


    @Select("select contract_id from ticket_execute where should_id=#{invoiceReId}")
    Integer searchInvoiceReId(Integer invoiceReId);

    @Update(" update ticket set money = #{param2} ,should_date = #{param3} where should_id = #{param1} ")
    void updateTicket2(Integer id, Double invoiceMoney, Date date);

    @Select(" select  ir.issue_id issue_id, (ir.invoice_money - t.money) money from ticket_execute te left join ticket t on t.should_id = te.should_id left join invoice_receivable  ir on ir.id = te.should_id  where te.contract_id = #{value}")
    Map<String,Object> getTicketExecute(Integer contractId);

    @Select("select  id , money should_money,actual_money actural_money from account_receivable  where contract_id = #{value} and money > actual_money order by end_date ")
    List<Map<String,Object>> getShouldReceiptMoney2(Integer contractId);

    @Select("select  id , money should_money,actual_money actural_money from account_receivable  where contract_id = #{value} and money >= actual_money order by end_date ")
    List<Map<String,Object>> getShouldReceiptMoneymin2(Integer contractId);
}
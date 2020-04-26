package com.zx.servicefinance.mapper;

import com.zx.servicefinance.model.Contract;
import com.zx.servicefinance.model.ContractArchive;
import com.zx.servicefinance.model.ContractExample;
import com.zx.servicefinance.model.SimpleSeller;
import com.zx.servicefinance.provider.ContractProvider;
import com.zx.servicefinance.vo.ShowContract;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ContractDAO继承基类
 */
@Repository
public interface ContractDAO extends MyBatisBaseDao<Contract, Integer, ContractExample> {
    @Select("select * from contract order by id DESC LIMIT 1")
    Contract lastRecord();

    @Select("select count(*) from contract")
    int recordNumber();// 查询表的记录条数

    @Select("select LAST_INSERT_ID()")
    int lastRecordId();

    @Select("select amount from contract where id=#{id}")
    Double getMoney(@Param("id") Integer id);// 获取合同总金额

    @Select("select service_start from contract where id=#{contractId}")
    Date getContractServiceStartDate(@Param("contractId") Integer contractId);// 获取运维合同的服务开始时间

    @Select("select service_end from contract where id=#{contractId}")
    Date getContractServiceEndDate(@Param("contractId") Integer contractId);// 获取运维合同的服务开始时间

    @Select("select sign_date from contract where id=#{contractId}")
    Date getContractSignDate(@Param("contractId") Integer contractId);// 获取合同签订时间

    @Select("select c.id id," + " c.number number," + " c.create_date create_date," + " t.name contract_type,"
            + " ct.name content_type," + " c.name name," + " b.name block," + " c.operator operator,"
            + " c.technician technician," + " c.customer customer," + " c.sign_date sign_date,"
            + " c.due due, " + " c.amount amount, " + " c.content content,"
            + " c.service_start service_start," + " c.service_end service_end,"
            + " c.archive_tag archive_tag" + " from contract c "
            + " join contract_type t on t.id = c.contract_type"
            + " join content_type ct on ct.id = c.content_type" + " join block b on b.id = c.block "
            + " where c.id = #{value} ")
    ShowContract lookUPContractALL(Integer id);

    @Select("select c.id id," + " c.number number," + " c.create_date create_date," + " t.name contract_type,"
            + " ct.name content_type," + " c.name name," + " b.name block," + " c.sign_date sign_date,"
            + " c.due due, " + " c.content content," + " c.service_start service_start, c.operation_tag contract_tag, "
            + " c.service_end service_end," + " c.archive_tag archive_tag" + " from contract c "
            + " join contract_type t on t.id = c.contract_type"
            + " join content_type ct on ct.id = c.content_type" + " join block b on b.id = c.block "
            + " where c.id = #{value}")
    ShowContract lookUpContractNoMoney(Integer id);

    @Select("select u.name name ," + " u.id id " + " from contract_seller s   "
            + " join user u on u.id = s.seller_id " + " where  s.contract_id = #{value} ")
    List<SimpleSeller> getSellers(Integer id);

    @Select("select money " + "from contract_node " + " where contract_id = #{value} ")
    List<Double> getNodeMoneys(Integer id);

    @Select("select b.user_id" + " from contract c " + " join block_user b on b.block_id = c.block"
            + " where c.id = #{value} ")
    List<Integer> lookUpBlockDuty(Integer id);

    @Select("select b.operate_id" + " from contract c " + " join block_user b on b.block_id = c.block"
            + " where c.id = #{value} ")
    List<Integer> lookUpBlockOpertorDuty(Integer id);

    @Select("select seller_id " + "  from contract_seller " + "  where contract_id = #{value}")
    List<Integer> lookUpSaler(Integer id);

    @SelectProvider(type = ContractProvider.class, method = "selectList")
    List<com.zx.servicefinance.vo.ContractList> getContractList(@Param("key") String key,
                                                                @Param("block_id") Integer blockId, @Param("type_id") Integer typeId,
                                                                @Param("start") Date start, @Param("end") Date end, @Param("seller_id") Integer sellerId, @Param("archive_tag") Integer archiveTag);

    @Select("select ct.tag " + " from contract c " + " join contract_type ct on ct.id = c.contract_type "
            + " where c.id = #{value} ")
    String getContractTypeTag(Integer contractId);

    @Select("select archive_number " + " from contract_archive " + " order by id desc " + " limit 1 ")
    String lastArchive();

    @Select("select concat(date_format(execute_date,'%Y-%m-%d') , '-----------------------------------------------------',node_name)"
            + " from execute_node " + " where contract_id = #{value}")
    List<String> executeList(Integer id);

    @Options(useGeneratedKeys = true)
    @Insert("insert into contract_archive values(null,#{contractId},#{archiveNumber},#{positionNumber},#{entryId},#{clerk},#{contractContents},#{others},#{comment},#{archiveDate}) ")
    void archiveContract(ContractArchive ca);

    @Select("select c.id archive_id , con.name contract_name,con.number contract_number , archive_number , position_number , clerk, u.name operator, contract_contents, others,comment , archive_date from contract_archive c join user  u on u.id = c.reporter join contract con on con.id = c.contract_id where c.contract_id = #{value} ")
    Map<String, Object> showArchive(Integer id);

    @Update("update contract set account_id=#{accountId} where id=#{id}")
    Integer updateContractAccountId(@Param("accountId") Integer accountId, @Param("id") Integer id);

    @Update("update contract_archive set position_number = #{positionNumber} , reporter = #{entryId} ,       clerk = #{clerk} , contract_contents = #{contractContents} , others = #{others}, comment = #{comment} ,archive_date = #{archiveDate} where id =#{id}")
    Integer updateArchive(ContractArchive ca);

    @Update("update contract set rest_amount = rest_amount - #{amount} where id = #{id} ")
    void updateRestAmount(@Param("amount") Double amount, @Param("id") Integer contractId);

    @Delete("delete from contract_archive where contract_id = #{value}")
    Integer deleteArchive(Integer id);

    @SelectProvider(type = ContractProvider.class,method = "getContract")
    List<LinkedHashMap<String, Object>> getOutput(Integer bolockId, Integer contentTypeId, String string, String startDate,
                                                  String endDate);


    @SelectProvider(type = ContractProvider.class,method = "getPeriodContract")
    Map<String,Object> getPeriodContract(String startDate, String endDate, Integer blockId, Integer contractTypeId, Integer type);

    @Select("select sum(amount) money, count(id) count from contract ")
    Map<String, Object> getTotal();

    @Select(" select operation_tag from contract where id = #{value}")
    Integer getOpratorTag(Integer contractId);

    @Select("SELECT  LPAD(count(*)+1, 2, 0) FROM contract where year(sign_date)=#{year} AND month(sign_date)=#{month} AND day(sign_date)=#{day} ")
    String getRecordAmount(@Param("year") String year,@Param("month") String month,@Param("day") String day);
}
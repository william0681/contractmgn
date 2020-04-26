package com.zx.servicegateway.provider;

import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;

public class ContractProvider {

    public String selectList(Map<String, Object> map) {
        Integer blockId = (Integer) map.get("block_id");
        Integer typeId = (Integer) map.get("type_id");
        String key = (String) map.get("key");
        Date start = (Date) map.get("start"), end = (Date) map.get("end");
        Integer sellerId = (Integer) map.get("seller_id");
        Integer archiveTag = (Integer) map.get("archive_tag");
        StringBuffer sb = new StringBuffer();
        sb.append("select id,number,name,block,sign_date,contract_type,operator,group_concat(name1) name1,technician,customer,contract_tag, archive_tag from (");
        sb.append("select distinct  c.id id, " + " c.number number," + " c.name name, " + " b.name block,"
                + " c.sign_date sign_date," + " ct.name contract_type ,"
                +" c.operator operator,"
                + "u.name name1,"
                + " c.technician technician," + " c.customer customer,"
                + " c.operation_tag contract_tag, c.archive_tag archive_tag " + " from contract c "
                + " left join block b on b.id = c.block " + " left join contract_type ct on ct.id = c.contract_type "
                + " left join contract_seller cs on cs.contract_id = c.id "
                + " left join user u on u.id = cs.seller_id "
                + " where true ");
        if (blockId != null &&blockId != 0) {
            sb.append(" and c.block = #{block_id} ");
        }

        if (sellerId != null && sellerId != 0) {
            sb.append(" and cs.seller_id = #{seller_id} ");
        }

        if (typeId != null && typeId != 0) {
            sb.append(" and c.contract_type = #{type_id} ");
        }

        if (start != null) {
            sb.append(" and c.sign_date >= #{start} ");
        }

        if (end != null)
            sb.append(" and c.sign_date <= #{end} ");
        if (archiveTag != null && archiveTag != 0) {
            sb.append("and archive_tag = (#{archive_tag} - 1) ");
        }

        if (!StringUtils.isEmpty(key)) {
            sb.append(" and (c.number like concat('%',#{key},'%')" + " or c.name like concat('%',#{key},'%')"
                    + " or b.name like concat('%',#{key},'%')" + " or ct.name like concat('%',#{key},'%')) ");
        }

        sb.append(" order by c.id desc ");
        sb.append(") a group by id desc");
        return sb.toString();
    }

    public String ticketList(Map<String, Object> map) {
        StringBuffer sb = new StringBuffer();
        sb.append(
                "select distinct c.operation_tag contract_tag ,c.id contract_id , c.number contract_number , c.name contract_name ," +
                        "c.customer customer,"+//客户名称
                        "c.amount amount,"+//合同总金额
                        "c.contract_type contract_type,"+
                        "b.name block_name, " +
                        "ifnull( concat('第',ir.issue_id,'期'),'--') issue, ifnull(ir.invoice_money,'--') should ," +
                        "ifnull( (ir.invoice_money - t.money),'--') actural , te.state state , ir.date date  " +
                        "from contract c " +
                        "left join block b on c.block = b.id " +
                        "left join ticket_execute te on te.contract_id = c.id " +
                        "left join invoice_receivable ir on ir.id = te.should_id " +
                        "left join ticket t on t.should_id = ir.id  where true  ");
        Date starDate = (Date) map.get("start_date");
        Date endDate = (Date) map.get("end_date");
        String key = (String) map.get("key");
        Integer blockId = (Integer) map.get("block_id");
        if (key != null) {
            sb.append(
                    "and (c.number like concat('%',#{key},'%') or c.name like concat('%',#{key},'%') or b.name like concat('%',#{key},'%') ) ");
        }

        if (starDate != null)
            sb.append("and c.sign_date >= #{start_date} ");
        if (endDate != null)
            sb.append("and c.sign_date <= #{end_date} ");
        if (blockId != null && blockId != 0)
            sb.append("and c.block = #{block_id} ");
        sb.append(" order by  field(te.state,0,2,1,3) , ir.date ,c.id  ");

        return sb.toString();
    }

    public String getContract(final Map<String, Object> map) {
        Integer blockId = (Integer) map.get("param1"), contractTypeId = (Integer) map.get("param2");
        String sql = (String) map.get("param3"), startDate = (String) map.get("param4"),
                endDate = (String) map.get("param5");

        StringBuffer sb = new StringBuffer(" select distinct ");
        sb.append(sql);
        sb.append(
                " from contract c left join block b on b.id = c.block left join  contract_type ct on ct.id = c.contract_type left join content_type ctype on ctype.id = c.content_type  where true  ");

        if (blockId != null && blockId != 0)
            sb.append(" and c.block = #{param1} ");
        if (contractTypeId != null && contractTypeId != 0)
            sb.append(" and c.contract_Type = #{param2} ");
        if (!StringUtils.isEmpty(startDate))
            sb.append(" and c.sign_date >= #{param4} ");
        if (!StringUtils.isEmpty(endDate))
            sb.append(" and c.sign_date <= #{param5} ");
        sb.append(" order by c.block , c.contract_type , c.id ");

        return sb.toString();

    }

    public String getFinancialOutput(final Map<String, Object> map) {
        Integer blockId = (Integer) map.get("param1"), contractTypeId = (Integer) map.get("param2");
        String startDate = (String) map.get("param3"), endDate = (String) map.get("param4"),
                changeStartDate = (String) map.get("param5"), changeEndDate = (String) map.get("param6");
        ;

        StringBuffer sb = new StringBuffer(" select distinct ");
        sb.append(" c.id id , c.name contract_name ,ct.name contract_type ,c.sign_date sign_date, b.name block , c.amount total_money ");
        sb.append(
                " from contract c left join block b on b.id = c.block left join  contract_type ct on ct.id = c.contract_type left join account_receivable ar on ar.contract_id = c.id left join account_actual aa on aa.contract_id = c.id left join invoice_receivable ir on ir.contract_id = c.id left join  actural_ticket at on at.contract_id = c.id where true ");

        if (blockId != null && blockId != 0)
            sb.append(" and c.block = #{param1} ");
        if (contractTypeId != null && contractTypeId != 0)
            sb.append(" and c.contract_Type = #{param2} ");
        if (!StringUtils.isEmpty(startDate))
            sb.append(" and c.sign_date >= #{param3} ");
        if (!StringUtils.isEmpty(endDate))
            sb.append(" and c.sign_date <= #{param4} ");
        if (!StringUtils.isEmpty(changeStartDate))
            sb.append(
                    " and ( ar.start_date >= #{param5} or aa.receive_date >= #{param5}  or ir.date >= #{param5}  or at.date >= #{param5} )");
        if (!StringUtils.isEmpty(changeEndDate))
            sb.append(
                    " and ( ar.start_date <= #{param6} or aa.receive_date <= #{param6}  or ir.date <= #{param6}  or at.date <= #{param6} )");
        sb.append(" order by c.block , c.contract_type , c.id  ");

        return sb.toString();

    }

    public String getPeriodContract(final Map<String, Object> map) {
        StringBuffer sb = new StringBuffer();

        String startDate = (String) map.get("param1");
        String endDate = (String) map.get("param2");
        Integer blockId = (Integer) map.get("param3");
        Integer contractTypeId = (Integer) map.get("param4");
        Integer type = (Integer) map.get("param5");

        sb.append(" select sum(amount) money,count(id) count from contract where true ");
        if (!StringUtils.isEmpty(startDate))
            sb.append(" and sign_date >= #{param1} ");
        if (!StringUtils.isEmpty(endDate))
            sb.append(" and sign_date <= #{param2} ");

        if(blockId != null && blockId != 0)
        sb.append(" and block = #{param3} ");
        if(contractTypeId != null && contractTypeId != 0)
        sb.append(" and contract_type = #{param4} ");

        if(type == 1)
        {
            sb.append(" and amount != rest_amount ");
        }
        return sb.toString();
    }
}

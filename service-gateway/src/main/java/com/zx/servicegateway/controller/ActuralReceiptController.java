package com.zx.servicegateway.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zx.servicegateway.model.ActuralReceiptList;
import com.zx.servicegateway.model.ActuralTicket;
import com.zx.servicegateway.model.ReceiptModel;
import com.zx.servicegateway.pojo.Message;
import com.zx.servicegateway.service.ContractService;
import com.zx.servicegateway.service.IActuralReceiptService;
import com.zx.servicegateway.util.ExcelUtil;
import com.zx.servicegateway.util.RoleUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ActuralReceiptController
 */
@RestController
@Api(description = "实收款操作")
@RequestMapping("/api/v1/actural")
public class ActuralReceiptController {

    @Autowired
    private ContractService contractService;
    @Autowired
    private IActuralReceiptService acturalReceiptService;

    @Autowired
    @Qualifier("financial")
    private Map<String, String> map;

    @GetMapping("get_receipt_list")
    @ApiOperation("查询实收款列表和合同总额")
    @RequiresRoles(value = {"manager", "accountant", "allchief", "areachief"}, logical = Logical.OR)
    public Message getReceiptList(@RequestParam("contract_id") Integer id) {
        ActuralReceiptList list = acturalReceiptService.getReceiptList(id);
        Message message;
        if (list.getTotalAmount() == null)
            message = Message.createErr(-1, "该项目不存在");
        else
            message = Message.createSuc(list);
        return message;
    }

    @RequiresRoles(value = {"manager", "accountant"}, logical = Logical.OR)
    @ApiOperation("添加一个实收款")
    @PostMapping("add_receipt")
    public Message addReceipt(@RequestBody ReceiptModel model) {
        Message message = null;
        Integer opratorTag = contractService.getOpratorTag(model.getContractId());
        if(opratorTag == 0) {
            List<Map<String, Object>> list = acturalReceiptService.getNodeList(model.getContractId());
            if (list == null || list.size() == 0)
                return Message.createErr(-1, "你还未添加节点");
            else {
                double amount = 0;
                int index = 0;
                String nodeName = null;
                while (index < list.size()) {
                    double should = 0, actural = 0, nodeMoney = 0;
                    Map<String, Object> map = list.get(index);
                    if (map.containsKey("node_name"))
                        nodeName = (String) map.get("node_name");
                    if (map.containsKey("should"))
                        should = (double) map.get("should");
                    if (map.containsKey("actural"))
                        actural = (double) map.get("actural");
                    if (map.containsKey("node_money"))
                        nodeMoney = (double) map.get("node_money");
                    amount += should - actural;
                    if (nodeMoney != should)
                        break;
                    index++;
                }

                if (amount < model.getReceipt()) {
                    return Message.createErr(-1, "应收款节点（" + nodeName + "）剩余金额还未添加");
                }
            }
        }
        else {
            if (model.getReceipt() > acturalReceiptService.isPlan(model.getContractId()))
            {
                return  Message.createErr(-1,"实收款大于应收余额");
            }
        }
        Subject subject = SecurityUtils.getSubject();
        Integer result = acturalReceiptService.insertReceipt(model,opratorTag,(int)subject.getSession().getAttribute("userId"));
        message = Message.createSuc(result);
        return message;

    }

    @RequiresRoles(value={"manager","accountant"},logical = Logical.OR)
    @ApiOperation("删除实收款")
    @DeleteMapping("delete_receipt")
    public Message deleteReceipt(@RequestParam("actrualId") Integer actrualId){
        Message message=null;
        Integer result=acturalReceiptService.deleteReceipt(actrualId);
        if(result==null)
            message=Message.createErr(-1,"删除实收款失败");
        else
            message=Message.createSuc(result);
        return message;
    }

    @RequiresRoles(value = {"manager", "accountant", "allchief", "areachief"}, logical = Logical.OR)
    @ApiOperation("查询实收款开票")
    @GetMapping("get_actural_ticket")
    public Message getActuralTicket(@RequestParam("contract_id") Integer id) {
        Message message = null;

        List<Map<String, Object>> list = acturalReceiptService.getAcuralTicket(id);
        message = Message.createSuc(list);
        return message;

    }

    @RequiresRoles(value = {"manager", "accountant"}, logical = Logical.OR)
    @ApiOperation("增加一个实际开票")
    @PostMapping("add_actural_ticket")
    public Message addActuralTicket(@RequestBody ActuralTicket model) {
        Message message = null;
        Subject subject = SecurityUtils.getSubject();
        Double planTicket = acturalReceiptService.isPlanTicket(model.getContractId());
        if (planTicket == null || model.getAmount() < 0 || planTicket < model.getAmount())
            message = Message.createErr(-1, "你的实开票不符合实际");
        else {
            Integer result = acturalReceiptService.addActralTicket(model,(int)subject.getSession().getAttribute("userId"));
            message = Message.createSuc(result);
        }
        return message;
    }

    @RequiresRoles(value = {"manager", "accountant"}, logical = Logical.OR)
    @ApiOperation("删除实开票")
    @DeleteMapping("delete_actual_ticket")
    public Message deleteActualTicket(@RequestParam("id")Integer actualticketid){
        return acturalReceiptService.deleteActualTicket(actualticketid);
//        Integer result=acturalReceiptService.deleteActualTicket(actualticketid);
//        if(result==null)
//            message=Message.createErr(-1,"删除实开票失败");
//        else
//            message=Message.createSuc(result);
//        return message;
    }


    @ApiOperation("开票列表")
    @GetMapping("ticket_list")
    @RequiresRoles(value = {"manager", "accountant", "allchief", "areachief", "operator"}, logical = Logical.OR)
    public Message ticketList(@RequestParam(required = false) String key,
                              @RequestParam(value = "start_date", required = false) String start,
                              @RequestParam(value = "end_date", required = false) String end, @RequestParam("page_num") Integer page,
                              @RequestParam Integer record) throws ParseException {

        Message message = null;
        Date startDate, endDate = startDate = null;
        WebDelegatingSubject subject = (WebDelegatingSubject)SecurityUtils.getSubject();
        Integer blockId = null;
        if (subject.hasRole("areachief") || subject.hasRole("operator"))
            blockId = contractService.getBlockById((Integer) subject.getSession().getAttribute("userId"), RoleUtil.getRole(subject));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (!StringUtils.isEmpty(start))
            startDate = sdf.parse(start);
        if (!StringUtils.isEmpty(end))
            endDate = sdf.parse(end);
        PageHelper.startPage(page, record);
        PageInfo<Map<String, Object>> list = new PageInfo<>(
                acturalReceiptService.getTicketList(key, startDate, endDate, blockId));
        Map<String, Object> map = new HashMap<>();
        map.put("data", list.getList());
        map.put("total_record", list.getTotal());
        message = Message.createSuc(map);
        return message;

    }

    @ApiOperation("开票详情列表")
    @GetMapping("ticket_detail")
    @RequiresRoles(value = {"manager", "accountant", "allchief", "areachief", "operator"}, logical = Logical.OR)
    public Message getTicketDetail(@RequestParam("page_num") Integer pageNum, @RequestParam("record") Integer record,
                                   @RequestParam("contract_id") Integer id) {
        Message message = null;
        PageHelper.startPage(pageNum, record);
        PageInfo<Map<String, Object>> list = new PageInfo<>(acturalReceiptService.getTicketDetail(id));
        Map<String, Object> map = new HashMap<>();
        map.put("data", list.getList());
        map.put("total_record", list.getTotal());
        message = Message.createSuc(map);
        return message;
    }

    @ApiOperation("實開票详情列表")
    @GetMapping("ticket_actural_detail")
    @RequiresRoles(value = {"manager", "accountant", "allchief", "areachief", "operator"}, logical = Logical.OR)
    public Message getTicketActuralDetail(@RequestParam("page_num") Integer pageNum,
                                          @RequestParam("record") Integer record, @RequestParam("contract_id") Integer id) {
        Message message = null;
        PageHelper.startPage(pageNum, record);
        PageInfo<Map<String, Object>> list = new PageInfo<>(acturalReceiptService.getActralDetail(id));
        Map<String, Object> map = new HashMap<>();
        map.put("data", list.getList());
        map.put("total_record", list.getTotal());
        message = Message.createSuc(map);
        return message;
    }

    @ApiOperation("导出财务合同")
    @GetMapping("get_output")
    public Message getOutput(HttpServletResponse response, @RequestParam(name = "sql", required = false) String sql,
                             @RequestParam(name = "block_id", required = false) Integer blockId,
                             @RequestParam(name = "contract_type", required = false) Integer contractTypeId,
                             @RequestParam(name = "start_date", required = false) String startDate,
                             @RequestParam(name = "end_date", required = false) String endDate,
                             @RequestParam(name = "start_date_change", required = false) String changeStartDate,
                             @RequestParam(name = "end_date_change", required = false) String changeEndDate) throws IOException {

        List<LinkedHashMap<String, Object>> list = acturalReceiptService.getOutput(blockId, contractTypeId, sql,
                startDate, endDate, changeStartDate, changeEndDate);

        if (list == null || list.size() == 0) {
            return Message.createErr(-1, "暂无数据");
        }
        StringBuffer sb = new StringBuffer("合同名称,合同类别,所属区块,合同总额");
        if (sql == null) {
            sql = "";
        }
        if (sql.contains("1"))
            sb.append(",收款节点,节点期数,应收款金额,实收款金额,收款状态");
        if (sql.contains("2"))
            sb.append(",应开票期数,应开票金额,实开票金额,开票状态");
        if (sql.contains("3"))
            sb.append(",应收款总额,实收款总额");
        if (sql.contains("4"))
            sb.append(",应开票总额,实开票总额");

        String[] titles = sb.toString().split(",");

        StringBuffer headName = new StringBuffer();
        if (StringUtils.isEmpty(startDate)) {
            headName.append(list.get(0).get("sign_date"));
        } else {
            headName.append(startDate);
        }
        headName.append("至");
        if (StringUtils.isEmpty(endDate)) {
            headName.append(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        } else {
            headName.append(endDate);
        }
        if (blockId == null || blockId == 0)
            headName.append("全地区");
        else {
            headName.append(list.get(0).get("block_name"));
        }
        if (contractTypeId == null || contractTypeId == 0)
            headName.append("所有");
        else {
            headName.append(list.get(0).get("contract_type"));
        }
        headName.append("合同财务情况");
        /**
         * 文件相关操作
         */
        ExcelUtil.exportFinanceExcel(response, headName.toString(), titles, list, sql);

        return Message.createSuc(list);

    }

}
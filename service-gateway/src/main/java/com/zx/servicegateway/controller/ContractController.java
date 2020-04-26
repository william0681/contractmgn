package com.zx.servicegateway.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zx.servicegateway.model.ContractArchive;
import com.zx.servicegateway.pojo.Message;
import com.zx.servicegateway.service.ContractService;
import com.zx.servicegateway.util.ExcelUtil;
import com.zx.servicegateway.util.RoleUtil;
import com.zx.servicegateway.vo.ContractList;
import com.zx.servicegateway.vo.ContractVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@Api(description = "合同相关接口")
public class ContractController {


    @Autowired
    @Qualifier("contract")
    Map<String, String> map;

    @Autowired
    private ContractService contractService;

    @Value("${pdf.path}")
    private String path;

    /**
     * 新建一个合同(包括合同节点)
     *
     * @param contractVo 合同对象
     * @return
     */
    @RequiresRoles(value = {"manager", "accountant", "overallchief", "areachief","operator"}, logical = Logical.OR)
    @PostMapping("/contract")
    @ResponseBody
    public Message buildContract(@RequestBody ContractVo contractVo) {
        String number = contractService.buildContractNumber(contractVo.getBlock(), contractVo.getContractType(),
                contractVo.getSignDate().getTime());
        contractVo.setNumber(number);
        Subject subject = SecurityUtils.getSubject();
        return contractService.addContract(contractVo,(int)subject.getSession().getAttribute("userId"));
    }

    /**
     * 更新合同
     *
     * @param contractVo 合同对象
     * @return Message
     */
    @RequiresRoles(value = {"manager", "accountant", "overallchief"}, logical = Logical.OR)
    @PutMapping("/contract")
    @ResponseBody
    public Message updateContract(@RequestBody ContractVo contractVo) {
        Subject subject = SecurityUtils.getSubject();
        return contractService.updateContract(contractVo,(int)subject.getSession().getAttribute("userId"));
    }

    /**
     * 删除合同
     *
     * @param ContractId 合同id
     * @return Message
     */
    @RequiresRoles(value = {"manager", "accountant", "overallchief"}, logical = Logical.OR)
    @DeleteMapping("/contract")
    @ResponseBody
    public Message deleteContract(@RequestParam Integer ContractId) {
        return contractService.deleteContract(ContractId);
    }

    /**
     * @param id 合同id
     * @return 该合同的详细信息
     */

    @GetMapping("contract")
    @ResponseBody
    @ApiOperation("查看合同")
    @RequiresAuthentication
    public Message lookUpContract(@RequestParam Integer id) {
        Subject subject = SecurityUtils.getSubject();
        Integer userId = (Integer) subject.getSession().getAttribute("userId");
        if (subject.hasRole("manager") || subject.hasRole("accountant") || subject.hasRole("overallchief")) {
            return Message.createSuc(contractService.lookUpContractAll(id));
        } else if (subject.hasRole("areachief")) {
            if (contractService.lookUpBlockDuty(id).contains(userId)) {
                return Message.createSuc(contractService.lookUpContractAll(id));
            }

            return Message.createErr(-1, "该合同不在你所负责的区块内");
        } else if (subject.hasRole("operator")) {
            if (contractService.lookUpBlockOperatorDuty(id).contains(userId)) {
                return Message.createSuc(contractService.lookUpContractAll(id));
            }

            return Message.createErr(-1, "该合同不在你所负责的区块内");
        }else if (subject.hasRole("saler")) {
            if (contractService.lookUpSaler(id).contains(userId)) {
                return Message.createSuc(contractService.lookUpContractAll(id));
            }

            return Message.createErr(-2, "你不是该合同的销售员");
        } else if (subject.hasRole("buyer")) {
            return Message.createSuc(contractService.lookUpContractNoMoney(id));
        }
        return Message.createErr(-3, "你还没有属于自己的角色");
    }

    /**
     * @param key       检索字段
     * @param pageNum   页号
     * @param record    每页记录数
     * @param blockId   区块号
     * @param typeId    合同内容类别
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    @GetMapping("contract_list")
    @ResponseBody
    @ApiOperation("获取列表")
    public Message getContractList(@RequestParam(value = "key", required = false) String key,
                                   @RequestParam("page_num") Integer pageNum, @RequestParam Integer record,
                                   @RequestParam(value = "block_id") Integer blockId, @RequestParam(value = "type_id") Integer typeId,
                                   @RequestParam(value = "start_date", required = false) String startDate,
                                   @RequestParam(value = "end_date", required = false) String endDate,
                                   @RequestParam(value = "archive_tag") Integer archiveTag) {
        Message message = null;
        Integer sellerId = null;
        Subject subject = SecurityUtils.getSubject();
        if (subject.hasRole("saler")) {
            sellerId = (Integer) subject.getSession().getAttribute("userId");
            blockId = 0;
        } else if (subject.hasRole("areachief") || subject.hasRole("operator")) {
            blockId = contractService.getBlockById((Integer) subject.getSession().getAttribute("userId"), RoleUtil.getRole(subject));
            if (blockId == null) {
                message = Message.createErr(-2, "你还没有负责的区块");
                return message;
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = null, end = null;
        try {
            if (startDate != null)
                start = sdf.parse(startDate);
            if (endDate != null)
                end = sdf.parse(endDate);
        } catch (Exception e) {
            Message.createErr(-3, "日期格式不对");
        }

        long startstamp,endstamp;
        if(start ==null){
            startstamp = 0;
        }else{
            startstamp = start.getTime();
        }
        if(end ==null){
            endstamp = 0;
        }else{
            endstamp = end.getTime();
        }

        PageHelper.startPage(pageNum, record);
        PageInfo<ContractList> pageInfo = new PageInfo<>(
                contractService.getContractList(key, blockId, typeId, startstamp, endstamp, sellerId, archiveTag));
        Map<String, Object> result = new HashMap<>();
        result.put("all_record", pageInfo.getTotal());
        result.put("all_page", pageInfo.getPages());
        result.put("current_page", pageNum);
        result.put("data", pageInfo.getList());
        message = Message.createSuc(result);
        return message;
    }


    @RequiresRoles(value = {"manager", "accountant", "allchief"}, logical = Logical.OR)
    @ApiOperation("合同归档")
    @ResponseBody
    @PostMapping("archive_contract")
    public Message archiveContract(ContractArchive ca) {
        ca.setEntryId((Integer) (SecurityUtils.getSubject().getSession().getAttribute("userId")));
        if (ca.getId() == null)
            ca.setArchiveNumber(contractService.getArchiveNumber(ca.getContractId()));
        Integer id = ca.getId() == null ? contractService.archiveContract(ca) : contractService.updateArchive(ca);
        if (id == null) {
            return Message.createErr(-1, "数据库存储错误");
        }
        return Message.createSuc(id);
    }

    @ApiOperation("执行节点列表")
    @ResponseBody
    @GetMapping("execute_list")
    public Message executeList(Integer id) {
        List<String> result = contractService.executeList(id);
        return Message.createSuc(result);
    }

    @RequiresRoles(value = {"manager", "accountant", "allchief", "areachief"}, logical = Logical.OR)
    @ApiOperation("查看合同归档信息")
    @ResponseBody
    @GetMapping("show_archive")
    public Message showArchive(@RequestParam("contract_id") Integer id) {
        Message message = null;
        Map<String, Object> map = contractService.showArchive(id);
        if (map == null || map.isEmpty())
            message = Message.createErr(-1, "该合同未归档");
        else
            message = Message.createSuc(map);
        return message;
    }

    @RequiresRoles(value = {"manager", "accountant", "allchief"}, logical = Logical.OR)
    @ApiOperation("刪除归档信息")
    @ResponseBody
    @DeleteMapping("delete_archive")
    public Message deleteArchive(@RequestParam("contract_id") Integer id) {
        Message message = null;
        Integer result = contractService.deleteArchive(id);
        if (result == null)
            message = Message.createErr(-1, "刪除歸檔失敗");
        else
            message = Message.createSuc(result);
        return message;
    }


    @ApiOperation("导出合同")
    @ResponseBody
    @GetMapping("get_output")

    public Message getOutput(HttpServletResponse response, @RequestParam("sql")String[] sql , @RequestParam(name = "block_id",required = false) Integer blockId , @RequestParam(name = "contract_type",required = false) Integer contractTypeId , @RequestParam(name = "start_date",required = false) String startDate , @RequestParam(name = "end_date",required = false) String endDate) throws IOException, InvalidFormatException {
        if(sql == null || sql.length == 0)
        {
            return Message.createErr(-1, "筛选字段不能为空");
        }
        Map<String,String> map = new LinkedHashMap<>();

        for (String str : sql) {
            if (this.map.containsKey(str)) {
                map.put(str, this.map.get(str));
            } else {
                return Message.createErr(-1, "筛选数据不合法");
            }
        }

        List<LinkedHashMap<String, Object>> list = contractService.getOutput(blockId, contractTypeId, map, startDate, endDate);
        if(list == null || list.size() == 0)
        {
            return Message.createErr(-1, "没有符合筛选条件的记录，无法导出，请返回重新筛选数据");
        }


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        StringBuffer headNama = new StringBuffer();
        if (StringUtils.isEmpty(startDate)) {
            startDate = simpleDateFormat.format((Date)list.get(0).get("sign_date1"));
            headNama.append(list.get(0).get("sign_date1"));
            
        } else {
            headNama.append(startDate);
        }

        for( Map<String,Object> item: list)
            item.remove("sign_date1");
        headNama.append("至");
        if (StringUtils.isEmpty(endDate)) {
            headNama.append(simpleDateFormat.format(new Date()));
            endDate = simpleDateFormat.format(new Date());
        } else {
            headNama.append(endDate);
        }
        if (blockId == null || blockId == 0)
            headNama.append("全地区");
        else  {
            headNama.append(list.get(0).get("block_name"));
        }
        if (contractTypeId == null || contractTypeId == 0)
            headNama.append("所有");
        else {
            headNama.append(list.get(0).get("contract_type"));
        }
        headNama.append("合同情况");

        Map<String,Object> totals = contractService.getTotals(startDate,endDate,blockId,contractTypeId);
        ExcelUtil.exportExcel(response, headNama.toString(), sql, list,totals);
        return Message.createSuc(totals);

    }


}

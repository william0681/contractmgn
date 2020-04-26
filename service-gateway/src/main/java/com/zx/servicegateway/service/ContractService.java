package com.zx.servicegateway.service;

import com.zx.servicegateway.model.ContractArchive;
import com.zx.servicegateway.pojo.Message;
import com.zx.servicegateway.vo.ContractList;
import com.zx.servicegateway.vo.ContractVo;
import com.zx.servicegateway.vo.ShowContract;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@FeignClient("service-contract")
@Repository
public interface ContractService {

    @RequestMapping(value = "/contract/addContract",method = RequestMethod.POST)
    Message addContract(@RequestBody ContractVo contractVo,@RequestParam("userId") int userId);

    @GetMapping("/contract/buildContractNumber")
    String buildContractNumber(@RequestParam("blockId") int blockId,@RequestParam("contractTypeId") int contractTypeId,@RequestParam("timestamp") long timestamp);

    @PostMapping("/contract/updateContract")
    Message updateContract(@RequestBody ContractVo contractVo,@RequestParam("userId") int userId);

    @GetMapping("/contract/deleteContract")
    Message deleteContract(@RequestParam("id") Integer id);

    @GetMapping("/contract/getContractServiceTime")
    Message getContractServiceTime(@RequestParam("contractId") Integer contractId);

    @GetMapping("/contract/lookUpContractAll")
    ShowContract lookUpContractAll(@RequestParam("id") Integer id);

    @GetMapping("/contract/lookUpContractNoMoney")
    ShowContract lookUpContractNoMoney(@RequestParam("id") Integer id);

    @GetMapping("/contract/getContractMoney")
    Message getContractMoney(@RequestParam("contractId") Integer contractId);

    @GetMapping("/contract/lookUpBlockDuty")
    List<Integer> lookUpBlockDuty(@RequestParam("id") Integer id);

    @GetMapping("/contract/lookUpBlockOperatorDuty")
    List<Integer> lookUpBlockOperatorDuty(@RequestParam("id") Integer id);

    @GetMapping("/contract/lookUpSaler")
    List<Integer> lookUpSaler(@RequestParam("id") Integer id);

    @GetMapping("/contract/getArchiveNumber")
    String getArchiveNumber(@RequestParam("contractId") Integer contractId);

    @GetMapping("/contract/getContractList")
    List<ContractList> getContractList(@RequestParam("key") String key,
                                       @RequestParam("blockId") Integer blockId,
                                       @RequestParam("typeId") Integer typeId,
                                       @RequestParam("startstamp") long startstamp,
                                       @RequestParam("endstamp") long endstamp,
                                       @RequestParam("sellerId") Integer sellerId,
                                       @RequestParam("archiveTag") Integer archiveTag);

    @GetMapping("/contract/executeList")
    List<String> executeList(@RequestParam("id") Integer id);

    @RequestMapping(value="/contract/archiveContract",method = RequestMethod.POST)
    Integer archiveContract(@RequestBody ContractArchive ca);

    @GetMapping("/contract/showArchive")
    Map<String, Object> showArchive(@RequestParam("id") Integer id);

    @GetMapping("/contract/getBlockById")
    Integer getBlockById(@RequestParam("attribute") Integer attribute,@RequestParam("role") String role);

    @RequestMapping(value = "/contract/updateArchive",method = RequestMethod.POST)
    Integer updateArchive(@RequestBody ContractArchive ca);

    @GetMapping("/contract/deleteArchive")
    Integer deleteArchive(@RequestParam("id") Integer id);

    @RequestMapping(value = "/contract/getOutput",method = RequestMethod.POST)
    List<LinkedHashMap<String,Object>> getOutput(@RequestParam("bolockId") Integer bolockId,
                                                 @RequestParam("contentTypeId") Integer contentTypeId,
                                                 @RequestBody Map<String, String> map,
                                                 @RequestParam("startDate") String startDate,
                                                 @RequestParam("endDate") String endDate);

    @GetMapping("/contract/getTotals")
    Map<String, Object> getTotals(@RequestParam("startDate") String startDate,
                                  @RequestParam("endDate") String endDate,
                                  @RequestParam("blockId") Integer blockId,
                                  @RequestParam("contractTypeId") Integer contractTypeId);

    @GetMapping("/contract/getOpratorTag")
    Integer getOpratorTag(@RequestParam("contractId") Integer contractId);

}

package com.zx.servicecontract.controller;

import com.zx.servicecontract.model.ContractArchive;
import com.zx.servicecontract.pojo.Message;
import com.zx.servicecontract.service.ContractService;
import com.zx.servicecontract.vo.ContractList;
import com.zx.servicecontract.vo.ContractVo;
import com.zx.servicecontract.vo.ShowContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ContractController {

    @Autowired
    ContractService contractService;

    @RequestMapping(value = "/contract/addContract",method = RequestMethod.POST)
    Message addContract(@RequestBody ContractVo contractVo,@RequestParam int userId){
        return contractService.addContract(contractVo,userId);
    }

    @GetMapping("/contract/buildContractNumber")
    String buildContractNumber(@RequestParam int blockId,@RequestParam int contractTypeId,@RequestParam long timestamp){
        return contractService.buildContractNumber(blockId,contractTypeId,timestamp);
    }

    @PostMapping("/contract/updateContract")
    Message updateContract(@RequestBody ContractVo contractVo,@RequestParam int userId){
        return contractService.updateContract(contractVo,userId);
    }

    @GetMapping("/contract/deleteContract")
    Message deleteContract(@RequestParam Integer id){
        return contractService.deleteContract(id);
    }

    @GetMapping("/contract/getContractServiceTime")
    Message getContractServiceTime(@RequestParam Integer contractId){
        return contractService.getContractServiceTime(contractId);
    }

    @GetMapping("/contract/lookUpContractAll")
    ShowContract lookUpContractAll(@RequestParam Integer id){
        return contractService.lookUpContractAll(id);
    }

    @GetMapping("/contract/lookUpContractNoMoney")
    ShowContract lookUpContractNoMoney(@RequestParam Integer id){
        return contractService.lookUpContractAll(id);
    }

    @GetMapping("/contract/getContractMoney")
    Message getContractMoney(@RequestParam Integer contractId){
        return contractService.getContractMoney(contractId);
    }

    @GetMapping("/contract/lookUpBlockDuty")
    List<Integer> lookUpBlockDuty(@RequestParam Integer id){
        return contractService.lookUpBlockDuty(id);
    }

    @GetMapping("/contract/lookUpBlockOperatorDuty")
    List<Integer> lookUpBlockOperatorDuty(@RequestParam Integer id){
        return contractService.lookUpBlockOperatorDuty(id);
    }

    @GetMapping("/contract/lookUpSaler")
    List<Integer> lookUpSaler(@RequestParam Integer id){
        return contractService.lookUpSaler(id);
    }

    @GetMapping("/contract/getArchiveNumber")
    String getArchiveNumber(@RequestParam Integer contractId){
        return contractService.getArchiveNumber(contractId);
    }

    @GetMapping("/contract/getContractList")
    List<ContractList> getContractList(@RequestParam(required = false) String key,@RequestParam(required = false) Integer blockId,@RequestParam(required = false) Integer typeId,@RequestParam(required = false) long startstamp,@RequestParam(required = false) long endstamp,@RequestParam(required = false) Integer sellerId,@RequestParam(required = false) Integer archiveTag){
        return contractService.getContractList(key, blockId, typeId, startstamp, endstamp, sellerId, archiveTag);
    }

    @GetMapping("/contract/executeList")
    List<String> executeList(@RequestParam Integer id){
        return contractService.executeList(id);
    }

    @RequestMapping(value="/contract/archiveContract",method = RequestMethod.POST)
    Integer archiveContract(@RequestBody ContractArchive ca){
        return contractService.archiveContract(ca);
    }

    @GetMapping("/contract/showArchive")
    Map<String, Object> showArchive(@RequestParam Integer id){
        return contractService.showArchive(id);
    }

    @GetMapping("/contract/getBlockById")
    Integer getBlockById(@RequestParam Integer attribute,@RequestParam String role){
        return contractService.getBlockById(attribute, role);
    }

    @RequestMapping(value = "/contract/updateArchive",method = RequestMethod.POST)
    Integer updateArchive(@RequestBody ContractArchive ca){
        return contractService.updateArchive(ca);
    }

    @GetMapping("/contract/deleteArchive")
    Integer deleteArchive(@RequestParam Integer id){
        return contractService.deleteArchive(id);
    }

    @RequestMapping(value = "/contract/getOutput",method = RequestMethod.POST)
    List<LinkedHashMap<String,Object>> getOutput(@RequestParam(required = false) Integer bolockId,@RequestParam(required = false) Integer contentTypeId,@RequestBody Map<String, String> map,@RequestParam(required = false) String startDate,@RequestParam(required = false) String endDate){
        return contractService.getOutput(bolockId, contentTypeId, map, startDate, endDate);
    }

    @GetMapping("/contract/getTotals")
    Map<String, Object> getTotals(@RequestParam(required = false) String startDate,@RequestParam(required = false) String endDate,@RequestParam(required = false) Integer blockId,@RequestParam(required = false) Integer contractTypeId){
        return contractService.getTotals(startDate, endDate, blockId, contractTypeId);
    }

    @GetMapping("/contract/getOpratorTag")
    Integer getOpratorTag(@RequestParam Integer contractId){
        return contractService.getOpratorTag(contractId);
    }

}

package com.zx.servicecontract.service;

import com.zx.servicecontract.mapper.ContractTypeDAO;
import com.zx.servicecontract.model.ContractType;
import com.zx.servicecontract.model.ContractTypeExample;
import com.zx.servicecontract.pojo.Message;
import com.zx.servicecontract.service.ContractTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractTypeService {

    @Autowired
    ContractTypeDAO contractTypeDAO;

    public Message addContractType(String typeName) {

        ContractTypeExample example = new ContractTypeExample();
        example.or().andNameEqualTo(typeName);
        List<ContractType> contractTypes = contractTypeDAO.selectByExample(example);
        if(contractTypes.size()!=0){
            return Message.createErr(501,"duplicate name");
        }

        ContractType contractType = new ContractType();
        contractType.setName(typeName);
        contractTypeDAO.insert(contractType);
        int id = contractType.getId();

        if(id>25){
            return Message.createErr(502,"too much type");
        }
        char tag = (char)(id+64);
        contractType.setId(id);
        contractType.setTag(String.valueOf(tag));
        contractTypeDAO.updateByPrimaryKey(contractType);

        return Message.createSuc(null);
    }

    public Message getContractType() {
        return Message.createSuc(contractTypeDAO.selectByExample(new ContractTypeExample()));
    }

    public Message renameContractType(int id, String typeName) {

        ContractTypeExample example = new ContractTypeExample();
        example.or().andNameEqualTo(typeName);
        List<ContractType> contractTypes = contractTypeDAO.selectByExample(example);
        if(contractTypes.size()!=0){
            return Message.createErr(501,"duplicate name");
        }

        ContractType contractType = contractTypeDAO.selectByPrimaryKey(id);
        if(contractType!=null){
            if(contractType.getName().equals("运维技术服务")||contractType.getName().equals("环保工程")){
                return Message.createErr(501,"cant change special type name");
            }
            contractType.setName(typeName);
            contractTypeDAO.updateByPrimaryKey(contractType);
        }

        return Message.createSuc(null);
    }
}

package com.zx.servicecontract.service;

import com.zx.servicecontract.mapper.ContentTypeDAO;
import com.zx.servicecontract.model.ContentType;
import com.zx.servicecontract.model.ContentTypeExample;
import com.zx.servicecontract.pojo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentTypeService{

    @Autowired
    ContentTypeDAO contentTypeDAO;


    public Message addContentType(int contractTypeId, String name) {

        ContentTypeExample example = new ContentTypeExample();
        example.or().andNameEqualTo(name).andContractTypeEqualTo(contractTypeId);
        List<ContentType> contentTypes = contentTypeDAO.selectByExample(example);
        if(contentTypes.size()!=0){
            return Message.createErr(501,"duplicate name");
        }

        ContentType contentType = new ContentType();
        contentType.setContractType(contractTypeId);
        contentType.setName(name);
        contentTypeDAO.insert(contentType);

        return Message.createSuc(null);
    }


    public Message getContentType(int contractTypeId) {

        ContentTypeExample example = new ContentTypeExample();
        example.or().andContractTypeEqualTo(contractTypeId);

        return Message.createSuc(contentTypeDAO.selectByExample(example));
    }


    public Message renameContentType(int id, String name) {

        ContentTypeExample example = new ContentTypeExample();
        example.or().andNameEqualTo(name);
        List<ContentType> contentTypes = contentTypeDAO.selectByExample(example);
        if(contentTypes.size()!=0){
            return Message.createErr(501,"duplicate name");
        }

        ContentType contentType = contentTypeDAO.selectByPrimaryKey(id);
        if(contentType!=null){
            contentType.setName(name);
            contentTypeDAO.updateByPrimaryKey(contentType);
        }
        return Message.createSuc(null);
    }
}

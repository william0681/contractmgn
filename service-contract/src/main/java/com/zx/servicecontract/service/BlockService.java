package com.zx.servicecontract.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zx.servicecontract.mapper.BlockDAO;
import com.zx.servicecontract.mapper.BlockUserDAO;
import com.zx.servicecontract.mapper.UserDAO;
import com.zx.servicecontract.model.Block;
import com.zx.servicecontract.model.BlockExample;
import com.zx.servicecontract.model.BlockUser;
import com.zx.servicecontract.model.BlockUserExample;
import com.zx.servicecontract.pojo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlockService {

    @Autowired
    BlockDAO blockDAO;
    @Autowired
    BlockUserDAO blockUserDAO;
    @Autowired
    UserDAO userDAO;

    public Message addBlock(String name) {

        BlockExample example = new BlockExample();
        example.or().andNameEqualTo(name);
        if(blockDAO.selectByExample(example).size()>0){
            return Message.createErr(501,"repeat name");
        }
        Block block = new Block();
        block.setName(name);
        blockDAO.insert(block);
        String tag = "0"+String.valueOf(block.getId());
        block.setTag(tag);
        blockDAO.updateByPrimaryKey(block);

        BlockUser blockUser = new BlockUser();
        blockUser.setBlockId(block.getId());
        blockUserDAO.insertSelective(blockUser);

        return Message.createSuc(null);

    }

    public Message getBlock() {

        List<Block> blocks = blockDAO.selectByExample(new BlockExample());
        JSONArray array = new JSONArray();
        for(Block block : blocks){
            JSONObject obj = new JSONObject();
            obj.put("id",block.getId());
            obj.put("name",block.getName());
            obj.put("tag",block.getTag());
            BlockUserExample example = new BlockUserExample();
            example.or().andBlockIdEqualTo(block.getId());
            List<BlockUser> blockUsers = blockUserDAO.selectByExample(example);
            if(blockUsers.get(0).getOperateId()!=null){
                obj.put("operator",userDAO.selectByPrimaryKey(blockUsers.get(0).getOperateId()).getName());
            }else{
                obj.put("operator","无");
            }
            if(blockUsers.get(0).getUserId()!=null){
                obj.put("responser",userDAO.selectByPrimaryKey(blockUsers.get(0).getUserId()).getName());
            }else{
                obj.put("responser","无");
            }
            array.add(obj);
        }

        return Message.createSuc(array);
    }

    public Message renameBlock(int id, String name) {

        Block block = blockDAO.selectByPrimaryKey(id);
        if(block!=null){
            block.setName(name);
            blockDAO.updateByPrimaryKey(block);
        }

        return Message.createSuc(null);
    }

    public Message setBlockResponser(int id, int responser) {

        BlockUserExample example = new BlockUserExample();
        example.or().andBlockIdEqualTo(id);
        List<BlockUser> blockUsers = blockUserDAO.selectByExample(example);
        BlockUser blockUser = blockUsers.get(0);
        blockUser.setUserId(responser);
        blockUserDAO.updateByPrimaryKey(blockUser);

        return Message.createSuc(null);
    }

    public Message setBlockOperator(int id, int operator) {

        BlockUserExample example = new BlockUserExample();
        example.or().andBlockIdEqualTo(id);
        List<BlockUser> blockUsers = blockUserDAO.selectByExample(example);
        BlockUser blockUser = blockUsers.get(0);
        blockUser.setOperateId(operator);
        blockUserDAO.updateByPrimaryKey(blockUser);

        return Message.createSuc(null);

    }
}

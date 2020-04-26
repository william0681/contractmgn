package com.zx.servicereminder.mapper;

import com.zx.servicereminder.model.BlockUser;
import com.zx.servicereminder.model.BlockUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BlockUserDAO {
    long countByExample(BlockUserExample example);

    int deleteByExample(BlockUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BlockUser record);

    int insertSelective(BlockUser record);

    List<BlockUser> selectByExample(BlockUserExample example);

    BlockUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BlockUser record, @Param("example") BlockUserExample example);

    int updateByExample(@Param("record") BlockUser record, @Param("example") BlockUserExample example);

    int updateByPrimaryKeySelective(BlockUser record);

    int updateByPrimaryKey(BlockUser record);
}
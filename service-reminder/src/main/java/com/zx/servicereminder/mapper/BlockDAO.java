package com.zx.servicereminder.mapper;

import com.zx.servicereminder.model.Block;
import com.zx.servicereminder.model.BlockExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BlockDAO {
    long countByExample(BlockExample example);

    int deleteByExample(BlockExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Block record);

    int insertSelective(Block record);

    List<Block> selectByExample(BlockExample example);

    Block selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Block record, @Param("example") BlockExample example);

    int updateByExample(@Param("record") Block record, @Param("example") BlockExample example);

    int updateByPrimaryKeySelective(Block record);

    int updateByPrimaryKey(Block record);
}
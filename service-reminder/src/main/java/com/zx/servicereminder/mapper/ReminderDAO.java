package com.zx.servicereminder.mapper;

import com.zx.servicereminder.model.Reminder;
import com.zx.servicereminder.model.ReminderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ReminderDAO {
    long countByExample(ReminderExample example);

    int deleteByExample(ReminderExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Reminder record);

    int insertSelective(Reminder record);

    List<Reminder> selectByExample(ReminderExample example);

    Reminder selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Reminder record, @Param("example") ReminderExample example);

    int updateByExample(@Param("record") Reminder record, @Param("example") ReminderExample example);

    int updateByPrimaryKeySelective(Reminder record);

    int updateByPrimaryKey(Reminder record);
}
package com.zx.servicecontract.service;

import com.zx.servicecontract.mapper.ExecuteNodeDAO;
import com.zx.servicecontract.model.ExecuteNode;
import com.zx.servicecontract.model.ExecuteNodeExample;
import com.zx.servicecontract.pojo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExecuteNodeService{
    @Autowired
    private ExecuteNodeDAO executeNodeDAO;
    @Autowired
    private ReminderService reminderService;

    public Message addExecuteNode(ExecuteNode executeNode, int userId) {
        executeNodeDAO.insertSelective(executeNode);
        reminderService.insertReminder(executeNode.getContractId(),1,"执行节点更新",userId);
        return Message.createSuc(null);

    }

    public Message remindExecuteNode() {
        //执行节点添加后提醒给总经理 财务 总负责人，以上用户登陆时调用此方法，通过查询数据库实现
        return null;
    }

    public Message showExecuteNode(Integer id) {
        ExecuteNodeExample executeNodeExample = new ExecuteNodeExample();
        ExecuteNodeExample.Criteria criteria = executeNodeExample.createCriteria();
        criteria.andContractIdEqualTo(id);
        executeNodeExample.setOrderByClause("execute_date");
        List<ExecuteNode> executeNodeList = executeNodeDAO.selectByExample(executeNodeExample);
        return Message.createSuc(executeNodeList);
    }
}

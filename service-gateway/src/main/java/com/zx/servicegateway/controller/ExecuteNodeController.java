package com.zx.servicegateway.controller;

import com.zx.servicegateway.model.ExecuteNode;
import com.zx.servicegateway.pojo.Message;
import com.zx.servicegateway.service.ExecuteNodeService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ExecuteNodeController {
    @Autowired
    private ExecuteNodeService executeNodeService;

    /**
     * 执行节点的添加
     *
     * @param executeNode
     * @return Message
     */
    @RequiresRoles(value = {"manager", "accountant", "overallchief", "areachief","operator"}, logical = Logical.OR)
    @PostMapping("/executeNode")
    public Message addExecuteNode(@RequestBody ExecuteNode executeNode) {
        Subject subject = SecurityUtils.getSubject();
        return executeNodeService.addExecuteNode(executeNode,(int)subject.getSession().getAttribute("userId"));
    }

    @RequiresRoles(value = {"manager", "accountant", "overallchief"}, logical = Logical.OR)
    @GetMapping("/executeNode")
    public Message remindExecuteNode() {
        return executeNodeService.remindExecuteNode();
    }

    /**
     * 合同执行节点显示
     *
     * @param id 合同id
     * @return Message
     */
    @GetMapping("/executeNode/{id}")
    public Message showExecuteNode(@PathVariable("id") Integer id) {
        return executeNodeService.showExecuteNode(id);
    }
}

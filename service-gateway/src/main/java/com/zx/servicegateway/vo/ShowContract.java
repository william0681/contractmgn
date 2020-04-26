package com.zx.servicegateway.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zx.servicegateway.model.SimpleSeller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ShowContract implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 合同Id
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer id;

    /**
     * 合同编号
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String number;

    public Integer getContractTag() {
        return contractTag;
    }

    public void setContractTag(Integer contractTag) {
        this.contractTag = contractTag;
    }

    @JsonProperty("contract_tag")



    private  Integer contractTag;

    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd",locale = "zh",timezone = "GMT+8")
    @JsonProperty("create_date")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date createDate;

    /**
     * 合同类别
     */
    @JsonProperty("contract_type")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String contractType;

    /**
     * 合同内容类别
     */
    @JsonProperty("content_type")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String contentType;

    /**
     * 合同名称
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;

    /**
     * 归属区块
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String block;

    /**
     * 经办人
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String operator;

    /**
     * 技术服务员
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String technician;

    /**
     * 客户名称
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String customer;

    /**
     * 合同签订日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd",locale = "zh",timezone = "GMT+8")
    @JsonProperty("sign_date")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date signDate;

    /**
     * 合同到期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd",locale = "zh",timezone = "GMT+8")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date due;

    /**
     * 合同金额
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double amount;

    /**
     * 合同内容
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String content;

    /**
     * 合同服务开始时间（运维类）
     */
    @JsonProperty("service_start")
    @JsonFormat(pattern = "yyyy-MM-dd",locale = "zh",timezone = "GMT+8")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date serviceStart;

    /**
     * 合同服务结束时间（运维类合同）
     */
    @JsonProperty("service_end")
    @JsonFormat(pattern = "yyyy-MM-dd",locale = "zh",timezone = "GMT+8")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date serviceEnd;

    /**
     * 归档标记
     */

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean archiveTag;

    /**
     * 销售服务员
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SimpleSeller>  sellers;




    /**
     * 合同各个节点的金额
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Double> nodeMoneys;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getTechnician() {
        return technician;
    }

    public void setTechnician(String technician) {
        this.technician = technician;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    public Date getDue() {
        return due;
    }

    public void setDue(Date due) {
        this.due = due;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getServiceStart() {
        return serviceStart;
    }

    public void setServiceStart(Date serviceStart) {
        this.serviceStart = serviceStart;
    }

    public Date getServiceEnd() {
        return serviceEnd;
    }

    public void setServiceEnd(Date serviceEnd) {
        this.serviceEnd = serviceEnd;
    }

    public Boolean getArchiveTag() {
        return archiveTag;
    }

    public void setArchiveTag(Boolean archiveTag) {
        this.archiveTag = archiveTag;
    }

    public List<SimpleSeller> getSellers() {
        return sellers;
    }

    public void setSellers(List<SimpleSeller> sellers) {
        this.sellers = sellers;
    }

    public List<Double> getNodeMoneys() {
        return nodeMoneys;
    }


    public void setNodeMoneys(List<Double> nodeMoneys) {
        this.nodeMoneys = nodeMoneys;
    }
}

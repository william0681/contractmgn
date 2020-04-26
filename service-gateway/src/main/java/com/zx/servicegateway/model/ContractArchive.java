package com.zx.servicegateway.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ContractArchive implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 归档id
     */
    private Integer id;

    /**
     * 合同id
     */
    @JsonProperty("contract_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer contractId;

    /**
     * 合同柜号
     */

    @JsonProperty("position_number")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String positionNumber;

    /**
     * 档案号
     */
    @JsonProperty("archive_number")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String archiveNumber;

    /**
     * 数据录入员
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("entry_id")
    private Integer entryId;

    /**
     * 原件保管员
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String clerk;

    /**
     * 合同文件名
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("contract_contents")
    private String contractContents;

    /**
     * 附件名
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String others;


    /**
     * 备注
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String comment;

    /**
     * 归档时间
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("archive_date")
    //@JsonFormat(pattern = "yyyy-MM-dd",locale = "zh",timezone = "GMT+8")
    private String archiveDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public String getPositionNumber() {
        return positionNumber;
    }

    public void setPositionNumber(String positionNumber) {
        this.positionNumber = positionNumber;
    }

    public String getArchiveNumber() {
        return archiveNumber;
    }

    public void setArchiveNumber(String archiveNumber) {
        this.archiveNumber = archiveNumber;
    }

    public Integer getEntryId() {
        return entryId;
    }

    public void setEntryId(Integer entryId) {
        this.entryId = entryId;
    }

    public String getClerk() {
        return clerk;
    }

    public void setClerk(String clerk) {
        this.clerk = clerk;
    }

    public String getContractContents() {
        return contractContents;
    }

    public void setContractContents(String contractContents) {
        this.contractContents = contractContents;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    /**
     * @param archiveDate the archiveDate to set
     */
    public void setArchiveDate(String archiveDate) {
        this.archiveDate = archiveDate;
    }

    /**
     * @return the archiveDate
     */
    public String getArchiveDate() {
        return archiveDate;
    }
}

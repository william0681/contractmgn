package com.zx.servicefinance.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class SimpleSeller implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * 销售员id
     */

    @JsonProperty("sellerId")
    private  Integer id;

    /**
     * 销售员名称
     */

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

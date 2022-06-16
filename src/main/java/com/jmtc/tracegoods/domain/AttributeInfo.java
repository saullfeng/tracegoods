package com.jmtc.tracegoods.domain;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Chris
 * @date 2021/6/7 20:42
 * @Email:gang.wu@nexgaming.com
 */
public class AttributeInfo {
    @ApiModelProperty(notes = "Attribute id")
    private String id;

    @ApiModelProperty(notes = "name",required = true)
    private String name;

    @ApiModelProperty(notes = "date",required = true)
    private String date;

    @ApiModelProperty(notes = "desc",required = true)
    private String desc;

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public String getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

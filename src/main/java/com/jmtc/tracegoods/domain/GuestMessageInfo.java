package com.jmtc.tracegoods.domain;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Chris
 * @date 2021/6/8 15:32
 * @Email:gang.wu@nexgaming.com
 */
public class GuestMessageInfo {
    @ApiModelProperty(notes = "name",required = true)
    private String name;

    @ApiModelProperty(notes = "date",required = true)
    private String date;

    @ApiModelProperty(notes = "message",required = true)
    private String message;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

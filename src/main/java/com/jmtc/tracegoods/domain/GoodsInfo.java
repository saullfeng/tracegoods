package com.jmtc.tracegoods.domain;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigInteger;

/**
 * @author Chris
 * @date 2021/6/7 20:54
 * @Email:gang.wu@nexgaming.com
 */
public class GoodsInfo {
    @ApiModelProperty(notes = "name",required = true)
    private String name;

    @ApiModelProperty(notes = "price",required = true)
    private BigInteger price;

    @ApiModelProperty(notes = "weight",required = true)
    private BigInteger weight;

    public String getName() {
        return name;
    }

    public BigInteger getPrice() {
        return price;
    }

    public BigInteger getWeight() {
        return weight;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigInteger price) {
        this.price = price;
    }

    public void setWeight(BigInteger weight) {
        this.weight = weight;
    }
}

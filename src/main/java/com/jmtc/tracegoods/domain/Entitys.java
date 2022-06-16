package com.jmtc.tracegoods.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * @author Chris
 * @date 2021/6/8 11:52
 * @Email:gang.wu@nexgaming.com
 */
public class Entitys implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

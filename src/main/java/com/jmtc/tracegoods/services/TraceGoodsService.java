package com.jmtc.tracegoods.services;

import com.jmtc.tracegoods.domain.AttributeInfo;
import com.jmtc.tracegoods.domain.GoodsInfo;
import com.jmtc.tracegoods.domain.GuestMessageInfo;
import com.jmtc.tracegoods.domain.LogisticInfo;

import java.util.List;

/**
 * @author Chris
 * @date 2021/6/1 21:18
 * @Email:gang.wu@nexgaming.com
 */
public interface TraceGoodsService {
    GoodsInfo getGoodsInfo();
    LogisticInfo getLogisticInfoById(Integer logID);
    Boolean addLogisticInfo(String id,String date, Integer status, String message);
    List<LogisticInfo> getLogisticsList();

    AttributeInfo getAttributeInfoById(Integer attributeId);
    Boolean addAttribute(String id,String name,String date, String desc);
    List<AttributeInfo> getAttributeList();

    GuestMessageInfo getMessageInfoById(Integer messageId);
    Boolean addMessage(String name,String date, String message);
    List<GuestMessageInfo> getMessageList();
}

package com.jmtc.tracegoods.services.impl;

import com.jmtc.tracegoods.antchain.TrackGoodsChainApi;
import com.jmtc.tracegoods.domain.AttributeInfo;
import com.jmtc.tracegoods.domain.GoodsInfo;
import com.jmtc.tracegoods.domain.GuestMessageInfo;
import com.jmtc.tracegoods.domain.LogisticInfo;
import com.jmtc.tracegoods.services.TraceGoodsService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author Chris
 * @date 2021/6/1 21:19
 * @Email:gang.wu@nexgaming.com
 */

@Service("TraceGoodsService")
public class TraceGoodsServiceImpl implements TraceGoodsService {
    TrackGoodsChainApi trackGoodsChainApi ;

    @PostConstruct
    public void init(){
        try {
            trackGoodsChainApi = new TrackGoodsChainApi();
            trackGoodsChainApi.initAntSDK();
        } catch (Exception e) {
            trackGoodsChainApi = null;
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize()throws Throwable{
        if(trackGoodsChainApi!= null){
            trackGoodsChainApi.shutDown();
        }
    }

    @Override
    public GoodsInfo getGoodsInfo() {
        GoodsInfo info = null;
        if(trackGoodsChainApi != null){
            info = trackGoodsChainApi.getGoodsInfo();
        }
        return info ;
    }

    @Override
    public LogisticInfo getLogisticInfoById(Integer logID) {
        LogisticInfo info = null;
        if(trackGoodsChainApi != null){
            info = trackGoodsChainApi.getLogistics(logID);
        }
        return info ;
    }

    @Override
    public Boolean addLogisticInfo(String id, String date, Integer status, String message) {
        if(trackGoodsChainApi != null){
            return trackGoodsChainApi.putLogistics(id,date,status,message);
        } else {
            return false ;
        }
    }

    @Override
    public List<LogisticInfo> getLogisticsList() {
        if(trackGoodsChainApi != null){
            return trackGoodsChainApi.getLogisticsList();
        } else {
            return null ;
        }
    }

    @Override
    public AttributeInfo getAttributeInfoById(Integer attributeId) {
        AttributeInfo info = null;
        if(trackGoodsChainApi != null){
            info = trackGoodsChainApi.getAttributeById(attributeId);
        }
        return info ;
    }

    @Override
    public Boolean addAttribute(String id, String name, String date, String desc) {
        if(trackGoodsChainApi != null){
            return trackGoodsChainApi.putAttribute(id,name,date,desc);
        } else {
            return false ;
        }
    }

    @Override
    public List<AttributeInfo> getAttributeList() {
        if(trackGoodsChainApi != null){
            return trackGoodsChainApi.getAttributeList();
        } else {
            return null ;
        }
    }

    @Override
    public GuestMessageInfo getMessageInfoById(Integer messageId) {
        GuestMessageInfo info = null;
        if(trackGoodsChainApi != null){
            info = trackGoodsChainApi.getMessageById(messageId);
        }
        return info ;
    }

    @Override
    public Boolean addMessage(String name, String date, String message) {
        if(trackGoodsChainApi != null){
            return trackGoodsChainApi.putGuestMessage(name,date,message);
        } else {
            return false ;
        }
    }

    @Override
    public List<GuestMessageInfo> getMessageList() {
        if(trackGoodsChainApi != null){
            return trackGoodsChainApi.getMesageList();
        } else {
            return null ;
        }
    }
}

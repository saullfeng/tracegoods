package com.jmtc.tracegoods.api;

import com.jmtc.tracegoods.comm.oap.LoggerManage;
import com.jmtc.tracegoods.comm.oap.UserLoginToken;
import com.jmtc.tracegoods.domain.AttributeInfo;
import com.jmtc.tracegoods.domain.GoodsInfo;
import com.jmtc.tracegoods.domain.GuestMessageInfo;
import com.jmtc.tracegoods.domain.LogisticInfo;
import com.jmtc.tracegoods.domain.result.NotificationMsg;
import com.jmtc.tracegoods.domain.result.RespEntity;
import com.jmtc.tracegoods.services.TraceGoodsService;
import com.jmtc.tracegoods.utils.DateUtils;
import com.jmtc.tracegoods.utils.Maps;
import com.jmtc.tracegoods.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Chris
 * @date 2021/6/1 21:10
 * @Email:gang.wu@nexgaming.com
 */

@RestController
@RequestMapping("/api/goods")
@Api(value = "TraceGoods APIs", description = "goods operation interface")
public class TraceGoodsController extends BaseController {
    @Resource
    private TraceGoodsService traceGoodsService;

    @UserLoginToken
    @RequestMapping(value="/info", method = RequestMethod.GET,produces = "application/json")
    @LoggerManage(description="获取朔源物品")
    @ApiOperation(value="get good info", notes="获取朔源物品")
    @CrossOrigin
    public RespEntity<Map<String, Object>> getGoodInfo() {
        GoodsInfo goodsInfo = traceGoodsService.getGoodsInfo();
        if(goodsInfo != null){
            Map<String, Object> result = Maps.create().add("goodInfo",goodsInfo ).get();
            return success(result);
        }else {
            return error(NotificationMsg.GOODS_FIND_FAIL);
        }
    }

    @RequestMapping(value="/logistics", method = RequestMethod.GET,produces = "application/json")
    @LoggerManage(description="获取所有交易环节信息")
    @ApiOperation(value="all Logistic", notes="获取所有交易环节信息")
    @CrossOrigin
    @UserLoginToken
    public RespEntity<Map<String, Object>> getLogisticList() {
        List<LogisticInfo> list = traceGoodsService.getLogisticsList();
        if(list.size() > 0){
            Map<String, Object> result = Maps.create().add("LogisticList",list ).get();
            return success(result);
        }else {
            return error(NotificationMsg.LOG_FIND_FAIL);
        }
    }

    @RequestMapping(value="/logistic/{logId}", method = RequestMethod.GET,produces = "application/json")
    @LoggerManage(description="Logistic")
    @ApiOperation(value=" logistic item", notes="get Logistic by id")
    @CrossOrigin
    @UserLoginToken
    public RespEntity<Map<String, Object>> getLogisticsById(@PathVariable("logId") int logId) {
        LogisticInfo info = traceGoodsService.getLogisticInfoById(logId);
        if(info != null){
            Map<String, Object> result = Maps.create().add("LogisticInfo",info ).get();
            return success(result);
        } else {
            return error(NotificationMsg.LOG_FIND_FAIL);
        }
    }

    @ApiOperation(value="new logistic", notes="new logistic")
    @RequestMapping(value = "/logistic/add", method = RequestMethod.PUT,produces = "application/json")
    @LoggerManage(description="AddLogisticInfo")
    @UserLoginToken
    public RespEntity<Map<String, Object>> addLogisticInfo(@RequestBody LogisticInfo logInfo) {
        if(logInfo.getStatus() > 5|| logInfo.getStatus() < 0){
            return error(NotificationMsg.LOG_ADD_FAIL);
        }

        if (logInfo.getDesc().isEmpty()) {
            return  error(NotificationMsg.LOG_ADD_FAIL_DESC);
        }
        Boolean resStatus = traceGoodsService.addLogisticInfo(StringUtil.getRandomString(10), DateUtils.getNowDateTimeStr(),logInfo.getStatus(),logInfo.getDesc());
        if(resStatus){
            return success();
        } else {
            return error(NotificationMsg.LOG_ADD_FAIL);
        }
    }

    @ApiOperation(value="new Attribute", notes="new Attribute")
    @RequestMapping(value = "/attribute/add", method = RequestMethod.PUT,produces = "application/json")
    @LoggerManage(description="AddAttributeInfo")
    @UserLoginToken
    public RespEntity<Map<String, Object>> addAttributeInfo(@RequestBody AttributeInfo attributeInfo) {
        if (attributeInfo.getName().isEmpty()) {
            return  error(NotificationMsg.ATT_ADD_FAIL_NAME);
        }

        if (attributeInfo.getDesc().isEmpty()) {
            return  error(NotificationMsg.ATT_ADD_FAIL_DESC);
        }
        Boolean resStatus = traceGoodsService.addAttribute(StringUtil.getRandomString(10),attributeInfo.getName(), DateUtils.getNowDateTimeStr(),attributeInfo.getDesc());
        if(resStatus){
            return success();
        } else {
            return error(NotificationMsg.LOG_ADD_FAIL);
        }
    }

    @RequestMapping(value="/attributelist", method = RequestMethod.GET,produces = "application/json")
    @LoggerManage(description="获取所有属性")
    @ApiOperation(value="all attribute", notes="获取所有属性")
    @CrossOrigin
    @UserLoginToken
    public RespEntity<Map<String, Object>> getAttributeList() {
        List<AttributeInfo> list = traceGoodsService.getAttributeList();
        if(list.size() > 0){
            Map<String, Object> result = Maps.create().add("attributeList",list ).get();
            return success(result);
        }else {
            return error(NotificationMsg.ATT_FIND_FAIL);
        }
    }
    @ApiOperation(value="new GuestMessage", notes="new GuestMessage")
    @RequestMapping(value = "/GuestMessage/add", method = RequestMethod.PUT,produces = "application/json")
    @LoggerManage(description="AddGuestMessageInfo")
    @UserLoginToken
    public RespEntity<Map<String, Object>> addGuestMessageInfo(@RequestBody GuestMessageInfo guestMessageInfo) {
        if (guestMessageInfo.getName().isEmpty()) {
            return  error(NotificationMsg.GUEM_ADD_FAIL_NAME);
        }

        if (guestMessageInfo.getMessage().isEmpty()) {
            return  error(NotificationMsg.ATT_ADD_FAIL_DESC);
        }
        Boolean resStatus = traceGoodsService.addMessage(guestMessageInfo.getName(), DateUtils.getNowDateTimeStr(),guestMessageInfo.getMessage());
        if(resStatus){
            return success();
        } else {
            return error(NotificationMsg.LOG_ADD_FAIL);
        }
    }
    @RequestMapping(value="/messageList", method = RequestMethod.GET,produces = "application/json")
    @LoggerManage(description="获取所有属性")
    @ApiOperation(value="all guestMessage", notes="获取所有属性")
    @CrossOrigin
    @UserLoginToken
    public RespEntity<Map<String, Object>> getMesageList() {
        List<GuestMessageInfo> list = traceGoodsService.getMessageList();
        if(list.size() > 0){
            Map<String, Object> result = Maps.create().add("messageList",list ).get();
            return success(result);
        }else {
            return error(NotificationMsg.GUEM_FIND_FAIL);
        }
    }



}

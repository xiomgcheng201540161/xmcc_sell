package com.xmcc.wechatorder.controller;

import com.google.common.collect.Maps;
import com.xmcc.wechatorder.bean.OrderBean;
import com.xmcc.wechatorder.bean.Pagebean;
import com.xmcc.wechatorder.common.ResultResponse;
import com.xmcc.wechatorder.dto.OrderMasterDto;
import com.xmcc.wechatorder.service.OrderMasterService;
import com.xmcc.wechatorder.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("buyer/order")
@Api(value = "订单相关接口",description = "完成订单的增删改查")
public class OrderMasterController {
    @Autowired
    private  OrderMasterService orderMasterService;
    @PostMapping("create")
    @ApiOperation(value = "创建订单",httpMethod = "POST" ,response = ResultResponse.class)
    public ResultResponse creat(@Valid  @ApiParam(name="订单对象",value = "传入json格式",required = true)OrderMasterDto orderMasterDto,
                                BindingResult bindingResult){
        HashMap<String, String> map = Maps.newHashMap();
        if(bindingResult.hasErrors()){
            List<String> collect = bindingResult.getFieldErrors().
                    stream().map(err -> err.getDefaultMessage()).collect(Collectors.toList());
            map.put("参数校验错误", JsonUtil.object2string(collect));
        }
        return orderMasterService.insertOrder(orderMasterDto);
    }



    @PostMapping("list")
    @ApiOperation(value = "订单列表",httpMethod = "POST" ,response = ResultResponse.class)
    public ResultResponse list(@Valid  @ApiParam(name="查询条件",value = "传入json格式",required = true) Pagebean pagebean,
                                BindingResult bindingResult){
        HashMap<String, String> map = Maps.newHashMap();
        if(bindingResult.hasErrors()){
            List<String> collect = bindingResult.getFieldErrors().
                    stream().map(err -> err.getDefaultMessage()).collect(Collectors.toList());
            map.put("参数校验错误", JsonUtil.object2string(collect));
        }
        return orderMasterService.orderMasterList(pagebean);
    }



    @PostMapping("detail")
    @ApiOperation(value = "订单详情",httpMethod = "POST" ,response = ResultResponse.class)
    public ResultResponse list(@Valid  @ApiParam(name="查询订单详情条件",value = "传入json格式",required = true)OrderBean orderBean,
                               BindingResult bindingResult){
        HashMap<String, String> map = Maps.newHashMap();
        if(bindingResult.hasErrors()){
            List<String> collect = bindingResult.getFieldErrors().
                    stream().map(err -> err.getDefaultMessage()).collect(Collectors.toList());
            map.put("参数校验错误", JsonUtil.object2string(collect));
        }
        return orderMasterService.orderParticulars(orderBean);
    }


    @PostMapping("cancel")
    @ApiOperation(value = "取消订单",httpMethod = "POST" ,response = ResultResponse.class)
    public ResultResponse cancel(@Valid  @ApiParam(name="取消订单",value = "传入json格式",required = true)OrderBean orderBean,
                               BindingResult bindingResult){
        HashMap<String, String> map = Maps.newHashMap();
        if(bindingResult.hasErrors()){
            List<String> collect = bindingResult.getFieldErrors().
                    stream().map(err -> err.getDefaultMessage()).collect(Collectors.toList());
            map.put("参数校验错误", JsonUtil.object2string(collect));
        }
        return orderMasterService.cancel(orderBean);
    }
}

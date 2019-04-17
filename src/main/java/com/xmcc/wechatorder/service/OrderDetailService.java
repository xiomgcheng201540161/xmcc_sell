package com.xmcc.wechatorder.service;

import com.xmcc.wechatorder.entity.OrderDetail;

import java.util.List;

/**
 * 批量将订单集合中的订单项插入订单项数据库中
 */
public interface OrderDetailService  {
    //批量插入
    void batchInsert(List<OrderDetail> orderDetailList);
}

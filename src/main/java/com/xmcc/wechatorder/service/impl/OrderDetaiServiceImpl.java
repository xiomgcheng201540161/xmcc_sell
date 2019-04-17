package com.xmcc.wechatorder.service.impl;

import com.xmcc.wechatorder.dao.Impl.BatchDaoImpl;
import com.xmcc.wechatorder.entity.OrderDetail;
import com.xmcc.wechatorder.service.OrderDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 批量插入订单项进数据库
 * 引用BatchDaoImpl批量插入
 * 可以自动对应表插入
 */
@Service
public class OrderDetaiServiceImpl extends BatchDaoImpl<OrderDetail> implements OrderDetailService {
    @Override
    @Transactional
    public void batchInsert(List<OrderDetail> orderDetailList) {
        super.batchInsert(orderDetailList);
    }
}

package com.xmcc.wechatorder.service.impl;



import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xmcc.wechatorder.common.OrderEnums;
import com.xmcc.wechatorder.common.PayEnums;
import com.xmcc.wechatorder.common.ResultEnums;
import com.xmcc.wechatorder.common.ResultResponse;
import com.xmcc.wechatorder.dto.OrderDetailDto;
import com.xmcc.wechatorder.dto.OrderMasterDto;
import com.xmcc.wechatorder.entity.OrderDetail;
import com.xmcc.wechatorder.entity.OrderMaster;
import com.xmcc.wechatorder.entity.Productinfo;
import com.xmcc.wechatorder.exception.CustomException;
import com.xmcc.wechatorder.repository.OrderMasterRepository;
import com.xmcc.wechatorder.service.OrderDetailService;
import com.xmcc.wechatorder.service.OrderMasterService;
import com.xmcc.wechatorder.service.ProductInfoService;
import com.xmcc.wechatorder.util.BigDecimalUtil;
import com.xmcc.wechatorder.util.IDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderMasterServiceImpl implements OrderMasterService {
    @Autowired
    private ProductInfoService infoService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private OrderMasterRepository masterRepository;
    @Override
    public ResultResponse insertOrder(OrderMasterDto orderMasterDto) {
        //取出订单项
        List<OrderDetailDto> items = orderMasterDto.getItems();
        //创建集合来储存OrderDetail
        List<OrderDetail> list = Lists.newArrayList();
        //初始化订单总金额
        BigDecimal total = new BigDecimal("0");
        //遍历订单项获取商品详情
        for ( OrderDetailDto dto: items
             ) {
            //查询商品详情
            ResultResponse<Productinfo> resultResponse = infoService.queryById(dto.getProductId());
            if (resultResponse.getCode() == ResultEnums.FAIL.getCode()) {
                throw new CustomException(resultResponse.getMsg());
            }
            //得到商品
            Productinfo data = resultResponse.getData();
            //比较库存
            if (data.getProductStock() < dto.getProductQuantity()) {        //如果库存小于订单量
                throw new CustomException(ResultEnums.PRODUCT_NOT_ENOUGH.getMsg());
            }
            //创建订单项
            OrderDetail build = OrderDetail.builder().detailId(IDUtils.createIdbyUUID())
                    .productIcon(data.getProductIcon()).productId(data.getProductId()).
                            productName(data.getProductName()).productPrice(data.getProductPrice()).
                            productQuantity(dto.getProductQuantity()).build();
            list.add(build);
            //减少库存
            data.setProductStock(data.getProductStock() - dto.getProductQuantity());
            infoService.updateProduct(data);
            //计算价格
            total = BigDecimalUtil.add(total, BigDecimalUtil.multi(data.getProductPrice(), dto.getProductQuantity()));
        }
            //生成orderId
            String orderId = IDUtils.createIdbyUUID();
            //生成订单
            OrderMaster master = OrderMaster.builder().buyerAddress(orderMasterDto.getAddress()).buyerName(orderMasterDto.getName())
                    .buyerOpenid(orderMasterDto.getOpenid()).buyerPhone(orderMasterDto.getPhone()).orderAmount(total)
                    .orderId(orderId).orderStatus(OrderEnums.NEW.getCode()).payStatus(PayEnums.WAIT.
                            getCode()).build();
        //将订单id设置到订单项中
        List<OrderDetail> orderDetails = list.stream().map(orderDetail -> {
            orderDetail.setOrderId(orderId);
            return orderDetail;
        }).collect(Collectors.toList());
        //批量插入订单项
        orderDetailService.batchInsert(orderDetails);
        //插入订单
        masterRepository.save(master);
        HashMap<String, String> map = Maps.newHashMap();
        map.put("orderId",orderId);
        return ResultResponse.success(map);
    };
}

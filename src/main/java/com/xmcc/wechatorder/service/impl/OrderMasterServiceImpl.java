package com.xmcc.wechatorder.service.impl;



import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.xml.internal.bind.v2.TODO;
import com.xmcc.wechatorder.bean.OrderBean;
import com.xmcc.wechatorder.bean.Pagebean;
import com.xmcc.wechatorder.common.OrderEnums;
import com.xmcc.wechatorder.common.PayEnums;
import com.xmcc.wechatorder.common.ResultEnums;
import com.xmcc.wechatorder.common.ResultResponse;
import com.xmcc.wechatorder.dto.OrderDetailDto;
import com.xmcc.wechatorder.dto.OrderMasterDto;
import com.xmcc.wechatorder.dto.OrderMasterListDto;
import com.xmcc.wechatorder.entity.OrderDetail;
import com.xmcc.wechatorder.entity.OrderMaster;
import com.xmcc.wechatorder.entity.Productinfo;
import com.xmcc.wechatorder.exception.CustomException;
import com.xmcc.wechatorder.repository.OrderDetailRepository;
import com.xmcc.wechatorder.repository.OrderMasterRepository;
import com.xmcc.wechatorder.repository.ProductInfoRepository;
import com.xmcc.wechatorder.service.OrderDetailService;
import com.xmcc.wechatorder.service.OrderMasterService;
import com.xmcc.wechatorder.service.ProductInfoService;
import com.xmcc.wechatorder.util.BigDecimalUtil;
import com.xmcc.wechatorder.util.IDUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderMasterServiceImpl implements OrderMasterService {
    @Autowired
    private ProductInfoService infoService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private OrderMasterRepository masterRepository;
    @Autowired
    private OrderDetailRepository detailRepository;
    @Autowired
    private ProductInfoRepository infoRepository;

    /**
     * 创建订单
     * @param orderMasterDto
     * @return
     */
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
    }

    /**
     * 订单列表
     * @param pagebean
     * @return
     */
    @Override
    public ResultResponse orderMasterList(Pagebean pagebean) {
        TODO 校验参数;
        //或得微信号
        String openid = pagebean.getOpenid();
        //更具微信号和size，page条件查询
        Pageable pageable = new PageRequest(pagebean.getPage(), pagebean.getSize());
        //通过微信号查询订单列表
        List<OrderMaster> orderMasters=masterRepository.findAllByBuyerOpenid(pagebean.getOpenid(),pageable);
        List<OrderMasterListDto> masterListDtos = orderMasters.stream().map(orderMaster -> OrderMasterListDto.toDto(orderMaster)).collect(Collectors.toList());

        return ResultResponse.success(masterListDtos);
    }

    /**
     * 查询订单详情
     * @param orderBean
     * @return
     */
    @Override
    public ResultResponse orderParticulars(OrderBean orderBean) {
        String openid = orderBean.getOpenid();
        String orderId = orderBean.getOrderId();
        //通过订单orderId查询订单
        OrderMaster  orderMaster = masterRepository.findByOrderId(orderId);
        if(orderMaster==null){
            throw  new CustomException("没有该订单");
        }
        //将orderMaster转换为orderMasterListDto
        OrderMasterListDto orderMasterListDto = OrderMasterListDto.toDto(orderMaster);
        //查询订单项
        List<OrderDetail> orderDetails = detailRepository.findAllByOrderId(orderId);
        if(orderDetails.size()==0){
            return ResultResponse.success(orderMasterListDto);
        }
        //将订单项写入订单集合中
        orderMasterListDto.setOrderDetailList(orderDetails);
        return ResultResponse.success(orderMasterListDto);
    }

    /**
     * 取消订单
     * @param orderBean
     * @return
     */
    @Transactional
    public ResultResponse cancel(OrderBean orderBean){
        String openid = orderBean.getOpenid();
        String orderId = orderBean.getOrderId();
        OrderMaster  orderMaster = masterRepository.findByOrderId(orderId);
        //判断订单是否存在
        if(orderMaster==null){
            throw  new CustomException("没有该订单");
        }
        Integer orderStatus = orderMaster.getOrderStatus();
        //判断订单状态
        if(orderStatus!=OrderEnums.FINSH.getCode()){
            return ResultResponse.fail(OrderEnums.FINSH_CANCEL.getMsg());
        }
        Integer payStatus = orderMaster.getPayStatus();
        TODO 判断是否需要退钱;
        //修改订单状态订单
        orderMaster.setOrderStatus(OrderEnums.CANCEL.getCode());
        masterRepository.save(orderMaster);
        //修改库存
        List<OrderDetail> orderDetails = detailRepository.findAllByOrderId(orderId);
        for (OrderDetail details: orderDetails
             ) {
            String productId = details.getProductId();
            Integer count = details.getProductQuantity();
            Optional<Productinfo> byId = infoRepository.findById(productId);
            if(!byId.isPresent()){            //如果查询出来的商品信息为空
                throw  new CustomException(ResultEnums.NOT_EXITS.getMsg());
            }
            Productinfo productinfo = byId.get();
            //修改库存
            productinfo.setProductStock(productinfo.getProductStock()-count);
            infoService.updateProduct(productinfo);

        }
        return ResultResponse.success();
    }

}

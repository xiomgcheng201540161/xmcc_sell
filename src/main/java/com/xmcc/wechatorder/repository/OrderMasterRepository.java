package com.xmcc.wechatorder.repository;

import com.xmcc.wechatorder.entity.OrderMaster;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderMasterRepository extends JpaRepository<OrderMaster,String> {

    List<OrderMaster> findAllByBuyerOpenid(String  openid, Pageable pageable);

   OrderMaster findByOrderId(String orderId);




}

package com.xmcc.wechatorder.repository;

import com.xmcc.wechatorder.entity.Productinfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductInfoRepository extends JpaRepository<Productinfo, String> {
    //根据商品的类目和状态查询 List<Productinfo>
    List<Productinfo> findByProductStatusAndCategoryTypeIn(Integer staus,List<Integer> typeList);
}

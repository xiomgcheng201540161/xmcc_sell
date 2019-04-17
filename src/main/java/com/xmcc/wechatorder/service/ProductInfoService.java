package com.xmcc.wechatorder.service;

import com.xmcc.wechatorder.common.ResultResponse;
import com.xmcc.wechatorder.entity.Productinfo;

import java.util.List;

public interface ProductInfoService {
    ResultResponse queryList();
    ResultResponse<Productinfo> queryById(String productId);
    void updateProduct(Productinfo productinfo);
}

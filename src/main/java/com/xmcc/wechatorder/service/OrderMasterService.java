package com.xmcc.wechatorder.service;

import com.xmcc.wechatorder.bean.OrderBean;
import com.xmcc.wechatorder.bean.Pagebean;
import com.xmcc.wechatorder.common.ResultResponse;
import com.xmcc.wechatorder.dto.OrderMasterDto;

public interface OrderMasterService {
    ResultResponse insertOrder(OrderMasterDto orderMasterDto);
    ResultResponse orderMasterList(Pagebean pagebean);
    ResultResponse orderParticulars(OrderBean orderBean);
    ResultResponse cancel(OrderBean orderBean);

}

package com.xmcc.wechatorder.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderBean {
    //微信id
    @NotBlank
    private String openid;
    //订单id
    @NotBlank
    private String orderId;
}

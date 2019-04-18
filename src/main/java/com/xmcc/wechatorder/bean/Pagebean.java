package com.xmcc.wechatorder.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pagebean {
    @NotBlank
    private String openid;         //  微信号
    private Integer page=0;        //从第几页开始，默认0
    private Integer size=10;       //查几条，默认10
}

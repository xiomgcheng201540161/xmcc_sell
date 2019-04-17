package com.xmcc.wechatorder.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
@ApiModel("订单实体参数类")    //swagger 参数的描述信息
public class OrderDetailDto implements Serializable {
    @NotBlank(message = "商品id不能为空")
    @ApiModelProperty(value = "商品id",dataType = "String") //swagger参数的描述信息
    private String productId;
    @NotNull(message = "商品数量不能为空")
    @Min(value = 1,message = "数量不能少于一件")
    @ApiModelProperty(value = "商品数量",dataType = "Integer" ,example = "1")
    private Integer productQuantity;
}

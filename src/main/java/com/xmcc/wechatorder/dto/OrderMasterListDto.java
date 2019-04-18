package com.xmcc.wechatorder.dto;

import com.xmcc.wechatorder.common.OrderEnum;
import com.xmcc.wechatorder.common.PayEnum;
import com.xmcc.wechatorder.entity.OrderDetail;
import com.xmcc.wechatorder.entity.OrderMaster;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.BeanUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("订单详情实体类")
public class OrderMasterListDto  implements Serializable {
    /** 订单id. */
    @Id
    private String orderId;

    /** 买家名字. */
    private String buyerName;

    /** 买家手机号. */
    private String buyerPhone;

    /** 买家地址. */
    private String buyerAddress;

    /** 买家微信Openid. */
    private String buyerOpenid;

    /** 订单总金额. */
    private BigDecimal orderAmount;

    /** 订单状态, 默认为0新下单. */
    private Integer orderStatus = OrderEnum.NEW.getCode();

    /** 支付状态, 默认为0未支付. */
    private Integer payStatus = PayEnum.WAIT.getCode();

    /** 创建时间. */
    private Date createTime;

    /** 更新时间. */
    private Date updateTime;

    private List<OrderDetail> orderDetailList =new ArrayList<>();


    public static  OrderMasterListDto toDto(OrderMaster orderMaster){
        OrderMasterListDto listDto = new OrderMasterListDto();
        BeanUtils.copyProperties(orderMaster,listDto);
        return listDto;
    }
}

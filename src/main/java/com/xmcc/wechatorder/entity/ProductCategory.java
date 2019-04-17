package com.xmcc.wechatorder.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
@Entity
@DynamicUpdate
@Data
@AllArgsConstructor
@NoArgsConstructor
//表明所用的表名
@Table(name = "product_category")
public class ProductCategory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;
    private String  categoryName;
    private Integer categoryType;
    private Date  createTime;
    private Date  updateTime;
}

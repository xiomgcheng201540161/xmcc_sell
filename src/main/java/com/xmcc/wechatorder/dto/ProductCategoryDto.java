package com.xmcc.wechatorder.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xmcc.wechatorder.entity.ProductCategory;
import com.xmcc.wechatorder.entity.Productinfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryDto implements Serializable {
    @JsonProperty("name")//返回前段的时候以name的形式返回
    private String categoryName;
    @JsonProperty("type")
    private Integer categoryType;
    @JsonProperty("foods")
    private List<ProductInfoDto> categoryDtoList;

    //将ProductCategory转换成ProductCategoryDto
  /*  public static ProductCategoryDto build(ProductCategory productCategory){
        ProductCategoryDto categoryDto = new ProductCategoryDto();
        BeanUtils.copyProperties(productCategory,categoryDto);
        return categoryDto;
    }*/

    public static ProductCategoryDto build(ProductCategory productCategory){
        ProductCategoryDto productCategoryDto = new ProductCategoryDto();
        BeanUtils.copyProperties(productCategory,productCategoryDto);
        return productCategoryDto;
    }

}

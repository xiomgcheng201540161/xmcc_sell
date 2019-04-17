package com.xmcc.wechatorder.service.impl;

import com.xmcc.wechatorder.common.ResultEnums;
import com.xmcc.wechatorder.common.ResultResponse;
import com.xmcc.wechatorder.dto.ProductCategoryDto;
import com.xmcc.wechatorder.dto.ProductInfoDto;
import com.xmcc.wechatorder.entity.ProductCategory;
import com.xmcc.wechatorder.entity.Productinfo;
import com.xmcc.wechatorder.repository.ProductCategoryRepository;
import com.xmcc.wechatorder.repository.ProductInfoRepository;
import com.xmcc.wechatorder.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class ProductInfoServiceImpl implements ProductInfoService {
    @Autowired
    private ProductCategoryRepository categoryRepository;
    @Autowired
    private ProductInfoRepository infoRepository;
    @Override
    public ResultResponse queryList() {
        //查询所有分类
        List<ProductCategory> productCategoryList = categoryRepository.findAll();
        //将所有的ProductCategory转换为ProductCategoryDto
        List<ProductCategoryDto> productCategoryDtoList
                = productCategoryList.stream().map(productCategory -> ProductCategoryDto.build(productCategory)).collect(Collectors.toList());


        if (CollectionUtils.isEmpty(productCategoryList)) {           //如果获取到的商品分类集合为空
            return ResultResponse.fail();
        }
        //获取类目编号集合
        List<Integer> typeList
                = productCategoryList.stream().map(ProductCategory -> ProductCategory.getCategoryType()).collect(Collectors.toList());
        //根据typeList查询访问列表
        List<Productinfo> productinfoList = infoRepository.findByProductStatusAndCategoryTypeIn(ResultEnums.PRODUCT_UP.getCode(), typeList);


        List<ProductCategoryDto> categoryDtoList = productCategoryDtoList.parallelStream().map(productCategoryDto -> {
            productCategoryDto.setCategoryDtoList(productinfoList.stream()
                    .filter(productinfo -> productCategoryDto.getCategoryType() == productinfo.getCategoryType())
                    .map(productinfo -> ProductInfoDto.build(productinfo)).collect(Collectors.toList()));
            return productCategoryDto;
        }).collect(Collectors.toList());


        /*categoryDtoList.stream().forEach(System.out::println);*/
        return ResultResponse.success(categoryDtoList);



    }
}

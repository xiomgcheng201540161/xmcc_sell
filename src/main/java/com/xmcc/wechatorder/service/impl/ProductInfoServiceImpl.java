package com.xmcc.wechatorder.service.impl;

import com.xmcc.wechatorder.common.ProductEnums;
import com.xmcc.wechatorder.common.ResultEnums;
import com.xmcc.wechatorder.common.ResultResponse;
import com.xmcc.wechatorder.dto.ProductCategoryDto;
import com.xmcc.wechatorder.dto.ProductInfoDto;
import com.xmcc.wechatorder.entity.ProductCategory;
import com.xmcc.wechatorder.entity.Productinfo;
import com.xmcc.wechatorder.repository.ProductCategoryRepository;
import com.xmcc.wechatorder.repository.ProductInfoRepository;
import com.xmcc.wechatorder.service.ProductInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
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

    /**
     * 根据id查询出商品详情
     * @param productId
     * @return
     */
    @Override
    public ResultResponse<Productinfo> queryById(String productId) {
        if(StringUtils.isBlank(productId)){
            return ResultResponse.fail(ResultEnums.PARAM_ERROR.getMsg());
        }
        Optional<Productinfo> id = infoRepository.findById(productId);
        if(!id.isPresent()){            //如果查询出来的商品信息不为空
            return ResultResponse.fail(ResultEnums.NOT_EXITS.getMsg());
        }
        Productinfo productinfo = id.get();
        if(productinfo.getProductStatus()==(ResultEnums.PRODUCT_DOWN.getCode())){       //判断商品状态是否下架
            return ResultResponse.fail(ResultEnums.PRODUCT_DOWN.getMsg());
        }
        return ResultResponse.success(productinfo);
    }

    @Override
    public void updateProduct(Productinfo productinfo) {
        infoRepository.save(productinfo);
    }
}

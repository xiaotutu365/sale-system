package com.trey.product.server.controller;

import com.trey.product.common.vo.DecreaseStockInput;
import com.trey.product.server.entity.ProductCategory;
import com.trey.product.server.entity.ProductInfo;
import com.trey.product.server.service.CategoryService;
import com.trey.product.server.service.ProductService;
import com.trey.product.server.util.ResultVoUtil;
import com.trey.product.server.vo.ProductInfoVo;
import com.trey.product.server.vo.ProductVo;
import com.trey.product.server.vo.ResultVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ResultVo<ProductVo> list() {
        // 1.查询所有在架的商品
        List<ProductInfo> productInfoList = productService.findUpAll();
        // 2.获取类目type列表
        List<Integer> categoryTypeList = productInfoList.stream().map(ProductInfo::getCategoryType)
                .collect(Collectors.toList());
        // 3.从数据库查询类目
        List<ProductCategory> categoryList = categoryService.findByCategoryTypeIn(categoryTypeList);

        List<ProductVo> productVoList = new ArrayList<>();

        categoryList.forEach(productCategory -> {
            ProductVo productVo = new ProductVo();
            productVo.setCategoryName(productCategory.getCategoryName());
            productVo.setCategoryType(productCategory.getCategoryType());

            List<ProductInfoVo> productInfoVoList = new ArrayList<>();
            productInfoList.forEach(productInfo -> {
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())) {
                    ProductInfoVo productInfoVo = new ProductInfoVo();
                    BeanUtils.copyProperties(productInfo, productInfoVo);
                    productInfoVoList.add(productInfoVo);
                }
            });
            productVo.setProductInfoVoList(productInfoVoList);
            productVoList.add(productVo);
        });
        return ResultVoUtil.success(productVoList);
    }

    @PostMapping("/listForOrder")
    public List<ProductInfo> listForOrder(@RequestBody List<String> productIdList) {
        return productService.findList(productIdList);
    }

    @PostMapping("/decreaseStock")
    public void decreaseStock(@RequestBody List<DecreaseStockInput> decreaseStockInputList) {
        productService.decreaseStock(decreaseStockInputList);
    }
}
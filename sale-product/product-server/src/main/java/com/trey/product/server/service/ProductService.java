package com.trey.product.server.service;

import com.alibaba.fastjson.JSON;
import com.trey.product.common.vo.DecreaseStockInput;
import com.trey.product.common.vo.ProductInfoOutput;
import com.trey.product.server.entity.ProductInfo;
import com.trey.product.server.enums.ProductStatusEnum;
import com.trey.product.server.enums.ResultEnum;
import com.trey.product.server.exception.ProductException;
import com.trey.product.server.repository.ProductInfoRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class ProductService {

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 查询所有在架商品列表
     *
     * @return
     */
    public List<ProductInfo> findUpAll() {
        return productInfoRepository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    /**
     * 根据多个商品Id查询多个商品详细信息
     *
     * @param productIdList
     * @return
     */
    public List<ProductInfo> findList(List<String> productIdList) {
        return productInfoRepository.findByProductIdIn(productIdList);
    }

    public void decreaseStock(List<DecreaseStockInput> decreaseStockInputList) {
        List<ProductInfo> productInfoList = decreaseStockProcess(decreaseStockInputList);

        List<ProductInfoOutput> productInfoOutputList = productInfoList.stream().map(productInfo -> {
            ProductInfoOutput output = new ProductInfoOutput();
            BeanUtils.copyProperties(productInfo, output);
            return output;
        }).collect(Collectors.toList());
        // 扣完库存后，发送消息到队列中
        amqpTemplate.convertAndSend("productInfo", JSON.toJSONString(productInfoOutputList));
    }

    /**
     * 扣库存
     */
    @Transactional
    public List<ProductInfo> decreaseStockProcess(List<DecreaseStockInput> decreaseStockInputList) {
        List<ProductInfo> productInfoList = new ArrayList<>();
        decreaseStockInputList.forEach(decreaseStockInput -> {
            Optional<ProductInfo> productInfoOptional = productInfoRepository.findById(decreaseStockInput.getProductId());
            // 如果商品不存在，则抛出异常
            if (!productInfoOptional.isPresent()) {
                throw new ProductException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            ProductInfo productInfo = productInfoOptional.get();
            // 减库存，如果库存不足，则抛出异常
            Integer result = productInfo.getProductStock() - decreaseStockInput.getProductQuantity();
            if (result < 0) {
                throw new ProductException(ResultEnum.PRODUCT_STOCK_ERROR);
            }
            productInfo.setProductStock(result);
            productInfoRepository.save(productInfo);

            // 将购买的商品信息添加进List
            productInfoList.add(productInfo);
        });
        return productInfoList;
    }
}
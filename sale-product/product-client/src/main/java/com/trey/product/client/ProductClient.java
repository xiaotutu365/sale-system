package com.trey.product.client;

import com.trey.product.common.vo.DecreaseStockInput;
import com.trey.product.common.vo.ProductInfoOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "SALE-PRODUCT")
public interface ProductClient {
    @PostMapping("/product/listForObject")
    List<ProductInfoOutput> listForOrder(@RequestBody List<String> productIdList);

    @PostMapping("/product/decreaseStock")
    void decreaseStock(@RequestBody List<DecreaseStockInput> decreaseStockInputList);
}

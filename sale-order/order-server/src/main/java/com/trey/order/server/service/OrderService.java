package com.trey.order.server.service;

import com.trey.order.server.dto.OrderDto;
import com.trey.order.server.entity.OrderDetail;
import com.trey.order.server.entity.OrderMaster;
import com.trey.order.server.enums.OrderStatusEnum;
import com.trey.order.server.enums.PayStatusEnum;
import com.trey.order.server.repository.OrderDetailRepository;
import com.trey.order.server.repository.OrderMasterRepository;
import com.trey.order.server.utils.KeyUtils;
import com.trey.product.client.ProductClient;
import com.trey.product.common.vo.DecreaseStockInput;
import com.trey.product.common.vo.ProductInfoOutput;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private ProductClient productClient;

    /**
     * 订单入库
     * @param orderDto
     * @return
     */
    @Transactional
    public OrderDto create(OrderDto orderDto) {
        String orderId = KeyUtils.genUniqueKey();
        String detailId = KeyUtils.genUniqueKey();

        // 查询商品信息，远程调用商品服务
        List<String> productIdList = orderDto.getOrderDetailList().stream()
                .map(OrderDetail::getProductId)
                .collect(Collectors.toList());
        // TODO
        // 此处可能会出现两个异常
        // 1、网络异常，导致远程调用超时，返回超时异常，事务如何处理？
        // 2、已经提交的远程事务，如何回滚？
        List<ProductInfoOutput> productInfoList = productClient.listForOrder(productIdList);

        // 读redis
        // 减库存并将新值重新设置进redis
        // 订单入库异常，手动回滚redis

        // 计算总价
        final BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
        orderDto.getOrderDetailList().forEach(orderDetail -> {
            productInfoList.forEach(productInfo -> {
                if(productInfo.getProductId().equals(orderDetail.getProductId())) {
                    // 单价 * 数量
                    productInfo.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity()))
                            .add(orderAmount);
                    BeanUtils.copyProperties(productInfo, orderDetail);
                    orderDetail.setOrderId(orderId);
                    orderDetail.setDetailId(detailId);
                    // 订单详情入库
                    orderDetailRepository.save(orderDetail);
                }
            });
        });

        // 扣库存
        List<DecreaseStockInput> decreaseStockInputList = orderDto.getOrderDetailList().stream()
                .map(e -> new DecreaseStockInput(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productClient.decreaseStock(decreaseStockInputList);

        // 订单入库
        OrderMaster orderMaster = new OrderMaster();
        orderDto.setOrderId(orderId);
        orderDto.setBuyerOpenid(orderDto.getBuyerOpenid());
        BeanUtils.copyProperties(orderDto, orderMaster);
        // 总价
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());

        orderMasterRepository.save(orderMaster);
        return orderDto;
    }
}
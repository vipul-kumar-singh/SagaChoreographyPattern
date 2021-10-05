package com.vkstech.saga.order.service;

import com.vkstech.saga.commons.dto.OrderRequestDto;
import com.vkstech.saga.commons.event.OrderStatus;
import com.vkstech.saga.order.entity.PurchaseOrder;
import com.vkstech.saga.order.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private PurchaseOrderRepository orderRepository;

    @Autowired
    private OrderStatusPublisher orderStatusPublisher;

    @Transactional
    public PurchaseOrder createOrder(OrderRequestDto orderRequestDto) {
        PurchaseOrder order = orderRepository.save(convertDtoToEntity(orderRequestDto));
        orderRequestDto.setOrderId(order.getId());

        //produce kafka event with status ORDER_CREATED
        orderStatusPublisher.publishOrderEvent(orderRequestDto, order.getOrderStatus());
        return order;
    }

    public List<PurchaseOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    private PurchaseOrder convertDtoToEntity(OrderRequestDto dto) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setProductId(dto.getProductId());
        purchaseOrder.setUserId(dto.getUserId());
        purchaseOrder.setOrderStatus(OrderStatus.ORDER_CREATED);
        purchaseOrder.setPrice(dto.getAmount());
        return purchaseOrder;
    }
}

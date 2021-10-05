package com.vkstech.saga.order.service;

import com.vkstech.saga.commons.dto.OrderRequestDto;
import com.vkstech.saga.commons.event.OrderEvent;
import com.vkstech.saga.commons.event.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

@Component
public class OrderStatusPublisher {

    @Autowired
    private Sinks.Many<OrderEvent> orderSinks;

    public void publishOrderEvent(OrderRequestDto orderRequestDto, OrderStatus orderStatus) {
        OrderEvent orderEvent = new OrderEvent(orderRequestDto, orderStatus);
        orderSinks.tryEmitNext(orderEvent);
    }
}

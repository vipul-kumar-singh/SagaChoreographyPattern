package com.vkstech.saga.payment.config;

import com.vkstech.saga.commons.event.OrderEvent;
import com.vkstech.saga.commons.event.OrderStatus;
import com.vkstech.saga.commons.event.PaymentEvent;
import com.vkstech.saga.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
public class PaymentConsumerConfig {

    @Autowired
    private PaymentService paymentService;

    @Bean
    public Function<Flux<OrderEvent>, Flux<PaymentEvent>> paymentProcessor() {
        return orderEventFlux -> orderEventFlux.flatMap(this::processPayment);
    }

    private Mono<PaymentEvent> processPayment(OrderEvent orderEvent) {
        if (orderEvent.getOrderStatus().equals(OrderStatus.ORDER_CREATED)) {
            return Mono.fromSupplier(() -> this.paymentService.newOrderEvent(orderEvent));
        } else {
            return Mono.fromRunnable(() -> this.paymentService.cancerOrderEvent(orderEvent));
        }
    }
}

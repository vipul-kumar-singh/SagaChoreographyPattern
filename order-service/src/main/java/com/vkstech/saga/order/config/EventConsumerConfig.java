package com.vkstech.saga.order.config;

import com.vkstech.saga.commons.event.PaymentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class EventConsumerConfig {

    @Autowired
    private OrderStatusUpdateHandler handler;

    @Bean
    public Consumer<PaymentEvent> paymentEventConsumer() {
        //listen payment-event topic
        //check payment status
        // if payment status completed -> complete the order
        // if payment status failed -> cancel the order

        return payment -> handler.updateOrder(payment.getPaymentRequestDto().getOrderId(),
                purchaseOrder -> purchaseOrder.setPaymentStatus(payment.getPaymentStatus()));
    }
}

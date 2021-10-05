package com.vkstech.saga.payment.service;

import com.vkstech.saga.commons.dto.OrderRequestDto;
import com.vkstech.saga.commons.dto.PaymentRequestDto;
import com.vkstech.saga.commons.event.OrderEvent;
import com.vkstech.saga.commons.event.PaymentEvent;
import com.vkstech.saga.commons.event.PaymentStatus;
import com.vkstech.saga.payment.entity.UserBalance;
import com.vkstech.saga.payment.entity.UserTransaction;
import com.vkstech.saga.payment.repository.UserBalanceRepository;
import com.vkstech.saga.payment.repository.UserTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PaymentService {

    @Autowired
    private UserBalanceRepository userBalanceRepository;

    @Autowired
    private UserTransactionRepository userTransactionRepository;

    public void initUserBalanceInDb() {
        userBalanceRepository.saveAll(Stream.of(
                new UserBalance(101, 5000),
                new UserBalance(102, 3000),
                new UserBalance(103, 4200),
                new UserBalance(104, 20000),
                new UserBalance(105, 699)
        ).collect(Collectors.toList()));
    }

    /**
     * get the user id
     * check the balance availabilit
     * if balance sufficient -> Payment completed and deduct amount from db
     * if balance not sufficient -> Cancel the order event and update the amount in DB
     */
    @Transactional
    public PaymentEvent newOrderEvent(OrderEvent orderEvent) {
        OrderRequestDto orderRequestDto = orderEvent.getOrderRequestDto();

        PaymentRequestDto paymentRequestDto = new PaymentRequestDto(orderRequestDto.getOrderId(),
                orderRequestDto.getUserId(), orderRequestDto.getAmount());

        return userBalanceRepository.findById(orderRequestDto.getUserId())
                .filter(ub -> ub.getPrice() > orderRequestDto.getAmount())
                .map(ub -> {
                    ub.setPrice(ub.getPrice() - orderRequestDto.getAmount());
                    userTransactionRepository.save(new UserTransaction(orderRequestDto.getOrderId(),
                            orderRequestDto.getUserId(), orderRequestDto.getAmount()));
                    return new PaymentEvent(paymentRequestDto, PaymentStatus.PAYMENT_COMPLETED);
                }).orElse(new PaymentEvent(paymentRequestDto, PaymentStatus.PAYMENT_FAILED));
    }

    @Transactional
    public void cancerOrderEvent(OrderEvent orderEvent) {
        userTransactionRepository.findById(orderEvent.getOrderRequestDto().getOrderId())
                .ifPresent(ut -> {
                    userTransactionRepository.delete(ut);
                    userBalanceRepository.findById(ut.getUserId())
                            .ifPresent(ub -> ub.setPrice(ub.getPrice() + ut.getAmount()));
                });
    }
}

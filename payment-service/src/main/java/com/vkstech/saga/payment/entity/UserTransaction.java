package com.vkstech.saga.payment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTransaction {

    @Id
    private Integer orderId;
    private Integer userId;
    private Integer amount;
}

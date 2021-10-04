package com.vkstech.saga.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {

    private Integer userId;
    private Integer productId;
    private Integer amount;
    private Integer orderId;
}

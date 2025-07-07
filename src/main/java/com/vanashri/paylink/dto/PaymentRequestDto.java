package com.vanashri.paylink.dto;

import com.vanashri.paylink.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor

public class PaymentRequestDto {
//    private String paymentId;
    private String payerName;
    private Double amount;

    private PaymentStatus status;
    private LocalDateTime paymentDate;



}

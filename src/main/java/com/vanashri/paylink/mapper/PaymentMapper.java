package com.vanashri.paylink.mapper;

import com.vanashri.paylink.dto.PaymentRequestDto;
import com.vanashri.paylink.dto.PaymentResponseDto;
import com.vanashri.paylink.entity.Payment;

import java.time.LocalDateTime;
import java.util.List;

public class PaymentMapper {
    // MAP BODY(DTO) TO ENTITY

    public static Payment mapDtoToEntity(PaymentRequestDto paymentRequestDto){
        Payment payment = Payment.builder()
                .payerName(paymentRequestDto.getPayerName())
                .amount(paymentRequestDto.getAmount())
                .paymentDate(LocalDateTime.now())
                .status(paymentRequestDto.getStatus())
                .build();

        return payment;
    }

    // MAP ENTITY TO BODY TO RETURN DATA TO USER
    public static PaymentResponseDto mapToBody(Payment payment) {
        PaymentResponseDto paymentResponseDto = PaymentResponseDto.builder()
                .id(payment.getId())
                .payerName(payment.getPayerName())
                .paymentId(payment.getPaymentId())
                .amount(payment.getAmount())
                .paymentDate(LocalDateTime.now())
                .status(payment.getStatus())
                .build();

        return paymentResponseDto;
    }



}

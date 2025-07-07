package com.vanashri.paylink.dto;

import com.vanashri.paylink.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponseDto {

    private long id;
    private String paymentId;
    private String payerName;
    private Double amount;

    private PaymentStatus status;
    private LocalDateTime paymentDate;

}

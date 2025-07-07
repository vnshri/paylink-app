package com.vanashri.paylink.dto;

import com.vanashri.paylink.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentStatusSummaryDto {
    private PaymentStatus status;
    private Double totalAmount;
    private String message;


}

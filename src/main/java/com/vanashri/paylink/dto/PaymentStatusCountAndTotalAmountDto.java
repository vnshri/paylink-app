package com.vanashri.paylink.dto;

import com.vanashri.paylink.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentStatusCountAndTotalAmountDto {
private String status;
private long count;
private double totalAmount;

    public PaymentStatusCountAndTotalAmountDto(String status, Long count, Double totalAmount) {
        this.status=status;
        this.count=count;
        this.totalAmount=totalAmount;

    }
}

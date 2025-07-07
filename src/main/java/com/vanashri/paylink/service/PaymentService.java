package com.vanashri.paylink.service;

import ch.qos.logback.core.util.Loader;
import com.vanashri.paylink.dto.PaymentRequestDto;
import com.vanashri.paylink.dto.PaymentResponseDto;
import com.vanashri.paylink.dto.PaymentStatusCountAndTotalAmountDto;
import com.vanashri.paylink.dto.PaymentStatusSummaryDto;
import com.vanashri.paylink.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public interface PaymentService {
    //    create payment in db
    public void addPayment(PaymentRequestDto paymentRequestDto);

    // GET DATA
    public List<PaymentResponseDto> getAllRecords();

    // Update by Id
    public PaymentResponseDto updateRecord(String paymentId, PaymentRequestDto paymentRequestDto);

    // Delete by Id
    public void deleteRecord(long id);

// GET BY ID
    public PaymentResponseDto getById(long id);

    // Count Num of Payments

    public long countNumberOfPayments();

    // count by status
    public Map<String, Long> getPaymentCountByStatus();

    // Sum of amount where status is success
public PaymentStatusSummaryDto sumOfAmountStatusSuccess();

// Status+count+Amount
    public List<PaymentStatusCountAndTotalAmountDto> statusCountAndTotalAmount();

// find by name
    public List<PaymentResponseDto> findByName(String payerName);

    // find by status
    public List<PaymentResponseDto> getByStatus(String status);

// find by payment satrt dateTime + EndDateTime
    public List<PaymentResponseDto> getByDate(LocalDateTime startDate, LocalDateTime endDate);

    // find by paymnet only date
    public List<PaymentResponseDto> getByDateOnly(LocalDate startDate, LocalDate endDate);

    // pagination and sort by date
    public List<PaymentResponseDto> pagintionSortByDate(LocalDate start, LocalDate end, Pageable pageable);

    // pagination + sort by name
    public List<PaymentResponseDto> paginationByPayerName(String payerName, Pageable pageable);

    // pagination+ sort by ID
    public Page<PaymentResponseDto> paginationSortByID(Pageable pageable);

    // search by keyword
    public List<PaymentResponseDto> searchByKeyword(String keyword);

    // filter
    public Page<PaymentResponseDto> filterPayment(String keyword, LocalDate start, LocalDate end, Pageable pageable);

}

















package com.vanashri.paylink.dao;

import com.vanashri.paylink.dto.PaymentResponseDto;
import com.vanashri.paylink.dto.PaymentStatusSummaryDto;
import com.vanashri.paylink.entity.Payment;
import com.vanashri.paylink.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

// Check for duplicatepaymentid

    public boolean existsByPaymentId(String paymentId);

    public Optional<Payment> findByPaymentId(String paymentId);

    // Count Status+ NUmber of Payments
    @Query("SELECT p.status, COUNT(p) FROM Payment p GROUP BY(p.status)")
    public List<Object[]> countByStatus();

    // Sum of paid amount
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'SUCCESS'")
    public Double sumOfAmountAndStatusIsSuccess();


    // Sum of amount by passing status
    @Query("SELECT p.status, COUNT(p), SUM(p.amount) FROM Payment p GROUP BY p.status")
    public List<Object[]> countAndSumOfAmount();

    // Find BY name
    public List<Payment> findByPayerNameContainingIgnoreCase(String payerName);

    // Filter by status
    public List<Payment> findByStatus(PaymentStatus status);

    // filter by payment date start+end+time
    @Query("SELECT p FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate")
    public List<Payment> findByPaymentDate(LocalDateTime startDate, LocalDateTime endDate);

    // filter by payment date
    @Query("SELECT p FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate")
    public List<Payment> findByPaymentDateOnly(LocalDateTime startDate, LocalDateTime endDate);

    // Pagination + Filter by date (Start+end)
    public List<Payment> findAllByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // Pagination + sort by name
    public List<Payment> findByPayerNameContainingIgnoreCase(String payerName, Pageable pageable);

    // Pagination+ sort by Id
    public Page<Payment> findAll(Pageable pageable);

    // Search by keyword in name OR status OR paymentId
    @Query("SELECT p FROM Payment p " +
            "WHERE LOWER(p.payerName) LIKE %:keyword% " +
            "OR LOWER(p.paymentId) LIKE %:keyword% " +
            "OR LOWER(p.status) LIKE %:keyword% ")
    public List<Payment> searchByKeyword(@PathVariable("keyword") String keyword);

    // Search by keyword (payer name, payment ID, status, etc.)
    @Query("SELECT p FROM Payment p " +
            "WHERE (LOWER(p.payerName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.paymentId) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.status) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "OR p.paymentDate BETWEEN :startDate AND :endDate")
    Page<Payment> searchByKeywordAndDate(@PathVariable("keyword") String keyword,
                                         @PathVariable("startDate") LocalDateTime startDate,
                                         @PathVariable("endDate") LocalDateTime endDate, Pageable pageable);
}
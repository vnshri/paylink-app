package com.vanashri.paylink.serviceImpl;

import com.vanashri.paylink.dao.PaymentRepository;
import com.vanashri.paylink.dto.PaymentRequestDto;
import com.vanashri.paylink.dto.PaymentResponseDto;
import com.vanashri.paylink.dto.PaymentStatusCountAndTotalAmountDto;
import com.vanashri.paylink.dto.PaymentStatusSummaryDto;
import com.vanashri.paylink.entity.Payment;
import com.vanashri.paylink.enums.PaymentStatus;
import com.vanashri.paylink.exception.*;
import com.vanashri.paylink.mapper.PaymentMapper;
import com.vanashri.paylink.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    // Making Repo field to use
    @Autowired
    private PaymentRepository paymentRepository;


    /**
     * add payment in DB
     *
     * @param paymentRequestDto
     */
    @Override
    public void addPayment(PaymentRequestDto paymentRequestDto) {
        log.info("Adding data with payer name {} and amount {}", paymentRequestDto.getPayerName(), paymentRequestDto.getAmount());

        Payment payment = PaymentMapper.mapDtoToEntity(paymentRequestDto);

        // ✅ Auto-generate paymentId
        String paymentId = "TXN" + System.currentTimeMillis();
        payment.setPaymentId(paymentId);

        paymentRepository.save(payment);
    }

    /**
     * get all records
     *
     * @return
     */
    @Override
    public List<PaymentResponseDto> getAllRecords() {
        log.info("Fetch all records");
        List<Payment> payment = paymentRepository.findAll();
        if (payment.isEmpty()) {
            throw new ResourceNotFoundException("No payment records found.");
        }
        return payment.stream()
                .map(PaymentMapper::mapToBody).collect(Collectors.toList());


    }


    // UPDATE BY ID

    /**
     * @param paymentId
     * @param paymentRequestDto
     * @return
     */
    @Override
    public PaymentResponseDto updateRecord(String paymentId, PaymentRequestDto paymentRequestDto) {
        log.info("Updating record of payment Id: ", paymentId);
        Payment payment = paymentRepository.findByPaymentId(paymentId).orElseThrow(() -> new PaymentIdNotFoundException("No match id found with " + paymentId));

        payment.setAmount(paymentRequestDto.getAmount());
        payment.setPayerName(paymentRequestDto.getPayerName());
        payment.setStatus(paymentRequestDto.getStatus());
        payment.setPaymentDate(LocalDateTime.now());

        Payment updatedData = paymentRepository.save(payment);
        log.info("Payment updated successfully for ID: {}", paymentId);

        return PaymentMapper.mapToBody(updatedData);
    }


    /**
     * Delete by Id
     * @param id
     * @return
     */
    @Override
    public void deleteRecord(long id) {
       log.info("delete record of id {}: ", id);
       Payment payment= paymentRepository.findById(id).orElseThrow(() -> new IdNotFoundException("No MAtch found with ID: " + id));
paymentRepository.delete(payment);


    }

    // GET BY ID
    /**
     * @param id
     * @return
     */
    @Override
    public PaymentResponseDto getById(long id) {
        log.info("Get Record By ID{} ",id);
        Payment payment=paymentRepository.findById(id).orElseThrow(() -> new IdNotFoundException("No Match found with Id: "+id));
      return  PaymentMapper.mapToBody(payment);
    }

    /**
     * @return
     */
    @Override
    public long countNumberOfPayments() {
log.info("count of payments");
    long payment= paymentRepository.count();
    return payment;

    }

    /**
     * @return
     */
    @Override
    public Map<String, Long> getPaymentCountByStatus() {
List<Object[]> list=paymentRepository.countByStatus();

Map<String, Long> result=new HashMap<>();
for(Object[] row: list){
    String status= row[0].toString();
    Long count= (Long) row[1];
    result.put(status, count);
}
return result;

    }

    /**
     * get sum of amount where status is success
     * @return
     */
    @Override
    public PaymentStatusSummaryDto sumOfAmountStatusSuccess() {
        Double payment=paymentRepository.sumOfAmountAndStatusIsSuccess();
        log.info("sum of amount where status is success", payment);
 String message="Total amount for SUCCESS status: ₹"+ payment;
 return new PaymentStatusSummaryDto(PaymentStatus.SUCCESS, payment, message);

     }

    /**
     * @return
     */
    @Override
    public List<PaymentStatusCountAndTotalAmountDto> statusCountAndTotalAmount() {
        log.info("Count number of payments according to status ");
        List<Object[]> paymentStatusCountAndTotalAmountDtos = paymentRepository.countAndSumOfAmount();
return paymentStatusCountAndTotalAmountDtos.stream()
        .map(obj -> new PaymentStatusCountAndTotalAmountDto(
                obj[0].toString(),          // status
                (Long) obj[1],              // count
                (Double) obj[2]             // totalAmount
        ))
        .collect(Collectors.toList());
    }

    /**
     * Get by PayerName
     * @param payerName
     * @return
     */
    @Override
    public List<PaymentResponseDto> findByName(String payerName) {
log.info("Get Data by PayerName {} ", payerName);
List<Payment> payment = paymentRepository.findByPayerNameContainingIgnoreCase(payerName);

if(payment.isEmpty()){
    throw new PaymentIdNotFoundException("No User Found With Name: "+payerName);
}

return payment.stream().map(PaymentMapper::mapToBody).collect(Collectors.toList());
    }

    /**
     * find by status
     * @param status
     * @return
     */
    @Override
    public List<PaymentResponseDto> getByStatus(String status) {
log.info("filter by status {} ", status);
// create enum variable to convert enum to string
PaymentStatus paymentStatus;
        try {
            paymentStatus = PaymentStatus.valueOf(status.toUpperCase());
        } catch (StatusNotFound ex) {
            throw new StatusNotFound("Invalid Status: " + status);
        }
List<Payment> payment=paymentRepository.findByStatus(paymentStatus);
return payment.stream().map(PaymentMapper::mapToBody).collect(Collectors.toList());

    }

    /**
     * fetch by paymentDate
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public List<PaymentResponseDto> getByDate(LocalDateTime startDate, LocalDateTime endDate) {

        log.info("Get data by payment StartDate{} : ", startDate," And EndDate{}: ", endDate);
        List<Payment> payment=paymentRepository.findByPaymentDate(startDate, endDate);
        if(payment.isEmpty()){
            throw new PaymentDateNotFoundException("No payment found in range "+startDate+" And "+endDate);
        }
        return payment.stream().map(PaymentMapper::mapToBody).collect(Collectors.toList());

    }

    /**
     * Find by Payment Date Only
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public List<PaymentResponseDto> getByDateOnly(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay(); // 00:00:00
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59); // 23:59:59

        log.info("get payment between {}:  AND {}: ", startDate, endDate);
    List<Payment> payments=paymentRepository.findByPaymentDateOnly(startDateTime, endDateTime);
    if(payments.isEmpty()){
        throw new PaymentDateNotFoundException("No Payment Found Between: "+startDate+" AND "+endDate);
    }
    return payments.stream().map(PaymentMapper::mapToBody).collect(Collectors.toList());

    }

    /**
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    @Override
    public List<PaymentResponseDto> pagintionSortByDate(LocalDate start, LocalDate end, Pageable pageable) {
        log.info("pagination by start date {} and end date {} ", start, end);
        LocalDateTime startDateTime=start.atStartOfDay();
        LocalDateTime endDateTime=end.atTime(23,59,59);

        List<Payment> paymentResponseDtos=paymentRepository.findAllByPaymentDateBetween(startDateTime, endDateTime, pageable);

        return paymentResponseDtos.stream().map(PaymentMapper::mapToBody).collect(Collectors.toList());
    }

    /**
     * pagination+ sort by name
     * @param payerName
     * @param pageable
     * @return
     */
    @Override
    public List<PaymentResponseDto> paginationByPayerName(String payerName, Pageable pageable) {
log.info("Pagination and sort by Payername{} ", payerName);
List<Payment> paymentResponseDtos=paymentRepository.findByPayerNameContainingIgnoreCase(payerName, pageable);
if(paymentResponseDtos.isEmpty()){
    throw new PayerNameNotFound("No Payment found with name :"+payerName);
}

return paymentResponseDtos.stream().map(PaymentMapper::mapToBody).collect(Collectors.toList());
    }

    /**
     * Pagination and sort by ID
     * @param pageable
     * @return
     */
    @Override
    public Page<PaymentResponseDto> paginationSortByID(Pageable pageable) {
log.info("Pagination and sort by Id");
Page<Payment> payments=paymentRepository.findAll(pageable);
if(payments.isEmpty()){
    throw new ResourceNotFoundException("No Data Found");
}
return payments.map(PaymentMapper::mapToBody);

    }

    /**
     * search by keyword
     * @param keyword
     * @return
     */
    @Override
    public List<PaymentResponseDto> searchByKeyword(String keyword) {
log.info("search by keyword{}: ", keyword);
List<Payment> payments=paymentRepository.searchByKeyword(keyword);
if(payments.isEmpty()){
    throw new KeywordNotFoundException("No Match Found With: "+keyword);
}
return payments.stream().map(PaymentMapper::mapToBody).collect(Collectors.toList());
    }

    /**
     * @param keyword
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    @Override
    public Page<PaymentResponseDto> filterPayment(String keyword, LocalDate start, LocalDate end, Pageable pageable) {
log.info("filter by either keyword{} OR Date{} ", keyword, start, end);
LocalDateTime startDateTime=start.atStartOfDay();
LocalDateTime endDateTime=end.atTime(23,59,59);

Page<Payment> payments=paymentRepository.searchByKeywordAndDate(keyword, startDateTime,endDateTime, pageable);
return payments.map(PaymentMapper::mapToBody);
    }


}




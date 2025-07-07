package com.vanashri.paylink.controller;

import com.vanashri.paylink.dto.PaymentRequestDto;
import com.vanashri.paylink.dto.PaymentResponseDto;
import com.vanashri.paylink.dto.PaymentStatusCountAndTotalAmountDto;
import com.vanashri.paylink.dto.PaymentStatusSummaryDto;
import com.vanashri.paylink.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@RestController
@Slf4j
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    // Created payment
    @PostMapping("/add")

    public ResponseEntity<String> createPayment(@RequestBody PaymentRequestDto paymentRequestDto){
        log.info("Create Payment with user name {}:", paymentRequestDto.getPayerName(), "and amount {}: ",paymentRequestDto.getAmount());
        paymentService.addPayment(paymentRequestDto);
String message="Payment added with Payer Name: "+ paymentRequestDto.getPayerName()+ "with amount: "+paymentRequestDto.getAmount() ;
//Or
//        String msg=String.format("payment added with ID{}: ", paymentRequestDto.getPaymentId());
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // GET DATA

    @GetMapping("/all")
    public ResponseEntity<List<PaymentResponseDto>> getAllPaymentRecord(){
        log.info("Get All records");
        List<PaymentResponseDto> paymentResponseDtoList=paymentService.getAllRecords();
        return new ResponseEntity<>(paymentResponseDtoList, HttpStatus.OK);

    }

// UpDATE BY ID
    @PutMapping("/update/{paymentId}")
    public ResponseEntity<PaymentResponseDto> updateRecord(@PathVariable String paymentId, @RequestBody PaymentRequestDto paymentRequestDto){
        log.info("update record by payment id: {} ", paymentId);

        PaymentResponseDto paymentResponseDto = paymentService.updateRecord(paymentId, paymentRequestDto);
         return new ResponseEntity<>(paymentResponseDto, HttpStatus.OK);

    }
//DELETE BY ID -SOFT DELETE

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteData(@PathVariable long id){
        log.info("Dalete record by id {}", id);
        paymentService.deleteRecord(id);
        String message="Deleted the record with Id: "+id;
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

@GetMapping("/get-by-id/{id}")
    public ResponseEntity<PaymentResponseDto> getByID(@PathVariable long id){
        log.info("Get By Id {}: ", id);
PaymentResponseDto payment=paymentService.getById(id);
return new ResponseEntity<>(payment, HttpStatus.OK);
}

// Count Payments
    @GetMapping("/count/payments")
    public ResponseEntity<Long> countNumbers(){
        log.info("count number of payments");
        Long payments= paymentService.countNumberOfPayments();
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    // Count By Status
    @GetMapping("count-by/status")
    public ResponseEntity<Map<String, Long>> countByStatus(){
        log.info("Count By Status {} : ");
        Map<String, Long> list = paymentService.getPaymentCountByStatus();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


// Total Amount by status success
    @GetMapping("/amount/status/success")
    public ResponseEntity<PaymentStatusSummaryDto> sumAmountByStatusSuccess(){
        log.info("sum of amount by status is success");
        PaymentStatusSummaryDto paymentStatusSummaryDto=paymentService.sumOfAmountStatusSuccess();
        return new ResponseEntity<>(paymentStatusSummaryDto, HttpStatus.OK);
    }

//    Status+Count+Amount
    @GetMapping("/status/count/totalamount")
    public ResponseEntity<List<PaymentStatusCountAndTotalAmountDto>> statusCountAmount(){
        log.info("Count Total count of status and its total amount");
List<PaymentStatusCountAndTotalAmountDto> paymentStatusCountAndTotalAmountDtos=paymentService.statusCountAndTotalAmount();
        return new ResponseEntity<>(paymentStatusCountAndTotalAmountDtos, HttpStatus.OK);
    }

    // Get by payer name
    @GetMapping("/by/payername/{payerName}")
    public ResponseEntity<List<PaymentResponseDto>> fetchBYPayerName(@PathVariable String payerName){
        log.info("Get data by payer name {} ", payerName);
List<PaymentResponseDto> paymentResponseDtos=paymentService.findByName(payerName);
return new ResponseEntity<>(paymentResponseDtos,HttpStatus.OK);

    }

    // Find by Status
    @GetMapping("filter/by/status/{status}")
    public ResponseEntity<List<PaymentResponseDto>> fetchByStatus(@PathVariable String status){
        log.info("filter by status {} ", status);
        List<PaymentResponseDto> paymentResponseDtos = paymentService.getByStatus(status);
        return new ResponseEntity<>(paymentResponseDtos, HttpStatus.OK);
    }

    // find by payment Start date+ End DAte + time
    @GetMapping("filter/by/paymentDate/{startDate}/{endDate}")
    public ResponseEntity<List<PaymentResponseDto>> fetchByPaymentDate(
            @PathVariable("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
@PathVariable("endDate") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) LocalDateTime ebdDate){

        List<PaymentResponseDto> paymentResponseDtos=paymentService.getByDate(startDate, ebdDate);

        return new ResponseEntity<>(paymentResponseDtos, HttpStatus.OK);
    }

    // find by Paymnet data
    @GetMapping("/filter/by/{startDate}/{endDate}")
    public ResponseEntity<List<PaymentResponseDto>> fetchByPaymentDate(
            @PathVariable("startDate") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDate startDate,
            @PathVariable("endDate") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)LocalDate endDate)
            {

                log.info("Get PAyment Records Using Start-Date{} ", startDate," And Enda ADte{} : ", endDate);

                List<PaymentResponseDto> paymentResponseDtos=paymentService.getByDateOnly(startDate, endDate);
                return new ResponseEntity<>(paymentResponseDtos, HttpStatus.OK);

    }

// PAgination+ date
    @GetMapping("/pagination/{startDate}/{endDate}")
    public ResponseEntity<List<PaymentResponseDto>> paginationAndSortByDate(
        @PathVariable("startDate") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate startDate,
        @PathVariable("endDate") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "paymentDate,desc") String[] sort)

    {

    log.info("PAgination by startDate{}: And EndDAte{} ", startDate, endDate);
Pageable pageable=PageRequest.of(page, size, Sort.by(Sort.Order.asc("paymentDate")));
        List<PaymentResponseDto> paymentResponseDtos=paymentService.pagintionSortByDate(startDate, endDate, pageable);
        return new ResponseEntity<>(paymentResponseDtos, HttpStatus.OK);
    }

// PAgination + Sort by payername
    @GetMapping("/pagination/{payerName}")
    public ResponseEntity<List<PaymentResponseDto>> paginationFetchByName(
            @PathVariable("payerName") String payerName,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "payerName,asc") String[] sort){

        log.info("get by payername{}: ", payerName);
        Pageable pageable=PageRequest.of(start, size,Sort.by(Sort.Order.asc("payerName")));

        List<PaymentResponseDto> paymentResponseDtos=paymentService.paginationByPayerName(payerName, pageable);

        return new ResponseEntity<>(paymentResponseDtos, HttpStatus.OK);
    }

// PAgination + sort by Id

    @GetMapping("/pagination/by/id")
    public ResponseEntity<Page<PaymentResponseDto>> pagintaionById(
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id, asc") String sort
    ){
        log.info("pagination by id");
        Pageable pageable=PageRequest.of(start, size, Sort.by(Sort.Order.asc("id")));
//        org.hibernate.query.Page<PaymentResponseDto> paymentResponseDtos=paymentService.paginationSortByID(pageable);
//        return new ResponseEntity<>(paymentResponseDtos, HttpStatus.OK);

        Page<PaymentResponseDto> page = paymentService.paginationSortByID(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

// Search by keyword
    @GetMapping("/search/by/{keyword}")
    public ResponseEntity<List<PaymentResponseDto>> fetchBuKeyword(@PathVariable String keyword){
        log.info("SEarch by keyword{} ", keyword);
        List<PaymentResponseDto> paymentResponseDtoList=paymentService.searchByKeyword(keyword);
        return new ResponseEntity<>(paymentResponseDtoList, HttpStatus.OK);
    }

//    Search by name, status, sartdate, enddate,with pagination
    @GetMapping("/filter/{keyword}/{startDate}/{endDate}")
    public ResponseEntity<Page<PaymentResponseDto>> filterPayment(
            @PathVariable String keyword,
            @PathVariable("startDate") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate startDate,
            @PathVariable("endDate") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id, asc") String id
    ){
        log.info("Filter");
        Pageable pageable=PageRequest.of(start, size, Sort.by(Sort.Order.asc("id")));
        Page<PaymentResponseDto> paymentResponseDtos=paymentService.filterPayment(keyword,startDate,endDate, pageable);
        return new ResponseEntity<>(paymentResponseDtos, HttpStatus.OK);
    }










//    -----------------
}

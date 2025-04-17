package com.multi_vendo_ecom.ecommerce.multivendor.controller;

import com.multi_vendo_ecom.ecommerce.multivendor.model.*;
import com.multi_vendo_ecom.ecommerce.multivendor.response.ApiResponse;
import com.multi_vendo_ecom.ecommerce.multivendor.response.PaymentLinkResponse;
import com.multi_vendo_ecom.ecommerce.multivendor.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;
    private final SellerService sellerService;

    private final SellerReportService sellerReportService;
    private final TransactionService transactionService;

    public PaymentController(PaymentService paymentService, UserService userService, SellerService sellerService, SellerReportService sellerReportService, TransactionService transactionService) {
        this.paymentService = paymentService;
        this.userService = userService;
        this.sellerService = sellerService;
        this.sellerReportService = sellerReportService;
        this.transactionService = transactionService;
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse> paymentSuccessHandler(@PathVariable String paymentId, @RequestParam String paymentLinkId, @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);

        PaymentLinkResponse paymentResponse;
        PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentLinkId);
        boolean paymentSuccess = paymentService.proceedPaymentOrder(paymentOrder, paymentId,paymentLinkId);
        if(paymentSuccess){
            for(Order order:paymentOrder.getOrders()){
                transactionService.createTransaction(order);
                Seller seller = sellerService.getSellerById(order.getSellerId());
                SellerReport report = sellerReportService.getSellerReport(seller);
                report.setTotalOrders(report.getTotalOrders() + 1);
                report.setTotalEarnings(report.getTotalEarnings()+order.getTotalSellingPrice());
                report.setTotalSales(report.getTotalSales()+order.getOrderItems().size());
                sellerReportService.updateSellerReport(report);
            }
        }
        ApiResponse res = new ApiResponse();
        res.setMessage("Payment successful");
        res.setStatus(true);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
}

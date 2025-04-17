package com.multi_vendo_ecom.ecommerce.multivendor.controller;

import com.multi_vendo_ecom.ecommerce.multivendor.model.Seller;
import com.multi_vendo_ecom.ecommerce.multivendor.model.Transaction;
import com.multi_vendo_ecom.ecommerce.multivendor.service.SellerService;
import com.multi_vendo_ecom.ecommerce.multivendor.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final SellerService sellerService;


    public TransactionController(TransactionService transactionService, SellerService sellerService) {
        this.transactionService = transactionService;
        this.sellerService = sellerService;
    }

    @GetMapping("/seller")
    public ResponseEntity<List<Transaction>> getTransactionBySeller(@RequestHeader("Authorization") String jwt) throws Exception {
        Seller seller = sellerService.getSellerProfile(jwt);
        List<Transaction> transactions = transactionService.getTransactionsBySellerId(seller);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransaction(){
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }
}

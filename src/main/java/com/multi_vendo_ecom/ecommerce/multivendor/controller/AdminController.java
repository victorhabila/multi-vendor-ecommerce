package com.multi_vendo_ecom.ecommerce.multivendor.controller;

import com.multi_vendo_ecom.ecommerce.multivendor.domain.AccountStatus;
import com.multi_vendo_ecom.ecommerce.multivendor.exceptions.SellerException;
import com.multi_vendo_ecom.ecommerce.multivendor.model.Seller;
import com.multi_vendo_ecom.ecommerce.multivendor.service.SellerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AdminController {

    private final SellerService sellerService;

    public AdminController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @PatchMapping("/seller/{id}/status/{status}")
    public ResponseEntity<Seller> updateSellerStatus(
            @PathVariable Long id,
            @PathVariable AccountStatus status) throws Exception {
        Seller updatedSeller = sellerService.updateSellerAccountStatus(id, status);
        return ResponseEntity.ok(updatedSeller);

    }

}

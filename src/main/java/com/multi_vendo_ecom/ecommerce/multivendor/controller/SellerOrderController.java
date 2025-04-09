package com.multi_vendo_ecom.ecommerce.multivendor.controller;

import com.multi_vendo_ecom.ecommerce.multivendor.domain.OrderStatus;
import com.multi_vendo_ecom.ecommerce.multivendor.model.Order;
import com.multi_vendo_ecom.ecommerce.multivendor.model.Seller;
import com.multi_vendo_ecom.ecommerce.multivendor.service.OrderService;
import com.multi_vendo_ecom.ecommerce.multivendor.service.SellerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/seller/orders")
public class SellerOrderController {

    private final OrderService orderService;
    private final SellerService sellerService;

    public SellerOrderController(OrderService orderService, SellerService sellerService) {
        this.orderService = orderService;
        this.sellerService = sellerService;
    }

    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrdersHandler(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        Seller sellers = sellerService.getSellerProfile(jwt);
        List<Order> orders = orderService.sellersOrder(sellers.getId());

        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }


    @PatchMapping("/{orderId}/status/{orderStatus}")
    public ResponseEntity<Order> updateOrderHandler(
            @RequestHeader("Authorization") String Jwt,
            @PathVariable Long orderId,
            @PathVariable OrderStatus orderStatus
    ) throws Exception {

            Order orders = orderService.updateOrderStatus(orderId, orderStatus);
            return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }
}
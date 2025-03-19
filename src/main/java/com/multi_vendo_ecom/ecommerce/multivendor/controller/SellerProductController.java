package com.multi_vendo_ecom.ecommerce.multivendor.controller;

import com.multi_vendo_ecom.ecommerce.multivendor.exceptions.ProductException;
import com.multi_vendo_ecom.ecommerce.multivendor.model.Product;
import com.multi_vendo_ecom.ecommerce.multivendor.model.Seller;
import com.multi_vendo_ecom.ecommerce.multivendor.request.CreateProductRequest;
import com.multi_vendo_ecom.ecommerce.multivendor.service.ProductService;
import com.multi_vendo_ecom.ecommerce.multivendor.service.SellerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sellers/products")
public class SellerProductController {

    private final SellerService sellerService;
    private final ProductService productService;

    public SellerProductController(SellerService sellerService, ProductService productService) {
        this.sellerService = sellerService;
        this.productService = productService;
    }

    @GetMapping() //we want all the product of every seller
    public ResponseEntity<List<Product>> getProductBySellerId(
            @RequestHeader("Authorization") String jwt) throws Exception {
        Seller seller=sellerService.getSellerProfile(jwt); // we are accessing the seller from the token
        List<Product> products = productService.getProductBySellerId(seller.getId());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }


    @PostMapping()
    public ResponseEntity<Product> createProduct(
            @RequestBody CreateProductRequest request,
            @RequestHeader("Authorization")String jwt)
            throws Exception{

        Seller seller = sellerService.getSellerProfile(jwt);
        Product product = productService.createProduct(request, seller);

        return new ResponseEntity<>(product, HttpStatus.CREATED);

    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) throws ProductException {

            productService.deleteProduct(productId);
            return new ResponseEntity<>(HttpStatus.OK);


    }
}

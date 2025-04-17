package com.multi_vendo_ecom.ecommerce.multivendor.controller;

import com.multi_vendo_ecom.ecommerce.multivendor.exceptions.ProductException;
import com.multi_vendo_ecom.ecommerce.multivendor.model.Product;
import com.multi_vendo_ecom.ecommerce.multivendor.model.User;
import com.multi_vendo_ecom.ecommerce.multivendor.model.Wishlist;
import com.multi_vendo_ecom.ecommerce.multivendor.service.ProductService;
import com.multi_vendo_ecom.ecommerce.multivendor.service.UserService;
import com.multi_vendo_ecom.ecommerce.multivendor.service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;
    private final UserService userService;

    private final ProductService productService;

    public WishlistController(WishlistService wishlistService, UserService userService, ProductService productService) {
        this.wishlistService = wishlistService;
        this.userService = userService;
        this.productService = productService;
    }

//    public ResponseEntity<Wishlist> createWishlist(@RequestBody User user){
//        Wishlist wishlist = wishlistService.createWishlist(user);
//        return ResponseEntity
//    }

    @GetMapping()
    public ResponseEntity<Wishlist> getWishlistByUserId(
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        Wishlist wishlist = wishlistService.getWishlistByUserId(user);
        return ResponseEntity.ok(wishlist);

    }

    @PostMapping("/add-product/{productId}")
    public ResponseEntity<Wishlist> addProductToWishlist(
            @PathVariable Long productId,
            @RequestHeader("Authorization") String jwt) throws Exception {
            Product product =productService.findProductById(productId);
            User user = userService.findUserByJwtToken(jwt);
            Wishlist updatedWishlist = wishlistService.addProductToWishlist(user,product);
            return ResponseEntity.ok(updatedWishlist);
        }
}

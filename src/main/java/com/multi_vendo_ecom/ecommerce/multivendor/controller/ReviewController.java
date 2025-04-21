package com.multi_vendo_ecom.ecommerce.multivendor.controller;

import com.multi_vendo_ecom.ecommerce.multivendor.model.Product;
import com.multi_vendo_ecom.ecommerce.multivendor.model.Review;
import com.multi_vendo_ecom.ecommerce.multivendor.model.User;
import com.multi_vendo_ecom.ecommerce.multivendor.request.CreateReviewRequest;
import com.multi_vendo_ecom.ecommerce.multivendor.response.ApiResponse;
import com.multi_vendo_ecom.ecommerce.multivendor.service.ProductService;
import com.multi_vendo_ecom.ecommerce.multivendor.service.ReviewService;
import com.multi_vendo_ecom.ecommerce.multivendor.service.UserService;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductService productService;

    public ReviewController(ReviewService reviewService, UserService userService, ProductService productService) {
        this.reviewService = reviewService;
        this.userService = userService;
        this.productService = productService;
    }
    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<List<Review>> getReviewsByProductId(
            @PathVariable Long productId) {
        List<Review> reviews = reviewService.getReviewByProductId(productId);
        return ResponseEntity.ok(reviews);
    }
    @PostMapping("/products/{productId}/reviews")
    public ResponseEntity<Review> writeReview(
            @RequestBody CreateReviewRequest req,
            @PathVariable Long productId,
            @RequestHeader("Authorization") String jwt) throws Exception {
    User user = userService.findUserByJwtToken(jwt);
    Product product = productService. findProductById(productId);
    Review review = reviewService.createReview(req, user, product);
    return ResponseEntity.ok(review);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(
            @PathVariable Long reviewId,
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);

        reviewService.deleteReview(reviewId, user.getId());
        ApiResponse res = new ApiResponse();
        res.setMessage("Review deleted successfully");

        return ResponseEntity.ok(res);
    }

}

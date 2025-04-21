package com.multi_vendo_ecom.ecommerce.multivendor.service;

import com.multi_vendo_ecom.ecommerce.multivendor.model.Product;
import com.multi_vendo_ecom.ecommerce.multivendor.model.Review;
import com.multi_vendo_ecom.ecommerce.multivendor.model.User;
import com.multi_vendo_ecom.ecommerce.multivendor.request.CreateReviewRequest;

import java.util.List;

public interface ReviewService {

    Review createReview(CreateReviewRequest req, User user, Product product);
    List<Review> getReviewByProductId(Long productId);

    Review updateReview (Long reviewId, String reviewText, double rating, Long userId) throws Exception;

    void deleteReview(Long reviewId, Long userId) throws Exception;

    Review getReviewById(Long reviewId) throws Exception;
}

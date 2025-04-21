package com.multi_vendo_ecom.ecommerce.multivendor.service.impl;

import com.multi_vendo_ecom.ecommerce.multivendor.model.Product;
import com.multi_vendo_ecom.ecommerce.multivendor.model.Review;
import com.multi_vendo_ecom.ecommerce.multivendor.model.User;
import com.multi_vendo_ecom.ecommerce.multivendor.repository.ReviewRepository;
import com.multi_vendo_ecom.ecommerce.multivendor.request.CreateReviewRequest;
import com.multi_vendo_ecom.ecommerce.multivendor.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Review createReview(CreateReviewRequest req, User user, Product product) {

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setReviewText(req.getReviewText());
        req.setReviewRating(req.getReviewRating());
        review.setProductImages(req.getProductImages());

        product.getReviews().add(review);
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    public Review updateReview(Long reviewId, String reviewText, double rating, Long userId) throws Exception {
       Review review = getReviewById(reviewId);
       if(review.getUser().getId().equals(userId)){
           review.setReviewText(reviewText);
           review.setRating(rating);
           return reviewRepository.save(review);

       }
        throw new Exception("You can't update this review");

    }

    @Override
    public void deleteReview(Long reviewId, Long userId) throws Exception {
        Review review = getReviewById(reviewId);
        if(review.getUser().getId().equals(userId)){
            throw new Exception("you can't delete this review");
        }
        reviewRepository.delete(review);
    }

    @Override
    public Review getReviewById(Long reviewId) throws Exception {
        return reviewRepository.findById(reviewId).orElseThrow(()-> new  Exception("Review not found"));
    }
}

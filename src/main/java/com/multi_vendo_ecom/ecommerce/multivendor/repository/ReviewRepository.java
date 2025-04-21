package com.multi_vendo_ecom.ecommerce.multivendor.repository;

import com.multi_vendo_ecom.ecommerce.multivendor.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductId(Long productId);
}

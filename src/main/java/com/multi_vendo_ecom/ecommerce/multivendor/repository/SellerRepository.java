package com.multi_vendo_ecom.ecommerce.multivendor.repository;

import com.multi_vendo_ecom.ecommerce.multivendor.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Seller findByEmail(String email);
}

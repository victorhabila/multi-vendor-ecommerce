package com.multi_vendo_ecom.ecommerce.multivendor.repository;

import com.multi_vendo_ecom.ecommerce.multivendor.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Coupon findByCode(String code);
}

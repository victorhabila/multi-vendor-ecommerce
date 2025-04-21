package com.multi_vendo_ecom.ecommerce.multivendor.service;

import com.multi_vendo_ecom.ecommerce.multivendor.model.Cart;
import com.multi_vendo_ecom.ecommerce.multivendor.model.Coupon;
import com.multi_vendo_ecom.ecommerce.multivendor.model.User;

import java.util.List;

public interface CouponService {

    Cart applyCoupon(String code, double orderValue, User user) throws Exception;
    Cart removeCoupon(String code, User user) throws Exception;
    Coupon findCouponById(Long id) throws Exception;
    Coupon createCoupon(Coupon coupon);
    List<Coupon> findAllCoupons();
    void deleteCoupon(Long id) throws Exception;
}

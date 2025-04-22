package com.multi_vendo_ecom.ecommerce.multivendor.service.impl;

import com.multi_vendo_ecom.ecommerce.multivendor.model.Cart;
import com.multi_vendo_ecom.ecommerce.multivendor.model.Coupon;
import com.multi_vendo_ecom.ecommerce.multivendor.model.User;
import com.multi_vendo_ecom.ecommerce.multivendor.repository.CartRepository;
import com.multi_vendo_ecom.ecommerce.multivendor.repository.CouponRepository;
import com.multi_vendo_ecom.ecommerce.multivendor.repository.UserRepository;
import com.multi_vendo_ecom.ecommerce.multivendor.service.CouponService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public CouponServiceImpl(CouponRepository couponRepository, CartRepository cartRepository, UserRepository userRepository) {
        this.couponRepository = couponRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Cart applyCoupon(String code, double orderValue, User user) throws Exception {
        Coupon coupon = couponRepository.findByCode(code); // find coupon using code
        Cart cart = cartRepository.findByUserId(user.getId()); // find cart using user
        if(coupon ==null){
            throw new Exception("coupon not valid");
        }
        if(user.getUsedCoupons().contains(coupon)){
            throw new Exception("coupon already used");
        }
        if(orderValue < coupon.getMinimumOrderValue()){
            throw new Exception("Only valid for minimum order value" + coupon.getMinimumOrderValue());
        }

        if(coupon.isActive() && LocalDate.now().isAfter(coupon.getValidityStartDate()) && LocalDate.now().isBefore(coupon.getValidityEndDate()))
        {
            user.getUsedCoupons().add(coupon);
            userRepository.save(user);

            //update our user cart

            double discountedPrice = (cart.getTotalSellingPrice() * coupon.getDiscountPercentage())/100;
            cart.setTotalSellingPrice(cart.getTotalSellingPrice() - discountedPrice); // subtract the prize after coupon is added
            cart.setCouponCode(code);
            cartRepository.save(cart);

            return cart;
        }
       throw new Exception("coupon not valid");
    }

    @Override
    public Cart removeCoupon(String code, User user) throws Exception {
        Coupon coupon = couponRepository.findByCode(code);
        if(coupon ==null){
            throw new Exception("coupon not found...");
        }
        Cart cart = cartRepository.findByUserId(user.getId()); // find cart using user
        double discountedPrice = (cart.getTotalSellingPrice() * coupon.getDiscountPercentage())/100;
        cart.setTotalSellingPrice(cart.getTotalSellingPrice() + discountedPrice);// add the prize after coupon is removed
        cart.setCouponCode(null);
        return cartRepository.save(cart);
    }

    @Override
    public Coupon findCouponById(Long id) throws Exception {
        return couponRepository.findById(id).orElseThrow(()-> new Exception("Coupon not found"));
    }

    @Override
    @PreAuthorize("hasRole ('ADMIN')")
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public List<Coupon> findAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole ('ADMIN')")
    public void deleteCoupon(Long id) throws Exception {
        findCouponById(id);
        couponRepository.deleteById(id);
    }
}

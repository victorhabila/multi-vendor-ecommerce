package com.multi_vendo_ecom.ecommerce.multivendor.service.impl;

import com.multi_vendo_ecom.ecommerce.multivendor.domain.USER_ROLE;
import com.multi_vendo_ecom.ecommerce.multivendor.model.Seller;
import com.multi_vendo_ecom.ecommerce.multivendor.model.User;
import com.multi_vendo_ecom.ecommerce.multivendor.repository.SellerRepository;
import com.multi_vendo_ecom.ecommerce.multivendor.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserServiceImpl implements UserDetailsService {
    //THIS CLASS IS MEANT TO RESTRICT DEFAULT PASSWORD GENERATION WHEN WE RUN OUR APPLICATION

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;

    private static final String SELLER_PREFIX="seller_";

    public CustomUserServiceImpl(UserRepository userRepository, SellerRepository sellerRepository) {
        this.userRepository = userRepository;
        this.sellerRepository = sellerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.startsWith(SELLER_PREFIX)){

            String actualUsername = username.substring(SELLER_PREFIX.length());
            Seller seller = sellerRepository.findByEmail(actualUsername);

            if(seller != null){
                return buildUserDetails(seller.getEmail(), seller.getPassword(), seller.getRole());
            }

        }else{
            User user = userRepository.findByEmail(username);
            if(user != null){
                return buildUserDetails(user.getEmail(), user.getPassword(), user.getRole());
            }
        }
        throw new UsernameNotFoundException("User or seller not found with email -" + username);
    }

    private UserDetails buildUserDetails(String email, String password, USER_ROLE role) {
        if(role==null) role = USER_ROLE.ROLE_CUSTOMER;
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_"+ role));
        return new org.springframework.security.core.userdetails.User(
                email,
                password,
                authorityList
        );
    }
}

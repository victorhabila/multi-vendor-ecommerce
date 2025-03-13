package com.multi_vendo_ecom.ecommerce.multivendor.service.impl;

import com.multi_vendo_ecom.ecommerce.multivendor.config.JwtProvider;
import com.multi_vendo_ecom.ecommerce.multivendor.domain.USER_ROLE;
import com.multi_vendo_ecom.ecommerce.multivendor.model.Cart;
import com.multi_vendo_ecom.ecommerce.multivendor.model.User;
import com.multi_vendo_ecom.ecommerce.multivendor.repository.CartRepository;
import com.multi_vendo_ecom.ecommerce.multivendor.repository.UserRepository;
import com.multi_vendo_ecom.ecommerce.multivendor.response.SignupRequest;
import com.multi_vendo_ecom.ecommerce.multivendor.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;

    // Manually define the constructor
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, CartRepository cartRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartRepository = cartRepository;
        this.jwtProvider = jwtProvider;
    }


    @Override
    public String createUser(SignupRequest req) {
      User user = userRepository.findByEmail(req.getEmail());

      if(user == null){
          User createdUser = new User();
          createdUser.setEmail(req.getEmail());
          createdUser.setFullName(req.getFullName());
          createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
          createdUser.setMobile("090987655443");
          createdUser.setPassword(passwordEncoder.encode(req.getOtp()));

          user = userRepository.save(createdUser);
          Cart cart = new Cart();
          cart.setUser(user);
          cartRepository.save(cart);

      }

      List<GrantedAuthority> authorities = new ArrayList<>();
      authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(req.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtProvider.generateToken(authentication);
    }
}

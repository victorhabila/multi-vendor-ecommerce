package com.multi_vendo_ecom.ecommerce.multivendor.service.impl;

import com.multi_vendo_ecom.ecommerce.multivendor.config.JwtProvider;
import com.multi_vendo_ecom.ecommerce.multivendor.domain.USER_ROLE;
import com.multi_vendo_ecom.ecommerce.multivendor.model.Cart;
import com.multi_vendo_ecom.ecommerce.multivendor.model.User;
import com.multi_vendo_ecom.ecommerce.multivendor.model.VerificationCode;
import com.multi_vendo_ecom.ecommerce.multivendor.repository.CartRepository;
import com.multi_vendo_ecom.ecommerce.multivendor.repository.UserRepository;
import com.multi_vendo_ecom.ecommerce.multivendor.repository.VerificationCodeRepository;
import com.multi_vendo_ecom.ecommerce.multivendor.request.LoginRequest;
import com.multi_vendo_ecom.ecommerce.multivendor.response.AuthResponse;
import com.multi_vendo_ecom.ecommerce.multivendor.response.SignupRequest;
import com.multi_vendo_ecom.ecommerce.multivendor.service.AuthService;
import com.multi_vendo_ecom.ecommerce.multivendor.service.EmailService;
import com.multi_vendo_ecom.ecommerce.multivendor.utils.OtpUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;

    private final VerificationCodeRepository verificationCodeRepository;

    private final EmailService emailService;

    private final CustomUserServiceImpl customUserService;

    // Manually define the constructor
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, CartRepository cartRepository, JwtProvider jwtProvider, VerificationCodeRepository verificationCodeRepository, EmailService emailService, CustomUserServiceImpl customUserService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartRepository = cartRepository;
        this.jwtProvider = jwtProvider;
        this.verificationCodeRepository = verificationCodeRepository;
        this.emailService = emailService;
        this.customUserService = customUserService;
    }


    @Override
    public void sentLoginAndSignUpOtp(String email) throws Exception {

        String SIGNING_PREFIX="signin_";

        if(email.startsWith(SIGNING_PREFIX)){
            email = email.substring(SIGNING_PREFIX.length());

            User user = userRepository.findByEmail(email);
            if(user == null){
                throw new Exception("User not exist with provided email");
            }
        }

        VerificationCode isExist = verificationCodeRepository.findByEmail(email);
        if(isExist != null){
            verificationCodeRepository.delete(isExist);
        }
        String otp = OtpUtil.generatedOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        verificationCodeRepository.save(verificationCode);

        String subject ="Victor Habila login/signup otp";
        String text="your login/signup otp is -" + otp;

        emailService.sendVerificationOtpEmail(email,otp, subject, text);

    }

    @Override
    public String createUser(SignupRequest req) throws Exception {

      VerificationCode verificationCode = verificationCodeRepository.findByEmail(req.getEmail());

      if(verificationCode == null || !verificationCode.getOtp().equals(req.getOtp())){
          throw new Exception("Wrong otp...");
      }


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

    @Override
    public AuthResponse signin(LoginRequest req) {
       String username = req.getEmail();
       String otp = req.getOtp();

       Authentication authentication = authenticate(username, otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Login success");

        Collection<? extends  GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName=authorities.isEmpty()?null:authorities.iterator().next().getAuthority();

        authResponse.setRole(USER_ROLE.valueOf(roleName));
       return authResponse;
    }

    private Authentication authenticate(String username, String otp) {
       UserDetails userDetails=  customUserService.loadUserByUsername(username);

       if(userDetails == null){
           throw new BadCredentialsException("invalid username");
       }
       VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);

       if(verificationCode == null || !verificationCode.getOtp().equals(otp)){
           throw new BadCredentialsException("wrong otp");
       }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}

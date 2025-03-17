package com.multi_vendo_ecom.ecommerce.multivendor.service;

import com.multi_vendo_ecom.ecommerce.multivendor.domain.USER_ROLE;
import com.multi_vendo_ecom.ecommerce.multivendor.request.LoginRequest;
import com.multi_vendo_ecom.ecommerce.multivendor.response.AuthResponse;
import com.multi_vendo_ecom.ecommerce.multivendor.response.SignupRequest;

public interface AuthService {

    void sentLoginAndSignUpOtp(String email, USER_ROLE roel) throws Exception;
    String createUser(SignupRequest req) throws Exception;

    AuthResponse signin(LoginRequest req) throws Exception;


}

package com.multi_vendo_ecom.ecommerce.multivendor.service;

import com.multi_vendo_ecom.ecommerce.multivendor.response.SignupRequest;

public interface AuthService {

    void sentLoginAndSignUpOtp(String email) throws Exception;
    String createUser(SignupRequest req) throws Exception;


}

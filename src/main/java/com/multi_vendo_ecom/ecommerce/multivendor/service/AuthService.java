package com.multi_vendo_ecom.ecommerce.multivendor.service;

import com.multi_vendo_ecom.ecommerce.multivendor.response.SignupRequest;

public interface AuthService {

    String createUser(SignupRequest req);
}

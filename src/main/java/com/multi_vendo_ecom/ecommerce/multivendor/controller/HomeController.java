package com.multi_vendo_ecom.ecommerce.multivendor.controller;

import com.multi_vendo_ecom.ecommerce.multivendor.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeController {

    @GetMapping("home")
    public ApiResponse HomeController(){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Welcome to ecommerce multivendor project");
        return apiResponse;

    }
}

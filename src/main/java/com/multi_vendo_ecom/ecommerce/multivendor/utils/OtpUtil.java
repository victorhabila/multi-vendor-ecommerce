package com.multi_vendo_ecom.ecommerce.multivendor.utils;

import java.util.Random;

public class OtpUtil {

    public static String generatedOtp(){

        int otpLenth = 6;

        Random random = new Random();

        StringBuilder otp = new StringBuilder(otpLenth);

        for(int i =0; i<otpLenth; i++){
            otp.append(random.nextInt(10));
        }

        return otp.toString();
    }
}

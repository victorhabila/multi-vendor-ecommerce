package com.multi_vendo_ecom.ecommerce.multivendor.controller;

import com.multi_vendo_ecom.ecommerce.multivendor.config.JwtProvider;
import com.multi_vendo_ecom.ecommerce.multivendor.domain.AccountStatus;
import com.multi_vendo_ecom.ecommerce.multivendor.model.Seller;
import com.multi_vendo_ecom.ecommerce.multivendor.model.SellerReport;
import com.multi_vendo_ecom.ecommerce.multivendor.model.VerificationCode;
import com.multi_vendo_ecom.ecommerce.multivendor.repository.VerificationCodeRepository;
import com.multi_vendo_ecom.ecommerce.multivendor.request.LoginRequest;
import com.multi_vendo_ecom.ecommerce.multivendor.response.AuthResponse;
import com.multi_vendo_ecom.ecommerce.multivendor.service.AuthService;
import com.multi_vendo_ecom.ecommerce.multivendor.service.EmailService;
import com.multi_vendo_ecom.ecommerce.multivendor.service.SellerService;
import com.multi_vendo_ecom.ecommerce.multivendor.utils.OtpUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sellers")
public class SellerController {

    private final SellerService sellerService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final AuthService authService;

    private final EmailService emailService;
    private final JwtProvider jwtProvider;

    public SellerController(SellerService sellerService, VerificationCodeRepository verificationCodeRepository, AuthService authService, EmailService emailService, JwtProvider jwtProvider) {
        this.sellerService = sellerService;
        this.verificationCodeRepository = verificationCodeRepository;
        this.authService = authService;
        this.emailService = emailService;
        this.jwtProvider = jwtProvider;
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest req) throws Exception {

        String otp = req.getOtp();
        String email = req.getEmail();

        req.setEmail("seller_"+email); // we are providing seller prefix to differentiate between seller login ornnormal user login
        AuthResponse authResponse = authService.signin(req);

        return ResponseEntity.ok(authResponse);
    }




    @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp) throws Exception {

        VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);

        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new Exception("wrong otp ... ");
        }

        Seller seller = sellerService. verifyEmail(verificationCode.getEmail(), otp);
        return new ResponseEntity<>(seller, HttpStatus.OK);

    }

    @PostMapping()
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws Exception {

        Seller savedSeller = sellerService.createSeller(seller);

        String otp = OtpUtil.generatedOtp();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(seller.getEmail());
        verificationCodeRepository.save(verificationCode);

        String subject = "BuzzBuy Email Verification Code";
        String text = "Welcome to BuzzBuy,  verify your account using this link ";
        String frontend_url = "http://localhost:3000/verify-seller/";
        emailService.sendVerificationOtpEmail(seller.getEmail(), verificationCode.getOtp(), subject, text + frontend_url);
        return new ResponseEntity<>(savedSeller, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws Exception {

        Seller seller = sellerService.getSellerById(id);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerByJwt(@RequestHeader("Authorization") String jwt) throws Exception {

        Seller seller = sellerService.getSellerProfile(jwt);
        return new ResponseEntity<>(seller, HttpStatus.OK);

    }

//    @GetMapping("/report")
//    public ResponseEntity<SellerReport> getAllReport(@RequestHeader("Authorization") String jwt) throws Exception {
//
//        String email = jwtProvider.getEmailFromJwtToken(jwt);
//        Seller seller = sellerService.getSellerByEmail(email);
//
//        SellerReport report = sellerReportService.getSellerReport(seller);
//
//        return new ResponseEntity<>(report, HttpStatus.OK);
//
//    }


    @GetMapping()
    public ResponseEntity<List<Seller>> getAllSellers(@RequestParam(required= false) AccountStatus status){
        List<Seller> sellers = sellerService.getAllSellers(status);
        return ResponseEntity.ok(sellers);
    }

    @PatchMapping()//to update seller, we need the id of this seller from profile info through jwt token
    public ResponseEntity<Seller> updateSeller(@RequestHeader("Authorization") String jwt, @RequestBody Seller seller ) throws Exception {
        Seller profile = sellerService.getSellerProfile(jwt);
        Seller updatedSeller = sellerService.updateSeller(profile.getId(), seller);

        return ResponseEntity.ok(updatedSeller);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws Exception {

        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }






}

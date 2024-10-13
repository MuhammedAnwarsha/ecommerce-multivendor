package com.ecommerce.controller;

import com.ecommerce.Enum.AccountStatus;
import com.ecommerce.modal.Seller;
import com.ecommerce.modal.SellerReport;
import com.ecommerce.modal.VerificationCode;
import com.ecommerce.repository.VerificationCodeRepository;
import com.ecommerce.request.LoginRequest;
import com.ecommerce.response.ApiResponse;
import com.ecommerce.response.AuthResponse;
import com.ecommerce.service.AuthService;
import com.ecommerce.service.EmailService;
import com.ecommerce.service.SellerService;
import com.ecommerce.utils.OtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sellers")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest req) throws Exception {

        String otp = req.getOtp();
        String email = req.getEmail();

        req.setEmail("seller_" + email);
        AuthResponse authResponse = authService.signing(req);

        return ResponseEntity.ok(authResponse);
    }

    @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp) throws Exception {

        VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);

        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new Exception("Wrong otp...");
        }

        Seller seller = sellerService.verifyEmail(verificationCode.getEmail(), otp);
        return ResponseEntity.ok(seller);
    }

    @PostMapping
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws Exception {

        Seller savedSeller = sellerService.createSeller(seller);

        String otp = OtpUtil.generateOtp();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(seller.getEmail());
        verificationCodeRepository.save(verificationCode);

        String subject = "E-Commerce Email Verification Code";
        String text = "Welcome E-Commerce Multi Vendor platform, verify your account using this link ";
        String frontend_url = "http://localhost:3000/verify-seller/";

        emailService.sendVerificationOtpEmail(seller.getEmail(), verificationCode.getOtp(), subject, text + frontend_url);

        return new ResponseEntity<>(savedSeller, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws Exception {

        Seller seller = sellerService.getSellerById(id);
        return ResponseEntity.ok(seller);
    }

    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerByJwt(@RequestHeader("Authorization") String jwt) throws Exception {

        Seller seller = sellerService.getSellerProfile(jwt);
        return ResponseEntity.ok(seller);
    }

//    @GetMapping("/report")
//    public ResponseEntity<SellerReport> getSellerReport(@RequestHeader("Authorization")String jwt) throws Exception {
//
//        Seller seller = sellerService.getSellerProfile(jwt);
//        return ResponseEntity.ok(seller);
//    }

    @GetMapping
    public ResponseEntity<List<Seller>> getAllSellers(@RequestParam(required = false) AccountStatus status) {

        List<Seller> sellers = sellerService.getAllSellers(status);
        return ResponseEntity.ok(sellers);
    }

    @PatchMapping()
    public ResponseEntity<Seller> updateSeller(@RequestHeader("Authorization") String jwt,
                                               @RequestBody Seller seller) throws Exception {

        Seller profile = sellerService.getSellerProfile(jwt);
        Seller updatedSeller = sellerService.updateSeller(profile.getId(), seller);
        return ResponseEntity.ok(updatedSeller);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Seller> deleteSeller(@PathVariable Long id) throws Exception {

        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }
}

package com.ecommerce.controller;

import com.ecommerce.Enum.USER_ROLE;
import com.ecommerce.modal.VerificationCode;
import com.ecommerce.request.LoginOtpRequest;
import com.ecommerce.request.LoginRequest;
import com.ecommerce.request.SignupRequest;
import com.ecommerce.response.ApiResponse;
import com.ecommerce.response.AuthResponse;
import com.ecommerce.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(
            @RequestBody SignupRequest req) throws Exception {

        String jwt = authService.createUser(req);

        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setMessage("User register success");
        res.setRole(USER_ROLE.ROLE_CUSTOMER);

        return ResponseEntity.ok(res);
    }

    @PostMapping("/sent/login-signup-otp")
    public ResponseEntity<ApiResponse> sentOtpHandler(
            @RequestBody LoginOtpRequest req) throws Exception {

        authService.sentLoginOtp(req.getEmail(),req.getRole());

        ApiResponse res = new ApiResponse();
        res.setMessage("Otp sent successfully");

        return ResponseEntity.ok(res);
    }

    @PostMapping("/signing")
    public ResponseEntity<AuthResponse> loginHandler(
            @RequestBody LoginRequest req) throws Exception {

        AuthResponse authResponse = authService.signing(req);

        return ResponseEntity.ok(authResponse);
    }
}
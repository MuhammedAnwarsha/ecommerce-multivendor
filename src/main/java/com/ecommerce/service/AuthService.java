package com.ecommerce.service;

import com.ecommerce.request.LoginRequest;
import com.ecommerce.request.SignupRequest;
import com.ecommerce.response.AuthResponse;

public interface AuthService {

    void sentLoginOtp(String email) throws Exception;

    String createUser(SignupRequest req) throws Exception;

    AuthResponse signing(LoginRequest req);
}

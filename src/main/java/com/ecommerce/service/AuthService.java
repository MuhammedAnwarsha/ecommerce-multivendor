package com.ecommerce.service;

import com.ecommerce.Enum.USER_ROLE;
import com.ecommerce.request.LoginRequest;
import com.ecommerce.request.SignupRequest;
import com.ecommerce.response.AuthResponse;

public interface AuthService {

    void sentLoginOtp(String email, USER_ROLE role) throws Exception;

    String createUser(SignupRequest req) throws Exception;

    AuthResponse signing(LoginRequest req);
}

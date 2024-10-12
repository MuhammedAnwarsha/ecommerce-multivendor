package com.ecommerce.service;

import com.ecommerce.request.SignupRequest;

public interface AuthService {

    String createUser(SignupRequest req);
}

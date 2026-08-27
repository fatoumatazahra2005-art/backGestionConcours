package org.example.ges_concours.Repository;

import org.example.ges_concours.Dto.AuthResponse;
import org.example.ges_concours.Dto.LoginRequest;
import org.example.ges_concours.Dto.RegisterRequest;

public interface AuthInterface {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}

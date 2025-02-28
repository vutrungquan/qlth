package org.example.qlth1.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationRequest {
    private String username;
    private String password;
}

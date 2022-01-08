package com.login.social.socialLoginDemo.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationResponse {
    private boolean success;
    private String message;
}

package com.tasc.blogging.model.requset.user;

import lombok.Data;

@Data
public class RegisterRequest {

    private String email;
    private String password;
    private String confirmPassword;
}

package com.tasc.blogging.model.requset.user;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}

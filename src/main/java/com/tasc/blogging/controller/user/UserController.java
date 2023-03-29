package com.tasc.blogging.controller.user;

import com.tasc.blogging.aop.ApplicationException;
import com.tasc.blogging.controller.BaseController;
import com.tasc.blogging.model.requset.user.LoginRequest;
import com.tasc.blogging.model.requset.user.RegisterRequest;
import com.tasc.blogging.model.response.BaseResponse;
import com.tasc.blogging.service.JwtTokenBlacklistService;
import com.tasc.blogging.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenBlacklistService jwtTokenBlacklistService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest request, HttpServletRequest httpServletRequest) throws ApplicationException {
        return createdResponse(userService.login(request, httpServletRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse> register(@RequestBody RegisterRequest request) throws ApplicationException {
        return createdResponse(userService.register(request));
    }

    @PutMapping("/verify")
    public ResponseEntity<BaseResponse> verifyEmail(@RequestParam String code) throws ApplicationException {
        return createdResponse(userService.verifyEmail(code));
    }

    @PostMapping("/logout")
    public ResponseEntity<BaseResponse> logout(HttpServletRequest request) throws ApplicationException {
        jwtTokenBlacklistService.logout(request);
        return createdResponse(HttpStatus.OK);
    }
}

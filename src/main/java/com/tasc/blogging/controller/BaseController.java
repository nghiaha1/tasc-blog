package com.tasc.blogging.controller;

import com.tasc.blogging.model.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController {

    public ResponseEntity<BaseResponse> createdResponse(BaseResponse response) {
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<BaseResponse> createdResponse(BaseResponse response, HttpStatus httpStatus) {
        return new ResponseEntity<>(response, httpStatus);
    }

    public ResponseEntity<BaseResponse> createdResponse(HttpStatus httpStatus) {
        return new ResponseEntity<>(httpStatus);
    }
}

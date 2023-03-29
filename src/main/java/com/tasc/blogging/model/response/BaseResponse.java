package com.tasc.blogging.model.response;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BaseResponse<T> implements Serializable {

    private int code;
    private String message;

    @SerializedName("data")
    private T data;

    public BaseResponse() {
        code = 1;
        message = "Success";
    }

    public BaseResponse(String message) {
        code = 1;
        this.message = message;
    }

    public BaseResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseResponse(String message, T data) {
        this.code = 1;
        this.message = message;
        this.data = data;
    }
}

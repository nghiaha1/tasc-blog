package com.tasc.blogging.aop;

import com.tasc.blogging.entity.enums.ERROR;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationException extends RuntimeException {

    private int code;
    private String message;

    public ApplicationException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ApplicationException(String message) {
        super(message);
        code = 0;
        this.message = message;
    }

    public ApplicationException(ERROR error) {
        this.code = error.getCode();
        this.message = error.getMessage();
    }

    public ApplicationException() {

    }
}

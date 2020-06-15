package com.hungts.internetbanking.exception;

import com.hungts.internetbanking.define.ResultCode;
import lombok.Data;

@Data
public class EzException extends RuntimeException {
    private int code;
    private String message;

    public EzException(int code) {
        ResultCode resultCode = ResultCode.findByCode(code);
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public EzException(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public EzException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public EzException(String message) {
        this.code = 1;
        this.message = message;
    }

    public EzException(ResultCode resultCode, String message) {
        this.code = resultCode.getCode();
        this.message = message;
    }
}

package com.hungts.internetbanking.model.response;

import com.hungts.internetbanking.define.ResultCode;
import lombok.Data;

@Data
public class ResponseBody<T> {
    private int code = ResultCode.INTERNAL_ERROR.getCode();
    private String message = ResultCode.INTERNAL_ERROR.getMessage();
    private T data;

    public ResponseBody() {
    }

    public ResponseBody(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public ResponseBody(ResultCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    public ResponseBody(int resultCode, String message) {
        this.code = resultCode;
        this.message = message;
    }

    public ResponseBody(int resultCode, String message, T data) {
        this.code = resultCode;
        this.message = message;
        this.data = data;
    }

    public void setResultCode(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }
}

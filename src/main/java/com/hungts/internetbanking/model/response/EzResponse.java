package com.hungts.internetbanking.model.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class EzResponse {
    public static ResponseEntity response(ResponseBody body) {
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}
package com.hungts.internetbanking.exception;

import com.hungts.internetbanking.define.ResultCode;
import com.hungts.internetbanking.model.response.EzResponse;
import com.hungts.internetbanking.model.response.ResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@ControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllException(Exception e) {
        log.error("handleAllException", e);

        ResponseBody body = new ResponseBody();
        body.setResultCode(ResultCode.INTERNAL_ERROR);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @ExceptionHandler(EzException.class)
    public ResponseEntity<?> handleEzException(EzException e) {
        log.error("handleAllException", e);

        ResponseBody body = new ResponseBody();
        body.setCode(e.getCode());
        body.setMessage(e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        ResponseBody body = new ResponseBody();
        body.setCode(ResultCode.INVALID_DATA.getCode());
        body.setMessage(errors.toString());

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseBody body = new ResponseBody(ResultCode.INVALID_DATA.getCode(), "Request sai định dạng");
        return EzResponse.response(body);
    }
}

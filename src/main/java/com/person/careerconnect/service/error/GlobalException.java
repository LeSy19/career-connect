package com.person.careerconnect.service.error;

import com.person.careerconnect.domain.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<RestResponse> handleIdException(IdInvalidException IdException) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(IdException.getMessage());
        res.setMessage("IdException");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}

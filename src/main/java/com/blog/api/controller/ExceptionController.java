package com.blog.api.controller;

import com.blog.api.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {

//            FieldError fieldError = e.getFieldError();
//            String field = fieldError.getField();
//            String message = fieldError.getDefaultMessage();

//        ErrorResponse response = new ErrorResponse("400", "잘못된 요청입니다.");

        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .build();

        for(FieldError fieldError : e.getFieldErrors()) {
            response.addValidatation(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return response;
    }
}

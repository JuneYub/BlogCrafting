package com.blog.api.controller;

import com.blog.api.exception.InvalidRequest;
import com.blog.api.exception.PostNotFound;
import com.blog.api.exception.TopLevelException;
import com.blog.api.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PostNotFound.class)
    public ErrorResponse postNotFound(PostNotFound e) {
        ErrorResponse response = ErrorResponse.builder()
                .code("404")
                .message(e.getMessage())
                .build();

        return response;
    }

    @ResponseBody
    @ExceptionHandler(TopLevelException.class)
    public ResponseEntity<ErrorResponse> topLevelException(TopLevelException e) {
        int statusCode = e.getStatusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        // 응답 json validation -> title : 제목에 바보를 포함할 수 없습니다.
        /* e.getValidation 으로 대체
        if(e instanceof InvalidRequest) {
            InvalidRequest invalidRequest = (InvalidRequest) e;
            String fieldName = invalidRequest.getFieldName();
            String message = invalidRequest.getMessage();
            body.addValidatation(fieldName, message);
        }
         */


        ResponseEntity<ErrorResponse> response = ResponseEntity.status(statusCode)
                .body(body);

        return response;
    }



}

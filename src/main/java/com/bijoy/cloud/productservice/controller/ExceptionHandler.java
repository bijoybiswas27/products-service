package com.bijoy.cloud.productservice.controller;

import com.bijoy.cloud.productservice.dto.ValidationErrorDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        ValidationErrorDTO validationErrorDTO = new ValidationErrorDTO();
        validationErrorDTO.setStatusCode(400);
        StringBuilder message = new StringBuilder();
        ex.getBindingResult().getAllErrors().stream()
                .forEach(errorObject -> {
                    message.append(errorObject.getDefaultMessage() + ", ");
                });
        validationErrorDTO.setMessage(message.substring(0, message.length() - 2).toString());
        return ResponseEntity.badRequest().body(validationErrorDTO);
    }
}

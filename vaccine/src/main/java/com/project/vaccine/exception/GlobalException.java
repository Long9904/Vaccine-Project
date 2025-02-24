package com.project.vaccine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalException {

    //MethodArgumentNotValidException --> lỗi do thư viện quăng ra

    // Xử lí lỗi validation (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        Map<String, String> fieldErrors = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        errorResponse.put("message", "VALIDATION_ERROR");
        errorResponse.put("details", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    // Xử lí lỗi cú pháp JSON
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleJsonParseError(HttpMessageNotReadableException ex) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("message", "error");
        msg.put("details", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
    }

    // Xử lí lỗi duplicate
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<?> handleDuplicateException(DuplicateException ex) {
        return new ResponseEntity<>("Duplicate: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Xử lí lỗi not found
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>("Not found: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Xử lí lỗi authentication
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex) {
        return new ResponseEntity<>("Authentication error: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthorizeException.class)
    public ResponseEntity<?> handleAuthorizeException(AuthorizeException ex) {
        return new ResponseEntity<>("Authorize error: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}

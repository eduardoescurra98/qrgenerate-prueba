package com.example.qrgenerate.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(QrGenerationException.class)
    public ResponseEntity<Map<String, Object>> handleQrGenerationException(QrGenerationException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage()); // Solo el mensaje
        body.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}

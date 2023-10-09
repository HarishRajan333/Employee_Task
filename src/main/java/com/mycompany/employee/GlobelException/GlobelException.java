package com.mycompany.employee.GlobelException;

import com.mycompany.employee.Response.ApiResponse;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobelException {

    @ExceptionHandler(IOException.class)
    public ApiResponse ioExceptionadvice(IOException ex) {
        return ApiResponse.builder()
                .statusCode("604")
                .errors(Map.of("message", "Failed to Save File"))
                .build();
    }

    @ExceptionHandler(IllegalStateException.class)
    public ApiResponse illegalStateExceptionadvice(IllegalStateException ex) {
        return ApiResponse.builder()
                .statusCode("604")
                .errors(Map.of("message", "Path Mismatch"))
                .build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ApiResponse badCredentails(BadCredentialsException ex) {
        return ApiResponse.builder()
                .statusCode("604")
                .errors(Map.of("message", ex.getMessage()))
                .build();
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ApiResponse sqlException(SQLIntegrityConstraintViolationException ex) {
        return ApiResponse.builder()
                .statusCode("604")
                .errors(Map.of("message", ex.getMessage()))
                .build();
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ApiResponse userNotFoundException(UserNotFoundException ex) {
        return ApiResponse.builder()
                .statusCode("604")
                .errors(Map.of("message", "User Not Found."))
                .build();
    }
}

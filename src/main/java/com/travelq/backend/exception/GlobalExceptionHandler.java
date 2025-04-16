package com.travelq.backend.exception;

import com.travelq.backend.dto.common.ApiResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 전역 예외처리

    // ID조회 실패 예외처리
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<?>> handleNotFount(EntityNotFoundException e) {
        ApiResponseDTO<?> exceptionResponse = new ApiResponseDTO<>(false, e.getMessage(), null);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    // DTO 유효성 검사 실패 예외처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<?>> handleNotValid(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("유효성 검사에 실패했습니다.");

        ApiResponseDTO<?> exceptionResponse = new ApiResponseDTO<>(false, errorMessage, null);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}

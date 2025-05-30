package com.travelq.backend.exception;

import com.travelq.backend.dto.common.ApiResponseDTO;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 전역 예외처리

    // ID조회 실패 예외처리
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<?>> handleNotFount(EntityNotFoundException e) {
        log.error("엔티티 조회 실패 : {}", e.getMessage());

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

        log.error("DTO 유효성 검사 실패 : {}", errorMessage);

        ApiResponseDTO<?> exceptionResponse = new ApiResponseDTO<>(false, errorMessage, null);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    // 기능 구현 중 에러 발생 예외처리
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponseDTO<?>> handleRuntimeException(RuntimeException e) {
        log.error("서비스 중 서버 에러 발생 : {}", e);
        ApiResponseDTO<?> exceptionResponse = new ApiResponseDTO<>(false, e.getMessage(), null);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // OAuth2 로그인 에러
    @ExceptionHandler(OAuth2AuthenticationException.class)
    public ResponseEntity<ApiResponseDTO<?>> handleOAuth2AuthenticationException(OAuth2AuthenticationException e) {
        log.error("로그인 중 에러 발생 : {}", e.getMessage());
        ApiResponseDTO<?> exceptionResponse = new ApiResponseDTO<>(false, e.getMessage(), null);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    // JWT 관련 에러
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponseDTO<?>> handleJwtException(JwtException e) {
        log.error("JWT 에러 발생 : {}", e.getMessage());
        ApiResponseDTO<?> exceptionResponse = new ApiResponseDTO<>(false, e.getMessage(), null);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    // 알 수 없는 에러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<?>> handleException(Exception e) {
        log.error("알 수 없는 에러 발생 : {}", e);
        ApiResponseDTO<?> exceptionResponse = new ApiResponseDTO<>(false, e.getMessage(), null);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

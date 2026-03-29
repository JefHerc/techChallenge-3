package com.fiap.gestao_servicos.infrastructure.controller;

import com.fiap.gestao_servicos.core.exception.DuplicateDataException;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateDataException.class)
    public ResponseEntity<ApiErrorResponse> handleEstabelecimentoDuplicado(DuplicateDataException ex,
                                        HttpServletRequest request) {
    return buildResponse(
        HttpStatus.CONFLICT,
        "DADO_DUPLICADO",
        ex.getMessage(),
        request,
        List.of()
    );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(ResourceNotFoundException ex,
                                                                   HttpServletRequest request) {
    return buildResponse(
        HttpStatus.NOT_FOUND,
        "NAO_ENCONTRADO",
        ex.getMessage(),
        request,
        List.of()
    );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleValorInvalido(IllegalArgumentException ex,
                                 HttpServletRequest request) {
    ApiFieldError fieldError = new ApiFieldError(
        "request",
        ex.getMessage()
    );

    return buildResponse(
        HttpStatus.BAD_REQUEST,
        "VALOR_INVALIDO",
        ex.getMessage(),
        request,
        List.of(fieldError)
    );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                             HttpServletRequest request) {
        List<ApiFieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ApiFieldError(error.getField(), error.getDefaultMessage()))
                .toList();

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "VALIDACAO_ENTRADA",
                "Erro de validação na requisição.",
                request,
                fieldErrors
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex,
                              HttpServletRequest request) {
    return buildResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "ERRO_INTERNO",
        "Erro interno no servidor.",
        request,
        List.of()
    );
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status,
                               String code,
                               String message,
                               HttpServletRequest request,
                               List<ApiFieldError> errors) {
    ApiErrorResponse errorResponse = new ApiErrorResponse(
        status.value(),
        code,
        message,
        Instant.now(),
        request.getRequestURI(),
        errors
        );
    return ResponseEntity.status(status).body(errorResponse);
    }
}



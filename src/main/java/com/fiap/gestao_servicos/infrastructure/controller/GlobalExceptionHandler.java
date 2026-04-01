package com.fiap.gestao_servicos.infrastructure.controller;

import com.fiap.gestao_servicos.core.exception.BusinessRuleException;
import com.fiap.gestao_servicos.core.exception.DuplicateDataException;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.method.ParameterErrors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DuplicateDataException.class)
    public ResponseEntity<ApiErrorResponse> handleEstabelecimentoDuplicado(DuplicateDataException ex,
                                        HttpServletRequest request) {
    return buildResponse(
        HttpStatus.CONFLICT,
        "DADO_DUPLICADO",
        ex.getMessage(),
        request
    );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(ResourceNotFoundException ex,
                                                                   HttpServletRequest request) {
    return buildResponse(
        HttpStatus.NOT_FOUND,
        "NAO_ENCONTRADO",
        ex.getMessage(),
        request
    );
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessRule(BusinessRuleException ex,
                                                               HttpServletRequest request) {
    return buildResponse(
        HttpStatus.BAD_REQUEST,
        "REGRA_NEGOCIO",
        ex.getMessage(),
        request
    );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleValorInvalido(IllegalArgumentException ex,
                                 HttpServletRequest request) {
    return buildResponse(
        HttpStatus.BAD_REQUEST,
        "VALOR_INVALIDO",
        ex.getMessage(),
        request
    );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex,
                                                                         HttpServletRequest request) {
        return buildResponse(
            HttpStatus.CONFLICT,
            "RECURSO_EM_USO",
            "Não é possível concluir a operação porque existem dados vinculados a este recurso.",
            request
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                          HttpServletRequest request) {
        String detail = ex.getMostSpecificCause() != null
            ? ex.getMostSpecificCause().getMessage()
            : ex.getMessage();

        LOG.warn("JSON inválido em {}: {}", request.getRequestURI(), detail);

        return buildResponse(
            HttpStatus.BAD_REQUEST,
            "JSON_INVALIDO",
            "Corpo da requisição inválido. Verifique o JSON enviado.",
            request
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                             HttpServletRequest request) {
        List<String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
            .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
            .toList();
        String message = fieldErrors.isEmpty()
            ? "Erro de validação na requisição."
            : String.join("; ", fieldErrors);
        return buildResponse(
            HttpStatus.BAD_REQUEST,
            "VALIDACAO_ENTRADA",
            message,
            request
        );
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleHandlerMethodValidation(HandlerMethodValidationException ex,
                                                                          HttpServletRequest request) {
        List<String> fieldErrors = ex.getParameterValidationResults().stream()
            .filter(ParameterErrors.class::isInstance)
            .map(ParameterErrors.class::cast)
            .flatMap(r -> r.getFieldErrors().stream())
            .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
            .toList();
        String message = fieldErrors.isEmpty()
            ? "Erro de validação na requisição."
            : String.join("; ", fieldErrors);
        return buildResponse(
            HttpStatus.BAD_REQUEST,
            "VALIDACAO_ENTRADA",
            message,
            request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex,
                              HttpServletRequest request) {
    return buildResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "ERRO_INTERNO",
        "Erro interno no servidor.",
        request
    );
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status,
                               String code,
                               String message,
                               HttpServletRequest request) {
    ApiErrorResponse errorResponse = new ApiErrorResponse(
        status.value(),
        code,
        message,
        Instant.now(),
        request.getRequestURI()
        );
    return ResponseEntity.status(status).body(errorResponse);
    }
}



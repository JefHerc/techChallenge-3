package com.fiap.gestao_servicos.infrastructure.controller;

import com.fiap.gestao_servicos.core.exception.DuplicateDataException;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterErrors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void deveRetornarConflictParaDuplicateDataException() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/estabelecimentos");

        ResponseEntity<ApiErrorResponse> response = handler.handleEstabelecimentoDuplicado(
                new DuplicateDataException("CNPJ já cadastrado"),
                request
        );

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("DADO_DUPLICADO", response.getBody().code());
        assertEquals("CNPJ já cadastrado", response.getBody().message());
        assertEquals("/estabelecimentos", response.getBody().path());
        assertFalse(response.getBody().errors().isEmpty() == false);
    }

    @Test
    void deveRetornarNotFoundParaResourceNotFoundException() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/clientes/99");

        ResponseEntity<ApiErrorResponse> response = handler.handleResourceNotFound(
                new ResourceNotFoundException("Cliente não encontrado"),
                request
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("NAO_ENCONTRADO", response.getBody().code());
        assertEquals("Cliente não encontrado", response.getBody().message());
        assertEquals("/clientes/99", response.getBody().path());
        assertEquals(0, response.getBody().errors().size());
    }

    @Test
    void deveRetornarBadRequestParaIllegalArgumentExceptionComFieldErrorRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/agendamentos");

        ResponseEntity<ApiErrorResponse> response = handler.handleValorInvalido(
                new IllegalArgumentException("Status inválido"),
                request
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("VALOR_INVALIDO", response.getBody().code());
        assertEquals("Status inválido", response.getBody().message());
        assertEquals(1, response.getBody().errors().size());
        assertEquals("request", response.getBody().errors().get(0).field());
        assertEquals("Status inválido", response.getBody().errors().get(0).detail());
    }

    @Test
    void deveRetornarBadRequestParaMethodArgumentNotValidExceptionComListaDeErros() throws Exception {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "clienteDto");
        bindingResult.addError(new FieldError("clienteDto", "nome", "Nome é obrigatório"));
        bindingResult.addError(new FieldError("clienteDto", "email", "Email inválido"));

        MethodParameter parameter = new MethodParameter(
                GlobalExceptionHandlerTest.class.getDeclaredMethod("dummyMethod", String.class),
                0
        );
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, bindingResult);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/clientes");

        ResponseEntity<ApiErrorResponse> response = handler.handleValidation(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("VALIDACAO_ENTRADA", response.getBody().code());
        assertEquals("Erro de validação na requisição.", response.getBody().message());
        assertEquals(2, response.getBody().errors().size());
        assertEquals("nome", response.getBody().errors().get(0).field());
        assertEquals("Nome é obrigatório", response.getBody().errors().get(0).detail());
        assertEquals("email", response.getBody().errors().get(1).field());
        assertEquals("Email inválido", response.getBody().errors().get(1).detail());
    }

    @Test
    void deveRetornarBadRequestParaHandlerMethodValidationExceptionComListaDeErros() {
        HandlerMethodValidationException ex = Mockito.mock(HandlerMethodValidationException.class);
        ParameterErrors parameterErrors = Mockito.mock(ParameterErrors.class);

        when(ex.getParameterValidationResults()).thenReturn(List.of(parameterErrors));
        when(parameterErrors.getContainerIndex()).thenReturn(0);
        when(parameterErrors.getFieldErrors()).thenReturn(List.of(
                new FieldError("servicoDtos", "nome", "Nome do serviço é obrigatório")
        ));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/estabelecimentos/1/servicos");

        ResponseEntity<ApiErrorResponse> response = handler.handleHandlerMethodValidation(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("VALIDACAO_ENTRADA", response.getBody().code());
        assertEquals("Erro de validação na requisição.", response.getBody().message());
        assertEquals(1, response.getBody().errors().size());
        assertEquals("[0].nome", response.getBody().errors().get(0).field());
        assertEquals("Nome do serviço é obrigatório", response.getBody().errors().get(0).detail());
    }

    @Test
    void deveRetornarInternalServerErrorParaExcecaoGenerica() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/qualquer-rota");

        ResponseEntity<ApiErrorResponse> response = handler.handleGeneric(
                new RuntimeException("erro inesperado"),
                request
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERRO_INTERNO", response.getBody().code());
        assertEquals("Erro interno no servidor.", response.getBody().message());
        assertEquals("/qualquer-rota", response.getBody().path());
        assertEquals(0, response.getBody().errors().size());
    }

    private void dummyMethod(String ignored) {
    }
}

package com.fiap.gestao_servicos.infrastructure.controller;

import com.fiap.gestao_servicos.core.exception.BusinessRuleException;
import com.fiap.gestao_servicos.core.exception.DuplicateDataException;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterErrors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    }

    @Test
    void deveRetornarBadRequestParaBusinessRuleException() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/agendamentos");

        ResponseEntity<ApiErrorResponse> response = handler.handleBusinessRule(
                new BusinessRuleException("Estabelecimento fechado para o dia informado"),
                request
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("REGRA_NEGOCIO", response.getBody().code());
        assertEquals("Estabelecimento fechado para o dia informado", response.getBody().message());
        assertEquals("/agendamentos", response.getBody().path());
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
    }

    @Test
    void deveRetornarConflictParaDataIntegrityViolationException() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/estabelecimentos/3");

        ResponseEntity<ApiErrorResponse> response = handler.handleDataIntegrityViolation(
                new DataIntegrityViolationException("constraint violation"),
                request
        );

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("RECURSO_EM_USO", response.getBody().code());
        assertEquals("Não é possível concluir a operação porque existem dados vinculados a este recurso.", response.getBody().message());
        assertEquals("/estabelecimentos/3", response.getBody().path());
    }

    @Test
    void deveRetornarBadRequestParaHttpMessageNotReadableException() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/estabelecimentos");
        HttpInputMessage inputMessage = Mockito.mock(HttpInputMessage.class);
        String causa = "Cannot deserialize value";

        ResponseEntity<ApiErrorResponse> response = handler.handleHttpMessageNotReadable(
            new HttpMessageNotReadableException("json malformado", new RuntimeException(causa), inputMessage),
                request
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("JSON_INVALIDO", response.getBody().code());
        assertEquals("Corpo da requisição inválido. Verifique o JSON enviado.", response.getBody().message());
        assertEquals("/estabelecimentos", response.getBody().path());
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
        assertEquals("nome: Nome é obrigatório; email: Email inválido", response.getBody().message());
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
        assertEquals("nome: Nome do serviço é obrigatório", response.getBody().message());
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
    }

    @SuppressWarnings("unused")
    private void dummyMethod(String ignored) {
    }
}

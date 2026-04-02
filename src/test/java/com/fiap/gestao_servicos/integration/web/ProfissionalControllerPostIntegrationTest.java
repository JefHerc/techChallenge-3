package com.fiap.gestao_servicos.integration.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("bdd")
class ProfissionalControllerPostIntegrationTest extends WebLayerIntegrationTestBase {

    @Test
    @DisplayName("POST /estabelecimentos/{id}/profissionais deve retornar os dados do servico vinculado")
    void deveRetornarDadosDoServicoNoPostDeProfissional() throws Exception {
        String request = """
                {
                  \"nome\": \"Carla Souza 2\",
                  \"cpf\": \"40405852835\",
                  \"celular\": \"13991943508\",
                  \"email\": \"carl2a@e1.com\",
                  \"urlFoto\": \"https://cdn.exemplo.com/fotos/carla.jpg\",
                  \"descricao\": \"Especialista coloração\",
                  \"sexo\": \"FEMININO\",
                  \"expedientes\": [],
                  \"servicosProfissional\": [
                    {
                      \"servicoId\": 1,
                      \"valor\": 80
                    }
                  ]
                }
                """;

        mockMvc.perform(post("/estabelecimentos/{estabelecimentoId}/profissionais", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.servicosProfissional[0].servicoId").value(1))
                .andExpect(jsonPath("$.servicosProfissional[0].servicoNome", not(emptyOrNullString())))
                .andExpect(jsonPath("$.servicosProfissional[0].valor").value(80));
    }
}

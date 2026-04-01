package com.fiap.gestao_servicos.bdd.steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.gestao_servicos.bdd_config.ScenarioContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

public class ApiStepDefinitions {

    private static final AtomicInteger UNIQUE_COUNTER = new AtomicInteger(1);

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ScenarioContext scenarioContext;

    private String requestBody;
    private Long lastClienteId;
    private Long lastEstabelecimentoId;
    private Long lastServicoId;
    private Long lastProfissionalId;
    private Long lastAgendamentoId;
    private Long lastAvaliacaoId;
    private boolean createdCliente;
    private boolean createdEstabelecimento;
    private boolean createdServico;
    private boolean createdProfissional;
    private boolean createdAgendamento;
    private boolean createdAvaliacao;

    @Before
    public void resetScenarioState() {
        requestBody = null;
        lastClienteId = null;
        lastEstabelecimentoId = null;
        lastServicoId = null;
        lastProfissionalId = null;
        lastAgendamentoId = null;
        lastAvaliacaoId = null;
        createdCliente = false;
        createdEstabelecimento = false;
        createdServico = false;
        createdProfissional = false;
        createdAgendamento = false;
        createdAvaliacao = false;
        scenarioContext.setLastResponse(null);
    }

    @After
    public void cleanupScenarioResources() {
        tryDeleteCreatedResources();
    }

    @Dado("que tenho os dados de um novo cliente valido")
    public void queTenhoOsDadosDeUmNovoClienteValido() {
        requestBody = buildClienteJson(
                "Ana Lima BDD",
                generateValidCpf(),
                generatePhoneNumber(),
                generateEmail("cliente.novo"),
                "FEMININO"
        );
    }

    @Dado("que tenho os dados de um cliente com email invalido")
    public void queTenhoOsDadosDeUmClienteComEmailInvalido() {
        requestBody = buildClienteJson(
                "Cliente Email Invalido",
                generateValidCpf(),
                generatePhoneNumber(),
                "email-invalido",
                "FEMININO"
        );
    }

    @Dado("que tenho os dados de um cliente para atualizacao")
    public void queTenhoOsDadosDeUmClienteParaAtualizacao() {
        requestBody = buildClienteJson(
                "Cliente Atualizado BDD",
                generateValidCpf(),
                generatePhoneNumber(),
                generateEmail("cliente.atualizado"),
                "FEMININO"
        );
    }

    @Dado("que existe um cliente com id {long} no banco de dados")
    public void queExisteUmClienteComIdNoBancoDeDados(Long id) {
        lastClienteId = id;
    }

    @Dado("que existe um cliente criado para o cenario de atualizacao")
    public void queExisteUmClienteCriadoParaOCenarioDeAtualizacao() throws Exception {
        lastClienteId = createCliente(
                buildClienteJson(
                        "Cliente Base Atualizacao",
                        generateValidCpf(),
                        generatePhoneNumber(),
                        generateEmail("cliente.base.update"),
                        "FEMININO"
                )
        );
    }

    @Dado("que existe um cliente criado para o cenario de exclusao")
    public void queExisteUmClienteCriadoParaOCenarioDeExclusao() throws Exception {
        lastClienteId = createCliente(
                buildClienteJson(
                        "Cliente Base Exclusao",
                        generateValidCpf(),
                        generatePhoneNumber(),
                        generateEmail("cliente.base.delete"),
                        "MASCULINO"
                )
        );
    }

    @Dado("que tenho os dados de um novo estabelecimento valido")
    public void queTenhoOsDadosDeUmNovoEstabelecimentoValido() {
        requestBody = buildEstabelecimentoJson("Studio BDD Novo", generateValidCnpj(), "Rua BDD Nova", "101");
    }

    @Dado("que tenho os dados de um estabelecimento com payload invalido")
    public void queTenhoOsDadosDeUmEstabelecimentoComPayloadInvalido() {
        requestBody = """
                {
                  "nome": "A",
                  "endereco": {
                    "logradouro": "Rua das Flores",
                    "numero": "123",
                    "bairro": "Centro",
                    "cidade": "Sao Paulo",
                    "estado": "SP",
                    "cep": "01001000"
                  },
                  "cnpj": "11.444.777/0001-61",
                  "urlFotos": ["https://cdn.exemplo.com/estabelecimentos/bdd/invalido.jpg"],
                  "horarioFuncionamento": []
                }
                """;
    }

    @Dado("que tenho os dados de um estabelecimento para atualizacao")
    public void queTenhoOsDadosDeUmEstabelecimentoParaAtualizacao() {
        requestBody = buildEstabelecimentoJson(
                "Studio BDD Atualizado",
                generateValidCnpj(),
                "Avenida Atualizada",
                "202"
        );
    }

    @Dado("que existe um estabelecimento com id {long} no banco de dados")
    public void queExisteUmEstabelecimentoComIdNoBancoDeDados(Long id) {
        lastEstabelecimentoId = id;
    }

    @Dado("que existe um estabelecimento criado para o cenario de atualizacao")
    public void queExisteUmEstabelecimentoCriadoParaOCenarioDeAtualizacao() throws Exception {
        lastEstabelecimentoId = createEstabelecimento(
                buildEstabelecimentoJson("Studio BDD Base Update", generateValidCnpj(), "Rua Update", "301")
        );
    }

    @Dado("que existe um estabelecimento criado para o cenario de exclusao")
    public void queExisteUmEstabelecimentoCriadoParaOCenarioDeExclusao() throws Exception {
        lastEstabelecimentoId = createEstabelecimento(
                buildEstabelecimentoJson("Studio BDD Base Delete", generateValidCnpj(), "Rua Delete", "401")
        );
    }

    @Dado("que tenho a lista de servicos para cadastro")
    public void queTenhoAListaDeServicosParaCadastro() {
        requestBody = """
                [
                  {
                    "nome": "ESCOVA",
                    "duracaoMedia": "PT30M"
                  }
                ]
                """;
    }

    @Dado("que existe um servico com id {long} no estabelecimento com id {long}")
    public void queExisteUmServicoComIdNoEstabelecimentoComId(Long servicoId, Long estabelecimentoId) {
        lastServicoId = servicoId;
        lastEstabelecimentoId = estabelecimentoId;
    }

    @Dado("que existe um servico criado para o cenario de exclusao no estabelecimento {long}")
    public void queExisteUmServicoCriadoParaOCenarioDeExclusaoNoEstabelecimento(Long estabelecimentoId) throws Exception {
        lastEstabelecimentoId = estabelecimentoId;
        lastServicoId = createServico(
                """
                [
                  {
                    "nome": "MANICURE",
                    "duracaoMedia": "PT45M"
                  }
                ]
                """,
                estabelecimentoId
        );
    }

    @Dado("que tenho os dados de um novo profissional valido no estabelecimento 1")
    public void queTenhoOsDadosDeUmNovoProfissionalValidoNoEstabelecimentoUm() {
        requestBody = buildProfissionalJson(
                "Profissional BDD Novo",
                generateValidCpf(),
                generatePhoneNumber(),
                generateEmail("profissional.novo")
        );
    }

    @Dado("que tenho os dados de um profissional com email invalido")
    public void queTenhoOsDadosDeUmProfissionalComEmailInvalido() {
        requestBody = buildProfissionalJson(
                "Profissional Email Invalido",
                generateValidCpf(),
                generatePhoneNumber(),
                "email-invalido"
        );
    }

    @Dado("que tenho os dados de um profissional com expediente fora do horario do estabelecimento")
    public void queTenhoOsDadosDeUmProfissionalComExpedienteForaDoHorarioDoEstabelecimento() {
        requestBody = buildProfissionalJsonForaDoHorario(
                "Profissional Fora do Horario",
                generateValidCpf(),
                generatePhoneNumber(),
                generateEmail("profissional.fora.horario")
        );
    }

    @Dado("que existe um profissional criado para o cenario de exclusao no estabelecimento {long}")
    public void queExisteUmProfissionalCriadoParaOCenarioDeExclusaoNoEstabelecimento(Long estabelecimentoId)
            throws Exception {
        lastEstabelecimentoId = estabelecimentoId;
        lastProfissionalId = createProfissional(
                buildProfissionalJson(
                        "Profissional BDD Exclusao",
                        generateValidCpf(),
                        generatePhoneNumber(),
                        generateEmail("profissional.exclusao")
                ),
                estabelecimentoId
        );
    }

    @Dado("que existe uma base de dados com estabelecimento, profissional, servico e cliente configurados")
    public void queExisteUmaBaseDeDadosComEstabelecimentoProfissionalServicoEClienteConfigurados() {
        lastEstabelecimentoId = 1L;
    }

    @Dado("que tenho os dados de um novo agendamento valido para segunda-feira")
    public void queTenhoOsDadosDeUmNovoAgendamentoValidoParaSegundaFeira() {
        requestBody = buildAgendamentoJson("2026-04-06T11:30:00");
    }

    @Dado("que tenho os dados de um agendamento com campos obrigatorios ausentes")
    public void queTenhoOsDadosDeUmAgendamentoComCamposObrigatoriosAusentes() {
        requestBody = """
                {
                  "profissionalId": 1
                }
                """;
    }

    @Dado("que tenho os dados de um novo agendamento fora do horario de funcionamento")
    public void queTenhoOsDadosDeUmNovoAgendamentoForaDoHorarioDeFuncionamento() {
        requestBody = buildAgendamentoJson("2026-04-06T23:30:00");
    }

    @Dado("que existe um agendamento criado para o cenario de conflito")
    public void queExisteUmAgendamentoCriadoParaOCenarioDeConflito() throws Exception {
        lastAgendamentoId = createAgendamento(buildAgendamentoJson("2026-04-06T15:00:00"), 1L);
    }

    @Dado("que tenho os dados de um agendamento em horario ja ocupado pelo profissional")
    public void queTenhoOsDadosDeUmAgendamentoEmHorarioJaOcupadoPeloProfissional() {
        requestBody = buildAgendamentoJson("2026-04-06T15:00:00");
    }

    @Dado("que existe um agendamento criado para o cenario de atualizacao")
    public void queExisteUmAgendamentoCriadoParaOCenarioDeAtualizacao() throws Exception {
        lastAgendamentoId = createAgendamento(buildAgendamentoJson("2026-04-06T11:30:00"), 1L);
    }

    @Dado("que existe um agendamento criado para o cenario de exclusao")
    public void queExisteUmAgendamentoCriadoParaOCenarioDeExclusao() throws Exception {
        lastAgendamentoId = createAgendamento(buildAgendamentoJson("2026-04-06T13:00:00"), 1L);
    }

    @Dado("que tenho os dados de atualizacao de agendamento com status CONCLUIDO")
    public void queTenhoOsDadosDeAtualizacaoDeAgendamentoComStatusConcluido() {
        requestBody = buildAgendamentoUpdateJson("2026-04-06T16:00:00", "CONCLUIDO");
    }

    @Dado("que existe um agendamento concluido para avaliacao")
    public void queExisteUmAgendamentoConcluidoParaAvaliacao() throws Exception {
        lastAgendamentoId = createAgendamento(buildAgendamentoJson("2026-04-06T14:30:00"), 1L);
        requestBody = buildAgendamentoUpdateJson("2026-04-06T14:30:00", "CONCLUIDO");
        MockHttpServletResponse response = performRequest(
                "PUT",
                "/estabelecimentos/1/agendamentos/" + lastAgendamentoId,
                requestBody
        );
        Assertions.assertEquals(200, response.getStatus());
    }

    @Dado("que existe um agendamento criado para avaliacao ainda nao concluido")
    public void queExisteUmAgendamentoCriadoParaAvaliacaoAindaNaoConcluido() throws Exception {
        lastAgendamentoId = createAgendamento(buildAgendamentoJson("2026-04-06T17:00:00"), 1L);
    }

    @Dado("que tenho os dados de uma nova avaliacao valida")
    public void queTenhoOsDadosDeUmaNovaAvaliacaoValida() {
        requestBody = """
                {
                  "notaEstabelecimento": 4.5,
                  "notaProfissional": 5.0,
                  "comentarioEstabelecimento": "Ambiente limpo e atendimento excelente",
                  "comentarioProfissional": "Profissional muito atenciosa"
                }
                """;
    }

    @Dado("que existe uma avaliacao criada para o cenario de exclusao")
    public void queExisteUmaAvaliacaoCriadaParaOCenarioDeExclusao() throws Exception {
        queExisteUmAgendamentoConcluidoParaAvaliacao();
        lastAvaliacaoId = createAvaliacao(requestBodyAvaliacaoExclusao(), 1L, lastAgendamentoId);
    }

    @Quando("envio uma requisicao {word} para {string}")
    public void envioUmaRequisicaoPara(String method, String path) throws Exception {
        MockHttpServletResponse response = performRequest(method, path, requestBody);
        captureCreatedResource(method, path, response);
        scenarioContext.setLastResponse(response);
    }

    @Quando("envio uma requisicao PUT para o ultimo cliente cadastrado")
    public void envioUmaRequisicaoPutParaOUltimoClienteCadastrado() throws Exception {
        Assertions.assertNotNull(lastClienteId);
        scenarioContext.setLastResponse(performRequest("PUT", "/clientes/" + lastClienteId, requestBody));
    }

    @Quando("envio uma requisicao DELETE para o ultimo cliente cadastrado")
    public void envioUmaRequisicaoDeleteParaOUltimoClienteCadastrado() throws Exception {
        Assertions.assertNotNull(lastClienteId);
        scenarioContext.setLastResponse(performRequest("DELETE", "/clientes/" + lastClienteId, null));
    }

    @Quando("envio uma requisicao PUT para o ultimo estabelecimento cadastrado")
    public void envioUmaRequisicaoPutParaOUltimoEstabelecimentoCadastrado() throws Exception {
        Assertions.assertNotNull(lastEstabelecimentoId);
        scenarioContext.setLastResponse(
                performRequest("PUT", "/estabelecimentos/" + lastEstabelecimentoId, requestBody)
        );
    }

    @Quando("envio uma requisicao DELETE para o ultimo estabelecimento cadastrado")
    public void envioUmaRequisicaoDeleteParaOUltimoEstabelecimentoCadastrado() throws Exception {
        Assertions.assertNotNull(lastEstabelecimentoId);
        scenarioContext.setLastResponse(performRequest("DELETE", "/estabelecimentos/" + lastEstabelecimentoId, null));
    }

    @Quando("envio uma requisicao DELETE para o ultimo servico criado no estabelecimento {long}")
    public void envioUmaRequisicaoDeleteParaOUltimoServicoCriadoNoEstabelecimento(Long estabelecimentoId) throws Exception {
        Assertions.assertNotNull(lastServicoId);
        scenarioContext.setLastResponse(
                performRequest("DELETE", "/estabelecimentos/" + estabelecimentoId + "/servicos/" + lastServicoId, null)
        );
    }

    @Quando("envio uma requisicao DELETE para o ultimo profissional criado no estabelecimento {long}")
    public void envioUmaRequisicaoDeleteParaOUltimoProfissionalCriadoNoEstabelecimento(Long estabelecimentoId)
            throws Exception {
        Assertions.assertNotNull(lastProfissionalId);
        scenarioContext.setLastResponse(
                performRequest(
                        "DELETE",
                        "/estabelecimentos/" + estabelecimentoId + "/profissionais/" + lastProfissionalId,
                        null
                )
        );
    }

    @Quando("envio uma requisicao PUT para o ultimo agendamento criado no estabelecimento {long}")
    public void envioUmaRequisicaoPutParaOUltimoAgendamentoCriadoNoEstabelecimento(Long estabelecimentoId) throws Exception {
        Assertions.assertNotNull(lastAgendamentoId);
        scenarioContext.setLastResponse(
                performRequest(
                        "PUT",
                        "/estabelecimentos/" + estabelecimentoId + "/agendamentos/" + lastAgendamentoId,
                        requestBody
                )
        );
    }

    @Quando("envio uma requisicao DELETE para o ultimo agendamento criado no estabelecimento {long}")
    public void envioUmaRequisicaoDeleteParaOUltimoAgendamentoCriadoNoEstabelecimento(Long estabelecimentoId) throws Exception {
        Assertions.assertNotNull(lastAgendamentoId);
        scenarioContext.setLastResponse(
                performRequest(
                        "DELETE",
                        "/estabelecimentos/" + estabelecimentoId + "/agendamentos/" + lastAgendamentoId,
                        null
                )
        );
    }

    @Quando("envio uma requisicao POST para o agendamento de avaliacao no estabelecimento {long}")
    public void envioUmaRequisicaoPostParaOAgendamentoDeAvaliacaoNoEstabelecimento(Long estabelecimentoId)
            throws Exception {
        Assertions.assertNotNull(lastAgendamentoId);
        scenarioContext.setLastResponse(
                performRequest(
                        "POST",
                        "/estabelecimentos/" + estabelecimentoId + "/agendamentos/" + lastAgendamentoId + "/avaliacoes",
                        requestBody
                )
        );
    }

    @Quando("envio uma requisicao GET para uma avaliacao inexistente do agendamento de avaliacao no estabelecimento {long}")
    public void envioUmaRequisicaoGetParaUmaAvaliacaoInexistenteDoAgendamentoDeAvaliacaoNoEstabelecimento(
            Long estabelecimentoId) throws Exception {
        Assertions.assertNotNull(lastAgendamentoId);
        scenarioContext.setLastResponse(
                performRequest(
                        "GET",
                        "/estabelecimentos/" + estabelecimentoId + "/agendamentos/" + lastAgendamentoId + "/avaliacoes/99999",
                        null
                )
        );
    }

    @Quando("envio uma requisicao DELETE para a ultima avaliacao criada no estabelecimento {long}")
    public void envioUmaRequisicaoDeleteParaAUltimaAvaliacaoCriadaNoEstabelecimento(Long estabelecimentoId)
            throws Exception {
        Assertions.assertNotNull(lastAvaliacaoId);
        scenarioContext.setLastResponse(
                performRequest(
                        "DELETE",
                        "/estabelecimentos/" + estabelecimentoId + "/agendamentos/avaliacoes/" + lastAvaliacaoId,
                        null
                )
        );
    }

    @Entao("o status da resposta deve ser {int}")
    public void oStatusDaRespostaDeveSer(int statusEsperado) {
        Assertions.assertNotNull(scenarioContext.getLastResponse());
        Assertions.assertEquals(
                statusEsperado,
                scenarioContext.getStatusCode(),
                () -> "Resposta recebida: " + scenarioContext.getResponseBody()
        );
    }

    @Entao("o codigo de erro deve ser {string}")
    public void oCodigoDeErroDeveSer(String codigoEsperado) throws Exception {
        JsonNode json = readResponseAsJson();
        Assertions.assertEquals(codigoEsperado, json.path("code").asText());
    }

    @Entao("a mensagem de erro deve conter {string}")
    public void aMensagemDeErroDeveConter(String trechoEsperado) throws Exception {
        JsonNode json = readResponseAsJson();
        Assertions.assertTrue(json.path("message").asText().contains(trechoEsperado));
    }

    @Entao("o corpo da resposta deve conter o nome {string}")
    public void oCorpoDaRespostaDeveConterONome(String nomeEsperado) {
        Assertions.assertTrue(scenarioContext.getResponseBody().contains(nomeEsperado));
    }

    @Entao("o corpo da resposta deve conter o campo {string}")
    public void oCorpoDaRespostaDeveConterOCampo(String campo) throws Exception {
        JsonNode fieldNode = findField(readResponseAsJson(), campo);
        Assertions.assertFalse(fieldNode.isMissingNode());
        Assertions.assertFalse(fieldNode.isNull());
    }

    @Entao("o campo {string} deve ser {string}")
    public void oCampoDeveSer(String campo, String valorEsperado) throws Exception {
        JsonNode fieldNode = findField(readResponseAsJson(), campo);
        Assertions.assertFalse(fieldNode.isMissingNode());
        Assertions.assertEquals(valorEsperado, fieldNode.asText());
    }

    private MockHttpServletResponse performRequest(String method, String path, String body) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = buildRequest(method, path, body);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        return response;
    }

    private MockHttpServletRequestBuilder buildRequest(String method, String path, String body) {
        String normalizedMethod = method.toUpperCase();
        return switch (normalizedMethod) {
            case "POST" -> post(path).contentType(MediaType.APPLICATION_JSON).content(body == null ? "" : body);
            case "PUT" -> put(path).contentType(MediaType.APPLICATION_JSON).content(body == null ? "" : body);
            case "DELETE" -> delete(path);
            case "GET" -> get(path);
            default -> throw new IllegalArgumentException("Metodo HTTP nao suportado: " + method);
        };
    }

    private JsonNode readResponseAsJson() throws Exception {
        return objectMapper.readTree(scenarioContext.getResponseBody());
    }

    private JsonNode findField(JsonNode jsonNode, String fieldName) {
        if (jsonNode.has(fieldName)) {
            return jsonNode.get(fieldName);
        }
        if (jsonNode.isArray() && !jsonNode.isEmpty() && jsonNode.get(0).has(fieldName)) {
            return jsonNode.get(0).get(fieldName);
        }
        if (jsonNode.has("content")
                && jsonNode.get("content").isArray()
                && !jsonNode.get("content").isEmpty()
                && jsonNode.get("content").get(0).has(fieldName)) {
            return jsonNode.get("content").get(0).get(fieldName);
        }
        return objectMapper.getNodeFactory().missingNode();
    }

    private Long createCliente(String jsonBody) throws Exception {
        MockHttpServletResponse response = performRequest("POST", "/clientes", jsonBody);
        Assertions.assertEquals(201, response.getStatus(), response.getContentAsString());
        createdCliente = true;
        return extractId(response.getContentAsString());
    }

    private Long createEstabelecimento(String jsonBody) throws Exception {
        MockHttpServletResponse response = performRequest("POST", "/estabelecimentos", jsonBody);
        Assertions.assertEquals(201, response.getStatus(), response.getContentAsString());
        createdEstabelecimento = true;
        return extractId(response.getContentAsString());
    }

    private Long createServico(String jsonBody, Long estabelecimentoId) throws Exception {
        MockHttpServletResponse response = performRequest(
                "POST",
                "/estabelecimentos/" + estabelecimentoId + "/servicos",
                jsonBody
        );
        Assertions.assertEquals(201, response.getStatus(), response.getContentAsString());
        createdServico = true;
        return extractId(response.getContentAsString());
    }

    private Long createProfissional(String jsonBody, Long estabelecimentoId) throws Exception {
        MockHttpServletResponse response = performRequest(
                "POST",
                "/estabelecimentos/" + estabelecimentoId + "/profissionais",
                jsonBody
        );
        Assertions.assertEquals(201, response.getStatus(), response.getContentAsString());
        createdProfissional = true;
        return extractId(response.getContentAsString());
    }

    private Long createAgendamento(String jsonBody, Long estabelecimentoId) throws Exception {
        MockHttpServletResponse response = performRequest(
                "POST",
                "/estabelecimentos/" + estabelecimentoId + "/agendamentos",
                jsonBody
        );
        Assertions.assertEquals(201, response.getStatus(), response.getContentAsString());
        createdAgendamento = true;
        return extractId(response.getContentAsString());
    }

    private Long createAvaliacao(String jsonBody, Long estabelecimentoId, Long agendamentoId) throws Exception {
        MockHttpServletResponse response = performRequest(
                "POST",
                "/estabelecimentos/" + estabelecimentoId + "/agendamentos/" + agendamentoId + "/avaliacoes",
                jsonBody
        );
        Assertions.assertEquals(201, response.getStatus(), response.getContentAsString());
        createdAvaliacao = true;
        return extractId(response.getContentAsString());
    }

    private Long extractId(String responseBody) throws Exception {
        JsonNode json = objectMapper.readTree(responseBody);
        if (json.has("id")) {
            return json.get("id").asLong();
        }
        if (json.isArray() && !json.isEmpty() && json.get(0).has("id")) {
            return json.get(0).get("id").asLong();
        }
        throw new IllegalStateException("Nao foi possivel localizar o campo id na resposta: " + responseBody);
    }

    private void captureCreatedResource(String method, String path, MockHttpServletResponse response) throws Exception {
        if (!"POST".equalsIgnoreCase(method) || response.getStatus() != 201) {
            return;
        }

        if ("/clientes".equals(path)) {
            lastClienteId = extractId(response.getContentAsString());
            createdCliente = true;
            return;
        }

        if ("/estabelecimentos".equals(path)) {
            lastEstabelecimentoId = extractId(response.getContentAsString());
            createdEstabelecimento = true;
            return;
        }

        if (path.matches("/estabelecimentos/\\d+/servicos")) {
            lastEstabelecimentoId = Long.parseLong(path.split("/")[2]);
            lastServicoId = extractId(response.getContentAsString());
            createdServico = true;
            return;
        }

        if (path.matches("/estabelecimentos/\\d+/profissionais")) {
            lastEstabelecimentoId = Long.parseLong(path.split("/")[2]);
            lastProfissionalId = extractId(response.getContentAsString());
            createdProfissional = true;
            return;
        }

        if (path.matches("/estabelecimentos/\\d+/agendamentos")) {
            lastEstabelecimentoId = Long.parseLong(path.split("/")[2]);
            lastAgendamentoId = extractId(response.getContentAsString());
            createdAgendamento = true;
            return;
        }

        if (path.matches("/estabelecimentos/\\d+/agendamentos/\\d+/avaliacoes")) {
            lastEstabelecimentoId = Long.parseLong(path.split("/")[2]);
            lastAgendamentoId = Long.parseLong(path.split("/")[4]);
            lastAvaliacaoId = extractId(response.getContentAsString());
            createdAvaliacao = true;
        }
    }

    private void tryDeleteCreatedResources() {
        if (createdAvaliacao && lastAvaliacaoId != null) {
            silentlyDelete("/estabelecimentos/1/agendamentos/avaliacoes/" + lastAvaliacaoId);
        }
        if (createdAgendamento && lastAgendamentoId != null) {
            silentlyDelete("/estabelecimentos/1/agendamentos/" + lastAgendamentoId);
        }
        if (createdServico && lastServicoId != null && lastEstabelecimentoId != null) {
            silentlyDelete("/estabelecimentos/" + lastEstabelecimentoId + "/servicos/" + lastServicoId);
        }
        if (createdProfissional && lastProfissionalId != null && lastEstabelecimentoId != null) {
            silentlyDelete("/estabelecimentos/" + lastEstabelecimentoId + "/profissionais/" + lastProfissionalId);
        }
        if (createdCliente && lastClienteId != null) {
            silentlyDelete("/clientes/" + lastClienteId);
        }
        if (createdEstabelecimento && lastEstabelecimentoId != null) {
            silentlyDelete("/estabelecimentos/" + lastEstabelecimentoId);
        }
    }

    private void silentlyDelete(String path) {
        try {
            performRequest("DELETE", path, null);
        } catch (Exception ignored) {
            // cleanup de melhor esforco para manter os cenarios independentes
        }
    }

    private String buildClienteJson(String nome, String cpf, String celular, String email, String sexo) {
        return """
                {
                  "nome": "%s",
                  "cpf": "%s",
                  "celular": "%s",
                  "email": "%s",
                  "sexo": "%s"
                }
                """.formatted(nome, cpf, celular, email, sexo);
    }

    private String buildEstabelecimentoJson(String nome, String cnpj, String logradouro, String numero) {
        return """
                {
                  "nome": "%s",
                  "endereco": {
                    "logradouro": "%s",
                    "numero": "%s",
                    "bairro": "Centro",
                    "cidade": "Sao Paulo",
                    "estado": "SP",
                    "cep": "01001000"
                  },
                  "cnpj": "%s",
                  "urlFotos": ["https://cdn.exemplo.com/estabelecimentos/bdd/foto.jpg"],
                  "horarioFuncionamento": []
                }
                """.formatted(nome, logradouro, numero, cnpj);
    }

    private String buildProfissionalJson(String nome, String cpf, String celular, String email) {
        return """
                {
                  "nome": "%s",
                  "cpf": "%s",
                  "celular": "%s",
                  "email": "%s",
                  "urlFoto": "https://cdn.exemplo.com/fotos/profissional-bdd.jpg",
                  "descricao": "Especialista em testes BDD",
                  "sexo": "FEMININO",
                  "expedientes": [],
                  "servicosProfissional": []
                }
                """.formatted(nome, cpf, celular, email);
    }

    private String buildProfissionalJsonForaDoHorario(String nome, String cpf, String celular, String email) {
        return """
                {
                  "nome": "%s",
                  "cpf": "%s",
                  "celular": "%s",
                  "email": "%s",
                  "urlFoto": "https://cdn.exemplo.com/fotos/profissional-bdd.jpg",
                  "descricao": "Profissional com expediente fora do horario",
                  "sexo": "FEMININO",
                  "expedientes": [
                    {
                      "diaSemana": "segunda-feira",
                      "inicioTurno": "06:00",
                      "fimTurno": "07:00"
                    }
                  ],
                  "servicosProfissional": []
                }
                """.formatted(nome, cpf, celular, email);
    }

    private String buildAgendamentoJson(String dataHoraInicio) {
        return """
                {
                  "profissionalId": 1,
                  "servicoId": 1,
                  "clienteId": 1,
                  "dataHoraInicio": "%s"
                }
                """.formatted(dataHoraInicio);
    }

    private String buildAgendamentoUpdateJson(String dataHoraInicio, String status) {
        return """
                {
                  "profissionalId": 1,
                  "servicoId": 1,
                  "clienteId": 1,
                  "dataHoraInicio": "%s",
                  "status": "%s"
                }
                """.formatted(dataHoraInicio, status);
    }

    private String requestBodyAvaliacaoExclusao() {
        return """
                {
                  "notaEstabelecimento": 4.0,
                  "notaProfissional": 4.7,
                  "comentarioEstabelecimento": "Boa experiencia no cenario de exclusao",
                  "comentarioProfissional": "Profissional muito cordial"
                }
                """;
    }

    private String generateEmail(String prefix) {
        int sequence = UNIQUE_COUNTER.getAndIncrement();
        return prefix + sequence + "@bdd.local";
    }

    private String generatePhoneNumber() {
        int sequence = UNIQUE_COUNTER.getAndIncrement();
        return String.format("119%08d", sequence % 100000000);
    }

    private String generateValidCpf() {
        int sequence = UNIQUE_COUNTER.getAndIncrement();
        String base = String.format("%09d", 100000000 + sequence);
        int firstDigit = calculateDigit(base, new int[]{10, 9, 8, 7, 6, 5, 4, 3, 2});
        int secondDigit = calculateDigit(base + firstDigit, new int[]{11, 10, 9, 8, 7, 6, 5, 4, 3, 2});
        return base + firstDigit + secondDigit;
    }

    private String generateValidCnpj() {
        int sequence = UNIQUE_COUNTER.getAndIncrement();
        String base = String.format("%012d", 100000000000L + sequence);
        int firstDigit = calculateDigit(base, new int[]{5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});
        int secondDigit = calculateDigit(base + firstDigit, new int[]{6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});
        return formatCnpj(base + firstDigit + secondDigit);
    }

    private int calculateDigit(String value, int[] weights) {
        int sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += Character.getNumericValue(value.charAt(i)) * weights[i];
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }

    private String formatCnpj(String digits) {
        return "%s.%s.%s/%s-%s".formatted(
                digits.substring(0, 2),
                digits.substring(2, 5),
                digits.substring(5, 8),
                digits.substring(8, 12),
                digits.substring(12)
        );
    }
}

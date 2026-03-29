package com.fiap.gestao_servicos.bdd.steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fiap.gestao_servicos.bdd.BddTestServer;
import com.fiap.gestao_servicos.bdd.ScenarioContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class CommonSteps {

    @When("o cliente envia POST para {string} com o corpo:")
    public void postComCorpo(String urlTemplate, String body) throws Exception {
        ScenarioContext context = BddTestServer.context();
        HttpResponse<String> response = BddTestServer.post(resolveUrl(urlTemplate, context), body);
        context.setLastResponse(response);
    }

    @When("o cliente envia PUT para {string} com o corpo:")
    public void putComCorpo(String urlTemplate, String body) throws Exception {
        ScenarioContext context = BddTestServer.context();
        HttpResponse<String> response = BddTestServer.put(resolveUrl(urlTemplate, context), body);
        context.setLastResponse(response);
    }

    @When("o cliente envia GET para {string}")
    public void getRequest(String urlTemplate) throws Exception {
        ScenarioContext context = BddTestServer.context();
        HttpResponse<String> response = BddTestServer.get(resolveUrl(urlTemplate, context));
        context.setLastResponse(response);
    }

    @When("o cliente envia DELETE para {string}")
    public void deleteRequest(String urlTemplate) throws Exception {
        ScenarioContext context = BddTestServer.context();
        HttpResponse<String> response = BddTestServer.delete(resolveUrl(urlTemplate, context));
        context.setLastResponse(response);
    }

    @Then("o status HTTP da resposta deve ser {int}")
    public void statusDeve(int expectedStatus) {
        assertThat(BddTestServer.context().getLastResponse().statusCode())
                .as("Status HTTP esperado: %d", expectedStatus)
                .isEqualTo(expectedStatus);
    }

    @And("o campo JSON {string} deve ser {string}")
    public void campoJsonDeveSerString(String jsonPath, String expectedValue) throws Exception {
        JsonNode node = resolveNode(jsonPath);
        assertThat(node).as("Campo JSON '%s' nao encontrado", jsonPath).isNotNull();
        assertThat(node.asText()).isEqualTo(expectedValue);
    }

    @And("o campo JSON {string} deve ser {int}")
    public void campoJsonDeveSerInt(String jsonPath, int expectedValue) throws Exception {
        JsonNode node = resolveNode(jsonPath);
        assertThat(node).as("Campo JSON '%s' nao encontrado", jsonPath).isNotNull();
        assertThat(node.asInt()).isEqualTo(expectedValue);
    }

    @And("o campo JSON {string} deve ser {double}")
    public void campoJsonDeveSerDouble(String jsonPath, double expectedValue) throws Exception {
        JsonNode node = resolveNode(jsonPath);
        assertThat(node).as("Campo JSON '%s' nao encontrado", jsonPath).isNotNull();
        assertThat(node.asDouble()).isEqualTo(expectedValue);
    }

    @And("a resposta deve conter um erro de validação no campo {string}")
    public void respostaDeveConterErroNoCampo(String fieldName) throws Exception {
        JsonNode json = responseJson();
        JsonNode errors = json.get("errors");
        assertThat(errors).isNotNull();
        assertThat(errors.isArray()).isTrue();

        boolean found = false;
        for (JsonNode error : errors) {
            JsonNode fieldNode = error.get("field");
            if (fieldNode != null && fieldNode.asText().contains(fieldName)) {
                found = true;
                break;
            }
        }

        assertThat(found)
                .as("Nenhum erro de validacao encontrado para o campo '%s'. Resposta: %s",
                        fieldName,
                        BddTestServer.context().getLastResponse().body())
                .isTrue();
    }

    @And("salvo o id do recurso criado")
    public void salvoIdDoRecursoCriado() throws Exception {
        JsonNode json = responseJson();
        BddTestServer.context().setLastCreatedId(json.get("id").asLong());
    }

    @And("salvo o id do recurso criado da lista JSON")
    public void salvoIdDoRecursoCriadoDaLista() throws Exception {
        JsonNode json = responseJson();
        BddTestServer.context().setLastCreatedId(json.get(0).get("id").asLong());
    }

    private JsonNode responseJson() throws Exception {
        return BddTestServer.objectMapper().readTree(BddTestServer.context().getLastResponse().body());
    }

    private JsonNode resolveNode(String path) throws Exception {
        JsonNode root = responseJson();
        String normalizedPath = path.startsWith("$") ? path.substring(1) : path;
        if (normalizedPath.startsWith(".")) {
            normalizedPath = normalizedPath.substring(1);
        }

        String[] parts = normalizedPath.split("(?=\\[)|\\.");
        JsonNode current = root;
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            if (part.startsWith("[") && part.endsWith("]")) {
                int index = Integer.parseInt(part.substring(1, part.length() - 1));
                current = current.get(index);
            } else {
                current = current.get(part);
            }
            if (current == null) {
                return null;
            }
        }
        return current;
    }

    private String resolveUrl(String urlTemplate, ScenarioContext context) {
        String url = urlTemplate;
        if (context.getLastCreatedId() != null) {
            url = url.replace("{lastId}", String.valueOf(context.getLastCreatedId()));
        }
        if (context.getLastAgendamentoId() != null) {
            url = url.replace("{agendamentoId}", String.valueOf(context.getLastAgendamentoId()));
        }
        return url;
    }
}
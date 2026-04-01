package com.fiap.gestao_servicos.bdd_config;

import io.cucumber.spring.ScenarioScope;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;

@Component
@ScenarioScope
public class ScenarioContext {

    private MockHttpServletResponse lastResponse;

    public MockHttpServletResponse getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(MockHttpServletResponse response) {
        this.lastResponse = response;
    }

    public int getStatusCode() {
        return lastResponse != null ? lastResponse.getStatus() : -1;
    }

    public String getResponseBody() {
        try {
            return lastResponse != null ? lastResponse.getContentAsString() : "";
        } catch (java.io.UnsupportedEncodingException e) {
            return "";
        }
    }
}

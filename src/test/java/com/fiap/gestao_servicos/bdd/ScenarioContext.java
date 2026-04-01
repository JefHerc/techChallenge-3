package com.fiap.gestao_servicos.bdd;

import java.net.http.HttpResponse;

public class ScenarioContext {

    private HttpResponse<String> lastResponse;
    private Long lastCreatedId;
    private Long lastAgendamentoId;
    private Integer lastEstabelecimentoId;

    public HttpResponse<String> getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(HttpResponse<String> lastResponse) {
        this.lastResponse = lastResponse;
    }

    public Long getLastCreatedId() {
        return lastCreatedId;
    }

    public void setLastCreatedId(Long lastCreatedId) {
        this.lastCreatedId = lastCreatedId;
    }

    public Long getLastAgendamentoId() {
        return lastAgendamentoId;
    }

    public void setLastAgendamentoId(Long lastAgendamentoId) {
        this.lastAgendamentoId = lastAgendamentoId;
    }

    public Integer getLastEstabelecimentoId() {
        return lastEstabelecimentoId;
    }

    public void setLastEstabelecimentoId(Integer lastEstabelecimentoId) {
        this.lastEstabelecimentoId = lastEstabelecimentoId;
    }

    public void reset() {
        lastResponse = null;
        lastCreatedId = null;
        lastAgendamentoId = null;
        lastEstabelecimentoId = null;
    }
}

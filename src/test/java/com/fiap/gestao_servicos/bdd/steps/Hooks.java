package com.fiap.gestao_servicos.bdd.steps;

import com.fiap.gestao_servicos.bdd.BddTestServer;
import io.cucumber.java.Before;

public class Hooks {

    @Before(order = 0)
    public void prepareScenario() {
        BddTestServer.startIfNeeded();
        BddTestServer.resetScenario();
    }
}
package com.fiap.gestao_servicos.integration.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public abstract class WebLayerIntegrationTestBase {

    @Autowired
    protected MockMvc mockMvc;
}

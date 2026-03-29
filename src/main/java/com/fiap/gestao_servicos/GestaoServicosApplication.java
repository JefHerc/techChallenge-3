package com.fiap.gestao_servicos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GestaoServicosApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestaoServicosApplication.class, args);
    }

}



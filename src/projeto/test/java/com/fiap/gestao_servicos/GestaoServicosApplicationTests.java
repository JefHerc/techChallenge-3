package com.fiap.gestao_servicos;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled("Requer configuração de banco para subir o contexto completo")
@SpringBootTest(properties = {
		"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration"
})
class GestaoServicosApplicationTests {

	@Test
	void contextLoads() {
	}

}



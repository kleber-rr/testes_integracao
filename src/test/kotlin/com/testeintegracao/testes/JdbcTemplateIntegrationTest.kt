package com.testeintegracao.testes

import com.testeintegracao.testes.dao.ProductDAO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate

@SpringBootTest
class JdbcTemplateIntegrationTest(
    @Autowired
    val jdbcTemplate: JdbcTemplate,

    @Autowired
    val dao: ProductDAO
) {



}
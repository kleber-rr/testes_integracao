package com.testeintegracao.testes

import com.testeintegracao.testes.repository.ProductRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup

@SpringBootTest
@ActiveProfiles("test")
class IntegrationDbTest(
    @Autowired
    val productRepository: ProductRepository
) {

    @Test
    @SqlGroup(
        Sql("/data-test.sql"),
        Sql("/clean_table.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    )
    fun criarInserirDoisProdutos() {
        var products = productRepository.findAll()
        Assertions.assertEquals(2, products.size)
    }

    @Test
    @SqlGroup(
        Sql(scripts = ["/clean_table.sql","/insert1.sql","/insert2.sql"]),
        Sql("/clean_table.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    )
    fun inserirDoisProdutosPorArquivoSql(){
        var products = productRepository.findAll()
        Assertions.assertEquals(2, products.size)
    }

    @Test
    @SqlGroup(
        Sql("/clean_table.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        Sql(statements = ["insert into PRODUCT (name,price) values('Microfone',15);"])
    )
    fun inserirUmProdutoPorScriptSql(){
        var products = productRepository.findAll()
        Assertions.assertEquals(1, products.size)
    }

}
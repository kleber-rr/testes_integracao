package com.testeintegracao.testes

import org.assertj.core.api.Assertions
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Sort
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup

@SpringBootTest
class JdbcIntegrationTest(
    @Autowired
    val repository: ProductRepository
) {
    @AfterEach
    fun cleanUp(){
        repository.deleteAll()
    }

    @Test
    @SqlGroup(
        Sql("/clean_table.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        Sql(statements = ["insert into product (name, price) values ('Mouse',15);"]),
    )
    fun findByNameIgnoreCaseReturnProduct(){
        val product = repository.findByNameIgnoreCase("mouse")
        Assertions.assertThat(product.name).isEqualTo("Mouse")
        Assertions.assertThat(product.price).isEqualTo(15)
    }

    @Test
    @SqlGroup(
        Sql("/clean_table.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        Sql(statements = ["insert into product (id,name,price) values (1,'Scanner',40);"]),
        Sql(statements = ["insert into product (id,name,price) values (2,'Mouse',15);"])
    )
    fun returnProductsSortByPriceAscendent(){
        val products = repository.findAll(Sort.by("price").ascending())
        Assertions.assertThat(products).extracting(Product::name).containsExactly(Tuple.tuple("Mouse"),Tuple.tuple("Scanner"))
    }

}
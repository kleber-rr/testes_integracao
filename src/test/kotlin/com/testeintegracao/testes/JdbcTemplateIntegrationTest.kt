package com.testeintegracao.testes

import com.testeintegracao.testes.dao.ProductDAO
import com.testeintegracao.testes.dao.ProductRowMapper
import com.testeintegracao.testes.model.Product
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.queryForObject
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
class JdbcTemplateIntegrationTest(
    @Autowired
    val jdbcTemplate: JdbcTemplate,

    @Autowired
    val dao: ProductDAO
) {

    @AfterEach
    fun cleanUp() {
        jdbcTemplate.execute("DELETE FROM product")
    }

    @Test
    @Sql(statements = ["insert into product(id,name,price) values(10,'Mouse',15)"])
    fun productShouldBeRemovedWhenDeleteIsCalled() {
        dao.deleteById(10 as Integer)
        var countAfterDelete: Integer = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM product")
        Assertions.assertThat(countAfterDelete).isEqualTo(0)
    }

    @Test
    fun productShouldBeInsertedWhenSaveIsCalled() {
        var product = dao.save(Product("WebCam", 5))
        var insertedProduct = jdbcTemplate.queryForObject("SELECT id, name, price FROM product WHERE id = ?", ProductRowMapper(), product.id)
        Assertions.assertThat(insertedProduct).isEqualTo(product)
    }

    @Test
    @Sql(statements = ["INSERT INTO product(id,name,price) values(11,'Mouse',15)"])
    fun findByIdReturnProduct() {
        var product = dao.findById(11 as Integer)
        Assertions.assertThat(product!!.name).isEqualTo("Mouse")
        Assertions.assertThat(product!!.price).isEqualTo(15)
    }



}
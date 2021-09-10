package com.testeintegracao.testes

import org.assertj.core.api.Assertions as assertJ
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions as junit
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.validation.ConstraintViolationException
import kotlin.test.assertFailsWith

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaRepositoryTest(
    @Autowired
    val repository: ProductRepository
) {
    @AfterEach
    fun cleanUp() {
        repository.deleteAll()
    }

    @Test
    @SqlGroup(
        Sql("/clean_table.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        Sql(statements = ["insert into product (name, price) values ('Mouse',15);"])
    )
    fun findByNameIgnoreCaseReturnAProduct(){
        var product = repository.findByNameIgnoreCase("mouse")
        assertJ.assertThat(product.name).isEqualTo("Mouse")
        assertJ.assertThat(product.price).isEqualTo(15)
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql("/clean_table.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    fun saveAProduct() {
        var product = Product("tstes", Integer.valueOf(50))
        junit.assertNotNull(repository.save(product).id)
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    fun saveAProductWithNameNullThrowsKotlinAssertFailsWithNameIsRequired() {
        var product = Product("", Integer.valueOf(60))
        assertFailsWith(
            exceptionClass = ConstraintViolationException::class,
            message = "Name is required",
            block = {repository.save(product)}
        )
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    fun saveAProductWithPriceOverFiftyThrowsKotlinAssertFailsWithPriceMustBeLetterOrEqualsThanFifty() {
        var product = Product("asdasdasdd", Integer.valueOf(60))
        assertFailsWith(
            exceptionClass = ConstraintViolationException::class,
            message = "Price must be letter or equals than fifty",
            block = {repository.save(product)}
        )
    }


    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    fun saveAProductWithNameNullThrowsAnExceptionJUnit(){
        var product = Product("", Integer.valueOf(60))
        org.junit.jupiter.api.Assertions.assertThrows(ConstraintViolationException::class.java, {repository.save(product)}, "Name is required")
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    fun saveAProductWithNameNullThrowsAssertThatThrowBy(){
        var product = Product("", Integer.valueOf(60))
        assertJ.assertThatThrownBy { repository.save(product) }
            .isInstanceOf(ConstraintViolationException::class.java)
            .hasMessageContaining("Name is required")
    }

}
package com.testeintegracao.testes

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

//@SpringBootTest
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [TextService::class])
class IntegrationTest(
    @Autowired
    val textService: TextService
) {

    @Test
    fun test(){
        textService.someMethod()
    }

}
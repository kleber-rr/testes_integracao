package com.testeintegracao.testes

import com.testeintegracao.testes.config.EnvironmentConfig
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class ProfilesIntegrationTest(
    @Autowired
    val environmentConfig: EnvironmentConfig
) {

    @Test
    fun test(){
        environmentConfig.someMethod()
    }
}
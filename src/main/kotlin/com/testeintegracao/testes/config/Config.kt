package com.testeintegracao.testes.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
class Config {

    @Bean
    @Profile("test")
    fun forTest(): EnvironmentConfig {
        return EnvironmentConfig("I am in test")
    }

    @Bean()
    @Profile("!test")
    fun forProduction(): EnvironmentConfig {
        return EnvironmentConfig("I am in production")
    }
}
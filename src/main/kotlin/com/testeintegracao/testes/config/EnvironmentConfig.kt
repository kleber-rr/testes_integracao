package com.testeintegracao.testes.config

class EnvironmentConfig(
    val somePhrase: String
) {

    fun someMethod(){
        println(somePhrase)
    }
}
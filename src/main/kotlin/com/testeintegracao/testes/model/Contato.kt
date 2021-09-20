package com.testeintegracao.testes.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity(name = "Contato")
data class Contato(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,

    @field:NotBlank(message = "O DDD deve ser preenchido")
    var ddd: String?,

    @field:NotBlank(message = "O telefone deve ser preenchido")
    var telefone: String?,

    @field:NotBlank(message = "O nome deve ser preenchido")
    var nome: String?

) {
    constructor() : this(null, "", "", "")
    constructor(ddd: String, telefone: String, nome: String) : this(null, ddd, telefone, nome)
}
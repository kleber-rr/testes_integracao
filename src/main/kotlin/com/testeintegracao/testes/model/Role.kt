package com.testeintegracao.testes.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import org.hibernate.envers.Audited
import org.springframework.security.core.GrantedAuthority
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity(name = "Role")
@JsonDeserialize
@Audited
@JsonIgnoreProperties("hibernateLazyInitializer")
data class Role(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,

    @field:NotBlank(message = "O nome da regra deve ser preenchido")
    var nome: String

): GrantedAuthority  {
    constructor() : this(null, "")
    constructor(nome: String) : this(null, nome)

    fun toMap() : Map<String, Object> {
        var asMap = HashMap<String, Object>()
        asMap["nome"] = nome as Object
        return asMap
    }

    override fun getAuthority(): String {
        return nome
    }
}
package com.testeintegracao.testes.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import org.hibernate.envers.Audited
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity(name = "Contato")
@JsonDeserialize
@Audited
@JsonIgnoreProperties("hibernateLazyInitializer")
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
    constructor(ddd: String?, telefone: String?, nome: String?) : this(null, ddd, telefone, nome)

    fun toMap() : Map<String, Object> {
        var asMap = HashMap<String, Object>()
        asMap.put("ddd", ddd as Object)
        asMap.put("telefone", telefone as Object)
        asMap.put("nome", nome as Object)
        return asMap
    }
}
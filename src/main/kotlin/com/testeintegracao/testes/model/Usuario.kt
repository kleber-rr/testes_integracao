package com.testeintegracao.testes.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import lombok.Data
import org.hibernate.envers.Audited
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity(name = "Usuario")
@JsonDeserialize
@Audited
@JsonIgnoreProperties("hibernateLazyInitializer")
@Data
open class Usuario(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long?,

    @Column(unique = true) @field:NotBlank(message = "O email deve ser preenchido")
    open var email: String?,

    @JsonIgnore @field:NotBlank(message = "A senha deve ser preenchida")
    open var senha: String?,

    @field:NotBlank(message = "O nome deve ser preenchido")
    open var nome: String?,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name="usuario_role",
        joinColumns= [JoinColumn(name="usuario_id")],
        inverseJoinColumns= [JoinColumn(name="role_id")]
    )
    open var roles: MutableSet<Role>

) {
    constructor() : this(null, "", "", "", mutableSetOf())
    constructor(email: String?, nome: String?) : this(null, email, null, nome, mutableSetOf())
    constructor(email: String?, password: String?, nome: String?, roles: MutableSet<Role>) : this(null, email, password, nome, roles)
    constructor(usuario: Usuario) : this(usuario.id, usuario.email, usuario.senha, usuario.nome, usuario.roles)

    fun toMap() : Map<String, Object> {
        var asMap = HashMap<String, Object>()
        asMap["nome"] = nome as Object
        asMap["email"] = email as Object
        asMap["roles"] = roles as Object
        return asMap
    }

}
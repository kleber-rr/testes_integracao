package com.testeintegracao.testes.init

import com.testeintegracao.testes.domain.Const
import com.testeintegracao.testes.model.Role
import com.testeintegracao.testes.model.Usuario
import com.testeintegracao.testes.repository.RoleRepository
import com.testeintegracao.testes.repository.UsuarioRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class DataInitializr(
    @Autowired val usuarioRepository: UsuarioRepository,
    @Autowired val roleRepository: RoleRepository,
    @Autowired val passwordEncoder: PasswordEncoder

): ApplicationListener<ContextRefreshedEvent> {

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        var users = usuarioRepository.findAll()
        if(users.isEmpty()) {
            createUser("Admin", "admin", passwordEncoder.encode("123456"), Const.ROLE_ADMIN)
            createUser("Cliente", "cliente", passwordEncoder.encode("123456"), Const.ROLE_CLIENT)
        }
    }

    fun createUser(name: String, email: String, password: String, roleName: String){
        var role = Role(roleName)
        roleRepository.save(role)

        var user = Usuario(email, password,name, mutableSetOf(role))
        usuarioRepository.save(user)
    }
}
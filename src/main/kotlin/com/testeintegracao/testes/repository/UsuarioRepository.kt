package com.testeintegracao.testes.repository

import com.testeintegracao.testes.model.Usuario
import org.springframework.data.jpa.repository.JpaRepository

interface UsuarioRepository: JpaRepository<Usuario, Long> {
    fun findByEmail(email:String): Usuario
}
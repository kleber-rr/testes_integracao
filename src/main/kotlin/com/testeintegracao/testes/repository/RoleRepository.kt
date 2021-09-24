package com.testeintegracao.testes.repository

import com.testeintegracao.testes.model.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository: JpaRepository<Role, Long> {
    fun findByNome(nome: String): Role
}
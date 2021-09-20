package com.testeintegracao.testes.repository

import com.testeintegracao.testes.model.Contato
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ContatoRepository : JpaRepository<Contato, Long>{
}
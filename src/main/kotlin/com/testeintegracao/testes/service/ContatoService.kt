package com.testeintegracao.testes.service

import com.testeintegracao.testes.model.Contato
import java.util.*

interface ContatoService {
    fun inserirOuAlterar(contato: Contato): Contato
    fun remover(id: Long)
    fun buscarContatos() : List<Contato>
    fun buscarContato(id: Long) : Contato
    fun findContatoById(id: Long) : Optional<Contato>
}
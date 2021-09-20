package com.testeintegracao.testes.service

import com.testeintegracao.testes.model.Contato

interface ContatoService {
    fun inserir(contato: Contato)
    fun remover(id: Long)
    fun buscarContatos() : List<Contato>
    fun buscarContato(id: Long) : Contato
}
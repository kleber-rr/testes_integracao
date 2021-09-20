package com.testeintegracao.testes.service

import com.testeintegracao.testes.model.Contato
import com.testeintegracao.testes.repository.ContatoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ContatoServiceImpl(
    @Autowired
    val repository: ContatoRepository
) : ContatoService {
    override fun inserirOuAlterar(contato: Contato): Contato {
        return repository.save(contato)
    }

    override fun remover(id: Long) {
        repository.deleteById(id)
    }

    override fun buscarContatos(): List<Contato> {
        return repository.findAll()
    }

    override fun buscarContato(id: Long): Contato {
        return  repository.getById(id)
    }

    override fun findContatoById(id: Long): Optional<Contato> {
        return  repository.findById(id)
    }
}
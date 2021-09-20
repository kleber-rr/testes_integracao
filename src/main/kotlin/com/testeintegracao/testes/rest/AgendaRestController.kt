package com.testeintegracao.testes.rest

import com.testeintegracao.testes.model.Contato
import com.testeintegracao.testes.service.ContatoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URISyntaxException
import javax.validation.Valid


@RestController
@RequestMapping("/agenda")
class AgendaRestController(
    @Autowired
    val service: ContatoService
) {

    @GetMapping("/")
    fun contatos(): ResponseEntity<List<Contato>> {
        var contatos = service.buscarContatos()
        return ResponseEntity.ok(contatos)
    }

    @GetMapping("/contato/{id}")
    fun contato(@PathVariable id: Long): ResponseEntity<Contato> {
        return service.findContatoById(id)
            .map { record -> ResponseEntity.ok().body(record) }
            .orElse(ResponseEntity.notFound().build())
    }

    @PostMapping("/inserir")
    fun inserir(@RequestBody @Valid contato: Contato): ResponseEntity<Contato> {
        var contatoSalvo: Contato = service.inserirOuAlterar(contato)
        return ResponseEntity(contatoSalvo, HttpStatus.CREATED)
    }

    @PutMapping("/alterar/{id}")
    fun alterar(@PathVariable("id") id: Long, @RequestBody @Valid contato: Contato): ResponseEntity<Contato> {
        contato.id = id
        var contatoSalvo = service.inserirOuAlterar(contato)
        return ResponseEntity(contatoSalvo, HttpStatus.CREATED)
    }

    @DeleteMapping("/remover/{id}")
    fun remover(@PathVariable("id") id: Long): ResponseEntity<Contato> {
        service.remover(id)
        return ResponseEntity.noContent().build()
    }
}
package com.testeintegracao.testes.controller

import com.testeintegracao.testes.domain.Const
import com.testeintegracao.testes.model.Usuario
import com.testeintegracao.testes.repository.RoleRepository
import com.testeintegracao.testes.repository.UsuarioRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UsuarioController(
    @Autowired val usuarioRepository: UsuarioRepository,
    @Autowired val roleRepository: RoleRepository,
    @Autowired val passwordEncoder: PasswordEncoder
    ) {

    @Secured("ROLE_ADMIN")
    @PostMapping
    fun save(@RequestBody usuario: Usuario): ResponseEntity<Usuario> {
        var user = usuarioRepository.save(usuario)
        return ResponseEntity<Usuario>(user, HttpStatus.CREATED)
    }

    @Secured("ROLE_ADMIN")
    @PutMapping
    fun edit(@RequestBody usuario: Usuario, @PathVariable id: Long): ResponseEntity<Usuario> {
        usuario.id = id
        usuarioRepository.save(usuario)
        return ResponseEntity<Usuario>(usuario, HttpStatus.OK)
    }

    @Secured(value = ["ROLE_CLIENT","ROLE_ADMIN"])
    @GetMapping
    fun list(@RequestParam("page") page: Int, @RequestParam("size") size: Int): ResponseEntity<Page<Usuario>> {
        var pageable = PageRequest.of(page, size, Sort.by("nome"))
        return ResponseEntity<Page<Usuario>>(usuarioRepository.findAll(pageable), HttpStatus.OK)
    }


}
package com.testeintegracao.testes.controller

import com.testeintegracao.testes.model.Usuario
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class SecurityController {

    @GetMapping("/user-auth")
    @ResponseBody
    @Secured(value = ["ROLE_CLIENT", "ROLE_ADMIN"])
    fun user(): Usuario {
        return SecurityContextHolder.getContext()
            .authentication
            .principal as Usuario
    }

}
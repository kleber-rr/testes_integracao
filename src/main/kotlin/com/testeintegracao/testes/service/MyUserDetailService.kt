package com.testeintegracao.testes.service

import com.testeintegracao.testes.model.Usuario
import com.testeintegracao.testes.repository.UsuarioRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class MyUserDetailService(
    @Autowired val usuarioRepository: UsuarioRepository
): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user: Usuario = usuarioRepository.findByEmail(username)
            ?: throw UsernameNotFoundException(java.lang.String.format("Usuário não existe!", username))
        return UserRepositoryUserDetails(user)
    }

    private class UserRepositoryUserDetails(user: Usuario) : Usuario(user), UserDetails {
        override fun getAuthorities(): Collection<GrantedAuthority?> {
            return roles
        }

        override fun getPassword(): String {
            return super.senha!!
        }

        override fun getUsername(): String {
            return email!!
        }

        override fun isAccountNonExpired(): Boolean {
            return true
        }

        override fun isAccountNonLocked(): Boolean {
            return true
        }

        override fun isCredentialsNonExpired(): Boolean {
            return true
        }

        override fun isEnabled(): Boolean {
            return true
        }

    }
}
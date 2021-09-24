package com.testeintegracao.testes.config

import com.testeintegracao.testes.service.MyUserDetailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore

@Configuration
class OAuth2ServerConfiguration {

    @Configuration
    @EnableResourceServer
    protected class ResourceServerConfiguration : ResourceServerConfigurerAdapter() {

        private val RESOURCE_ID = "restservice"

        override fun configure(resources: ResourceServerSecurityConfigurer) {
            resources
                .resourceId(RESOURCE_ID)
        }

        @Throws(Exception::class)
        override fun configure(http: HttpSecurity) {
            http
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .and().authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().fullyAuthenticated()
        }
    }

    @Configuration
    @EnableAuthorizationServer
    protected class AuthorizationServerConfiguration(
        @Autowired @Qualifier("authenticationManagerBean")
        private val authenticationManager: AuthenticationManager,

        @Autowired
        private val userDetailsService: MyUserDetailService,

        @Autowired
        private val passwordEncoder: PasswordEncoder
    ) : AuthorizationServerConfigurerAdapter() {

        private val RESOURCE_ID = "restservice"
        private val tokenStore: TokenStore = InMemoryTokenStore()

        @Throws(Exception::class)
        override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
            endpoints
                .tokenStore(tokenStore)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
        }

        @Throws(Exception::class)
        override fun configure(clients: ClientDetailsServiceConfigurer) {
            clients
                .inMemory()
                .withClient("client")
                .authorizedGrantTypes("password", "authorization_code", "refresh_token").scopes("all")
                .refreshTokenValiditySeconds(300000)
                .resourceIds(RESOURCE_ID)
                .secret(passwordEncoder!!.encode("123"))
                .accessTokenValiditySeconds(50000)
        }

        @Bean
        @Primary
        fun tokenServices(): DefaultTokenServices {
            val tokenServices = DefaultTokenServices()
            tokenServices.setSupportRefreshToken(true)
            tokenServices.setTokenStore(tokenStore)
            return tokenServices
        }
    }
}
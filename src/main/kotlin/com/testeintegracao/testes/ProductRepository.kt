package com.testeintegracao.testes

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProductRepository : JpaRepository<Product,Integer>{

    fun findByNameIgnoreCase(name: String) : Product

}
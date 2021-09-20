package com.testeintegracao.testes.repository

import com.testeintegracao.testes.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product,Integer>{

    fun findByNameIgnoreCase(name: String) : Product

}
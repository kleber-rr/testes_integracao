package com.testeintegracao.testes

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProductRepository : JpaRepository<Product,Integer>{

    fun findByNameIgnoreCase(name: String) : Product

//    @Query("select p from Product p order by p.price asc")
//    fun findAllOrderedByPriceAsc() : List<Product>
}
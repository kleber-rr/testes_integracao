package com.testeintegracao.testes.dao

import com.testeintegracao.testes.Product
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository

@Repository
class ProductDAO(
    template: JdbcTemplate
){

    var jdbcTemplate: JdbcTemplate = template

    fun findById(id: Integer) : Product? {
        return jdbcTemplate.queryForObject("SELECT id, name, price FROM product WHERE id = ?", ProductRowMapper(), id)
    }

    fun deleteById(id: Integer) {
        jdbcTemplate.update("DELETE FROM product where id = ?", id)
    }

    fun save(product: Product):Product {
        var simpleJdbcInsert = SimpleJdbcInsert(jdbcTemplate).withTableName("product").usingGeneratedKeyColumns("id")
        var id = simpleJdbcInsert.executeAndReturnKey(product.toMap()).toInt()
        product.id = id as Integer
        return product
    }
}
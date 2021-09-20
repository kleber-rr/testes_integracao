package com.testeintegracao.testes.dao

import com.testeintegracao.testes.model.Product
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class ProductRowMapper : RowMapper<Product> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Product? {
        return Product(rs.getInt("id") as? Integer, rs.getString("name"), rs.getInt("price"))
    }
}
package com.testeintegracao.testes

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.*
import kotlin.collections.HashMap

@Entity(name= "PRODUCT")
data class Product(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Integer?,

    @field:NotBlank(message = "Name is required")
    var name: String,

    @field:Positive(message = "Price must be greater than zero")
    @field:Max(value = 50, message = "Price must be letter or equals than fifty")
    var price: Int?
) {
    constructor() : this(null, "", null)
    constructor(name: String, price: Int) : this(null, name, price)

    fun toMap() : Map<String, Object> {
        var productAsMap = HashMap<String, Object>()
        productAsMap.put("name", name as Object)
        productAsMap.put("price", price as Object)
        return productAsMap
    }
}
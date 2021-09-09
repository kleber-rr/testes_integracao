package com.testeintegracao.testes

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.*

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
}
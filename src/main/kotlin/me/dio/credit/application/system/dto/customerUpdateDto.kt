package me.dio.credit.application.system.dto

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import me.dio.credit.application.system.entity.Customer
import java.math.BigDecimal

data class customerUpdateDto(
    @field:NotEmpty(message = "invalid option") val firstName: String,
    @field:NotEmpty(message = "invalid option") val lastName: String,
    @field:NotNull(message = "invalid option") val income: BigDecimal,
    @field:NotEmpty(message = "invalid option") val zipCode: String,
    @field:NotEmpty(message = "invalid option") val street: String
) {
    fun toEntity(customer: Customer) : Customer {
        customer.firstName = this.firstName
        customer.lastName = this.lastName
        customer.income = this.income
        customer.address.street = this.street
        customer.address.zipCode = this.zipCode
        return customer
    }
}

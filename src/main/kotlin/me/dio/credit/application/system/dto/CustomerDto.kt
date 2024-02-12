package me.dio.credit.application.system.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import me.dio.credit.application.system.entity.Address
import me.dio.credit.application.system.entity.Customer
import org.hibernate.validator.constraints.br.CPF
import java.math.BigDecimal

data class CustomerDto(
    @field:NotEmpty(message = "invalid option") val firstName: String,
    @field:NotEmpty(message = "invalid option") val lastName: String,
    @field:NotEmpty(message = "invalid option")
    @CPF(message = "Invalid CPF") val cpf: String,
    @field:NotNull(message = "invalid option") val income: BigDecimal,
    @field:Email(message = "invalid option")
    @field:NotEmpty(message = "invalid option") val email: String,
    @field:NotEmpty(message = "invalid option") val password: String,
    @field:NotEmpty(message = "invalid option") val zipCode: String,
    @field:NotEmpty(message = "invalid option") val street: String
) {
    fun toEntity(): Customer = Customer(
        firstName = this.firstName,
        lastName = this.lastName,
        cpf = this.cpf,
        email = this.email,
        income = this.income,
        password = this.password,
        address = Address(
            zipCode = this.zipCode,
            street = this.street
        )

    )
}

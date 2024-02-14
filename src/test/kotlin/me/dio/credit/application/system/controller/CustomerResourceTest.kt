package me.dio.credit.application.system.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.dio.credit.application.system.dto.CustomerDto
import me.dio.credit.application.system.dto.customerUpdateDto
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.repository.CustomerRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.web.servlet.function.RequestPredicates.accept
import org.springframework.web.servlet.function.RequestPredicates.contentType
import java.math.BigDecimal

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CustomerResourceTest {
    @Autowired private lateinit var customerRepository: CustomerRepository
    @Autowired private lateinit var mockMvc: MockMvc
    @Autowired private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL: String = "/api/customers"
    }
        @BeforeEach fun setup() = customerRepository.deleteAll()

        @AfterEach fun tearDown() = customerRepository.deleteAll()

    @Test
    fun `should create a customer and return 201 status` () {
        val customerDto: CustomerDto = builderCustomerDto()
        val valueAsString: String = objectMapper.writeValueAsString(customerDto)

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isCreated)
    }

    @Test
    fun `should not save a customer with same CPF and return 409 status`() {
        //given
        customerRepository.save(builderCustomerDto().toEntity())
        val customerDto: CustomerDto = builderCustomerDto()
        val valueAsString: String = objectMapper.writeValueAsString(customerDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
         .andExpect(MockMvcResultMatchers.status().isConflict)
         .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Conflict! Consult the documentation"))
         .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(409))
         .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.springframework.dao.DataIntegrityViolationException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not save a customer with firstName empty and return 400 status` () {
        val customerDto: CustomerDto = builderCustomerDto(firstName = "")
        val valueAsString: String = objectMapper.writeValueAsString(customerDto)

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
            .content(valueAsString)
            .contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    fun `should find customer by id and return 200 status` (){
        val customer: Customer = customerRepository.save(builderCustomerDto().toEntity())

        mockMvc.perform(MockMvcRequestBuilders.get("$URL/${customer.id}").accept(MediaType.APPLICATION_JSON))



    }

    @Test
    fun `should not find customer whith invalid id and return 400 status` () {
        val invalidId: Long = 2L
        mockMvc.perform(MockMvcRequestBuilders.get("$URL/${invalidId}").accept(MediaType.APPLICATION_JSON))

    }

    @Test
    fun `should not delete customer by id and return 400 status` () {
        val customer: Customer = customerRepository.save(builderCustomerDto().toEntity())
        mockMvc.perform(MockMvcRequestBuilders.get("$URL/${customer.id}").accept(MediaType.APPLICATION_JSON)
    }

    @Test
    fun `should update a customer and return 200` () {
        val customer: Customer = customerRepository.save(builderCustomerDto().toEntity())
        val customerUpdateDto: customerUpdateDto = builderCustomerUpdateDto()
        val valueAsString: String = objectMapper.writeValueAsString(customerUpdateDto)

        mockMvc.perform(
            MockMvcRequestBuilders.post("URL?customerId=${customer.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Conflict! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(409))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.springframework.dao.DataIntegrityViolationException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    private fun builderCustomerDto(
        firstName: String = "Gio",
        lastName: String = "santini",
        cpf: String = "46548978966",
        email: String = "gio@email.com",
        income: BigDecimal = BigDecimal.valueOf(1000.0),
        password: String = "4582",
        zipCode: String = "45566123",
        street: String = "Rua da giovanni, 123")
                = CustomerDto(
                firstName = firstName,
                lastName = lastName,
                cpf = cpf,
                email = email,
                income = income,
                password = password,
                zipCode = zipCode,
                street = street
            )
    private fun builderCustomerUpdateDto(
        firstName: String = "Gioupdate",
        lastName: String = "santiniupdate",
        income: BigDecimal = BigDecimal.valueOf(1000.0),

        zipCode: String = "45566123",
        street: String = "Rua up, 123"
    ): customerUpdateDto = customerUpdateDto(
        firstName = firstName,
        lastName = lastName,

        income = income,

        zipCode = zipCode,
        street = street
    )
}
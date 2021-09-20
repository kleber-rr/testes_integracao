package com.testeintegracao.testes.controller

import com.testeintegracao.testes.model.Contato
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Before
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import javax.transaction.Transactional

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
class AgendaControllerIntegrationTest(
    @Autowired
    val mockMvc: MockMvc,

    @Autowired
    val testEntityManager: TestEntityManager
) {
    lateinit var contato: Contato

    @BeforeEach
    @Throws(Exception::class)
    fun start(){
        contato = Contato("Chefe", "0y", "9xxxxxxxx9")
    }

    @Test
    @Throws(Exception::class)
    fun mustShowAllContacts(){
        testEntityManager.persist(contato)
        mockMvc.perform(get("/agendactrl/"))
            .andExpect(status().isOk)
            .andExpect(view().name("agendactrl"))
            .andExpect(model().attribute("contatos", Matchers.hasSize<Any>(1)))
            .andExpect(model().attribute("contatos", Matchers.hasItem<Any>(
                Matchers.allOf(
                    Matchers.hasProperty("id", Matchers.`is`(contato.id)),
                    Matchers.hasProperty("nome", Matchers.`is`(contato.nome)),
                    Matchers.hasProperty("ddd", Matchers.`is`(contato.ddd)),
                    Matchers.hasProperty("telefone", Matchers.`is`(contato.telefone)),
                )
            )))
            .andDo(print())
    }

    @Test
    @Throws(Exception::class)
    fun mustShowOneContact() {
        var id: Long = testEntityManager.persistAndGetId(contato) as Long
        mockMvc.perform(get("/agendactrl/contato/{id}", id))
            .andExpect(status().isOk)
            .andExpect(view().name("agendactrl/contato"))
            .andExpect(model().attribute("contato", Matchers.any(Contato::class.java)))
            .andExpect(model().attribute("contato", contato))
            .andDo(print())
    }

    @Test
    @Throws(Exception::class)
    fun shouldBeRemoveContact() {
        var id: Long = testEntityManager.persistAndGetId(contato) as Long
        mockMvc.perform(get("/agendactrl/remover/{id}", id))
            .andExpect(status().is3xxRedirection)
            .andExpect(view().name("redirect:agendactrl/"))
            .andExpect(flash().attribute("successMessage", "Contato removido com sucesso"))
            .andDo(print())

        var query = testEntityManager.entityManager.createQuery("FROM Contato", Contato::class.java)
        var resultList: List<Contato> = query.resultList as List<Contato>
        Assert.assertEquals(0, resultList.size)
    }

    @Test
    @Throws(Exception::class)
    fun insertWithDddNullShouldBeReturnAnError() {

        mockMvc.perform(post("/agendactrl/inserir")
                .param("telefone", contato.telefone)
                .param("nome", contato.nome)
            )
            .andExpect(status().isOk)
            .andExpect(view().name("agendactrl/inserir"))
            .andExpect(model().attribute("contato", Matchers.any(Contato::class.java)))
            .andExpect(model().attributeHasFieldErrors("contato", "ddd"))
            .andExpect(model().attributeHasFieldErrorCode("contato", "ddd", "NotBlank"))
            .andDo(print())
    }

    @Test
    @Throws(Exception::class)
    fun insertWithTelefoneNullShouldBeReturnAnError() {

        mockMvc.perform(post("/agendactrl/inserir")
            .param("ddd", contato.ddd)
            .param("nome", contato.nome)
        )
            .andExpect(status().isOk)
            .andExpect(view().name("agendactrl/inserir"))
            .andExpect(model().attribute("contato", Matchers.any(Contato::class.java)))
            .andExpect(model().attributeHasFieldErrors("contato", "telefone"))
            .andExpect(model().attributeHasFieldErrorCode("contato", "telefone", "NotBlank"))
            .andDo(print())
    }

    @Test
    @Throws(Exception::class)
    fun insertWithNomeNullShouldBeReturnAnError() {

        mockMvc.perform(post("/agendactrl/inserir")
            .param("ddd", contato.ddd)
            .param("telefone", contato.telefone)
        )
            .andExpect(status().isOk)
            .andExpect(view().name("agendactrl/inserir"))
            .andExpect(model().attribute("contato", Matchers.any(Contato::class.java)))
            .andExpect(model().attributeHasFieldErrors("contato", "nome"))
            .andExpect(model().attributeHasFieldErrorCode("contato", "nome", "NotBlank"))
            .andDo(print())
    }

    @Test
    @Throws(Exception::class)
    fun insertShouldBeSaveContact() {

        mockMvc.perform(post("/agendactrl/inserir")
            .param("ddd", contato.ddd)
            .param("nome", contato.nome)
            .param("telefone", contato.telefone)
        )
            .andExpect(status().isOk)
            .andExpect(view().name("agendactrl/inserir"))
            .andExpect(model().attribute("contato", Matchers.any(Contato::class.java)))
            .andExpect(model().attribute("successMessage", "Contato cadastrado com sucesso"))
            .andDo(print())

        var query = testEntityManager.entityManager.createQuery("FROM Contato", Contato::class.java)
        var resultList: List<Contato> = query.resultList
        Assert.assertEquals(1, resultList.size)
    }
}

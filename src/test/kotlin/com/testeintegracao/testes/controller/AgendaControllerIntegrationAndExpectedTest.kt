package com.testeintegracao.testes.controller

import com.testeintegracao.testes.model.Contato
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import javax.transaction.Transactional
import kotlin.jvm.Throws

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
class AgendaControllerIntegrationAndExpectedTest(
    @Autowired
    val mockMvc: MockMvc,

    @Autowired
    val testEntityManager: TestEntityManager
) {
    lateinit var contato: Contato

    @Before
    @Throws(Exception::class)
    fun start(){
        contato = Contato("Chefe", "0y", "9xxxxxxxx9")
        testEntityManager.persist(contato)
    }

    @After
    fun end(){
        testEntityManager.entityManager.createQuery("DELETE FROM Contato").executeUpdate()
    }

    @Test
    @Throws(Exception::class)
    fun checkStatus(){
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/agenda/"))
        var status = MockMvcResultMatchers.status()

        result.andExpect(status.isOk)
        result.andExpect(status.`is`(200))
        result.andExpect(status.`is`(Matchers.`is`(200)))
    }

    @Test
    @Throws(Exception::class)
    fun checkStatusWithDoAndReturn(){
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/agenda/"))
        result.andDo(MockMvcResultHandlers.print())

        var status = result.andReturn().response.status
        Assert.assertTrue(status == 200)

    }

    @Test
    @Throws(Exception::class)
    fun checkView() {
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/agenda/"))
        var view = MockMvcResultMatchers.view()

        result.andExpect(view.name("agenda"))
        result.andExpect(view.name(Matchers.`is`("agenda")))
    }

    @Test
    @Throws(Exception::class)
    fun checkViewWithDoAndReturn() {
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/agenda/"))
        result.andDo(MockMvcResultHandlers.print())

        var mav = result.andReturn().modelAndView
        Assert.assertEquals("agenda", mav!!.viewName)
    }

    @Test
    @Throws(Exception::class)
    fun checkModel() {
        contato = Contato("Chefe", "0y", "9xxxxxxxx9")
        testEntityManager.persist(contato)

        var result = mockMvc.perform(MockMvcRequestBuilders.get("/agenda/"))
        var model = MockMvcResultMatchers.model()

        result.andExpect(model.attributeExists("contatos"))
        result.andExpect(model.attribute("contatos", Matchers.hasSize<Any>(1)))
        result.andExpect(model.attribute("contatos",
            Matchers.hasItem<Any>(Matchers.allOf(
                Matchers.hasProperty("id", Matchers.`is`(contato.id)),
                Matchers.hasProperty("nome", Matchers.`is`(contato.nome)),
                Matchers.hasProperty("ddd", Matchers.`is`(contato.ddd)),
                Matchers.hasProperty("telefone", Matchers.`is`(contato.telefone)),
            ))))
    }

    @Test
    @Throws(Exception::class)
    fun checkModelWithDoAndReturn() {
        contato = Contato("Chefe", "0y", "9xxxxxxxx9")
        testEntityManager.persist(contato)

        var result = mockMvc.perform(MockMvcRequestBuilders.get("/agenda/"))
        result.andDo(MockMvcResultHandlers.print())

        var mav = result.andReturn().modelAndView

        var contatos: List<Contato> = mav!!.model["contatos"] as List<Contato>

        Assert.assertEquals(1, contatos.size)
        Assert.assertTrue(contatos.contains(contato))
    }
}

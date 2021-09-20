package com.testeintegracao.testes.rest

import com.testeintegracao.testes.model.Contato
import com.testeintegracao.testes.repository.ContatoRepository
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.test.context.junit4.SpringRunner
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AgendaRestControllerTest(
    @Autowired
    val testRestTemplate: TestRestTemplate,

    @Autowired
    val repository: ContatoRepository
) {

    lateinit var contato: Contato
    val nome = "Chefe"
    val ddd = "0y"
    val telefone = "9xxxxxxx9"

    @BeforeEach
    fun start() {
        contato = Contato(ddd, telefone, nome)
        contato = repository.save(contato)
    }

    @AfterEach
    fun end() {
        repository.deleteAll()
    }

    @Test
    fun deveMostrarTodosContatos(){
        var resposta: ResponseEntity<String> = testRestTemplate.exchange("/agenda/", HttpMethod.GET, null, String::class)
        Assert.assertEquals(HttpStatus.OK, resposta.statusCode)
    }

    @Test
    fun deveMostrarTodosContatosUsandoString(){
        var resposta: ResponseEntity<String> = testRestTemplate.exchange("/agenda/", HttpMethod.GET, null, String::class)
        Assert.assertEquals(HttpStatus.OK, resposta.statusCode)
        Assert.assertTrue(resposta.headers.contentType!! == MediaType.parseMediaType("application/json"))

        var resultStr = "[{\"id\":"+contato.id+",\"ddd\":\"0y\","+"\"telefone\":\"9xxxxxxx9\",\"nome\":\"Chefe\"}]"
        Assert.assertEquals(resultStr, resposta.body)
    }

    @Test
    fun deveMostrarTodosContatosUsandoList(){

        var typeReference = object : ParameterizedTypeReference<List<Contato>>() {}
        var resposta: ResponseEntity<List<Contato>> = testRestTemplate.exchange("/agenda/", HttpMethod.GET,
            null, typeReference)

        Assert.assertEquals(HttpStatus.OK, resposta.statusCode)
        Assert.assertTrue(resposta.headers.contentType!! == MediaType.parseMediaType("application/json"))

        Assert.assertEquals(1, resposta.body!!.size)
        Assert.assertEquals(contato, resposta.body!![0])
    }

    @Test
    fun deveMostrarUmContato(){
        var resposta: ResponseEntity<Contato> = testRestTemplate.exchange("/agenda/contato/{id}", HttpMethod.GET,
            null, contato!!.id!!
        )
        Assert.assertEquals(HttpStatus.OK, resposta.statusCode)
        Assert.assertTrue(resposta.headers.contentType!! == MediaType.parseMediaType("application/json"))

        Assert.assertEquals(contato, resposta.body)
    }

    @Test
    fun buscarUmContatoDeveRetornarNaoEncontrado(){
        var resposta: ResponseEntity<Contato> = testRestTemplate.exchange("/agenda/contato/{id}", HttpMethod.GET,
            null, 100
        )
        Assert.assertEquals(HttpStatus.NOT_FOUND, resposta.statusCode)
        Assert.assertNull(resposta.body)
    }

    @Test
    fun deveMostrarUmContatoComGetForEntity(){
        var resposta: ResponseEntity<Contato> = testRestTemplate.getForEntity("/agenda/contato/{id}",
            Contato::class.java, contato!!.id!!
        )
        Assert.assertEquals(HttpStatus.OK, resposta.statusCode)
        Assert.assertTrue(resposta.headers.contentType!! == MediaType.parseMediaType("application/json"))

        Assert.assertEquals(contato, resposta.body)
    }

    @Test
    fun buscarUmContatoDeveRetornarNaoEncontradoComGetForEntity(){
        var resposta: ResponseEntity<Contato> = testRestTemplate.getForEntity("/agenda/contato/{id}",
            Contato::class.java, 100
        )
        Assert.assertEquals(HttpStatus.NOT_FOUND, resposta.statusCode)
        Assert.assertNull(resposta.body)
    }

    @Test
    fun buscarUmContatoDeveRetornarNaoEncontradoComGetForObject(){
        var resposta: Contato? = testRestTemplate.getForObject("/agenda/contato/{id}",
            Contato::class.java, 100
        )
        Assert.assertNull(resposta)
    }

    @Test
    fun salvarContatoDeveRetornarMensagemDeErro(){
        var contatoFail = Contato(null, null, nome)
        var httpEntity = HttpEntity<Contato>(contatoFail)
        var typeReference = object : ParameterizedTypeReference<List<String>>() {}

        var resposta: ResponseEntity<List<String>> = testRestTemplate.exchange("/agenda/inserir", HttpMethod.POST,
            httpEntity, typeReference)

        Assert.assertEquals(HttpStatus.BAD_REQUEST, resposta.statusCode)
        Assert.assertTrue(resposta.body!!.contains("O DDD deve ser preenchido"))
        Assert.assertTrue(resposta.body!!.contains("O telefone deve ser preenchido"))
    }

    @Test
    fun deveSalvarContato(){
        var contatoNOFail = Contato(ddd, telefone, nome)
        var httpEntity = HttpEntity<Contato>(contatoNOFail)

        var resposta: ResponseEntity<Contato> = testRestTemplate.exchange("/agenda/inserir", HttpMethod.POST,
            httpEntity, Contato::class.java)

        Assert.assertEquals(HttpStatus.CREATED, resposta.statusCode)

        var resultado = resposta.body

        Assert.assertNotNull(resultado!!.id)
        Assert.assertEquals(contato.nome, resultado.nome)
        Assert.assertEquals(contato.ddd, resultado.ddd)
        Assert.assertEquals(contato.telefone, resultado.telefone)
        repository.deleteAll()
    }

    @Test
    fun deveSalvarContatoComPostForEntity(){
        var contatoNOFail = Contato(ddd, telefone, nome)
        var httpEntity = HttpEntity<Contato>(contatoNOFail)

        var resposta: ResponseEntity<Contato> = testRestTemplate.postForEntity("/agenda/inserir",
            httpEntity, Contato::class.java)

        Assert.assertEquals(HttpStatus.CREATED, resposta.statusCode)

        var resultado = resposta.body

        Assert.assertNotNull(resultado!!.id)
        Assert.assertEquals(contato.nome, resultado.nome)
        Assert.assertEquals(contato.ddd, resultado.ddd)
        Assert.assertEquals(contato.telefone, resultado.telefone)
        repository.deleteAll()
    }

    @Test
    fun deveSalvarContatoComPostForObject(){
        var contatoNOFail = Contato(ddd, telefone, nome)
        var httpEntity = HttpEntity<Contato>(contatoNOFail)

        var resultado: Contato = testRestTemplate.postForObject("/agenda/inserir",
            httpEntity, Contato::class.java)

        Assert.assertNotNull(resultado!!.id)
        Assert.assertEquals(contato.nome, resultado.nome)
        Assert.assertEquals(contato.ddd, resultado.ddd)
        Assert.assertEquals(contato.telefone, resultado.telefone)
        repository.deleteAll()
    }

    @Test
    fun alterarDeveRetornarMensagemDeErro(){
        contato.ddd = null
        contato.telefone = null
        var httpEntity = HttpEntity<Contato>(contato)
        var typeReference = object : ParameterizedTypeReference<List<String>>() {}

        var resposta: ResponseEntity<List<String>> = testRestTemplate.exchange("/agenda/alterar/{id}", HttpMethod.PUT,
            httpEntity, typeReference, contato!!.id!!)

        Assert.assertEquals(HttpStatus.BAD_REQUEST, resposta.statusCode)
        Assert.assertTrue(resposta.body!!.contains("O DDD deve ser preenchido"))
        Assert.assertTrue(resposta.body!!.contains("O telefone deve ser preenchido"))
    }

    @Test
    fun alterarDeveRetornarContato(){
        contato.nome = "Novo chefe"
        var httpEntity = HttpEntity<Contato>(contato)

        var resposta: ResponseEntity<Contato> = testRestTemplate.exchange("/agenda/alterar/{id}", HttpMethod.PUT,
            httpEntity, contato!!.id!!)

        Assert.assertEquals(HttpStatus.CREATED, resposta.statusCode)

        var resultado = resposta.body

        Assert.assertEquals(contato.id, resultado!!.id)
        Assert.assertEquals(contato.nome, resultado!!.nome)
        Assert.assertEquals(contato.ddd, resultado!!.ddd)
        Assert.assertEquals(contato.telefone, resultado!!.telefone)
    }

    @Test
    fun alterarDeveRetornarContatoComPut(){
        contato.nome = "Novo chefe"
        var httpEntity = HttpEntity<Contato>(contato)

        testRestTemplate.put("/agenda/alterar/{id}", httpEntity, contato!!.id!!)

        var resultado = repository.findById(contato.id!!).get()

        Assert.assertEquals(contato.ddd, resultado!!.ddd)
        Assert.assertEquals(contato.telefone, resultado!!.telefone)
        Assert.assertEquals("Novo chefe", resultado!!.nome)
    }

    @Test
    fun alterarDeveRetornarContatoComDelete(){
        testRestTemplate.delete("/agenda/remover/"+ contato!!.id!!)

        var resultado: Optional<Contato> = repository.findById(contato.id!!)

        Assert.assertTrue(resultado.isEmpty)
    }
}

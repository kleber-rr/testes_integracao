package com.testeintegracao.testes

import com.testeintegracao.testes.model.Contato
import com.testeintegracao.testes.repository.ContatoRepository
import org.hibernate.envers.AuditReader
import org.hibernate.envers.AuditReaderFactory
import org.hibernate.envers.RevisionType
import org.hibernate.envers.query.AuditEntity
import org.hibernate.envers.query.AuditQuery
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDate
import java.util.*
import javax.persistence.EntityManagerFactory

@RunWith(SpringRunner::class)
@SpringBootTest
class ContatosRepositoryIntegrationTest(
    @Autowired
    val repository: ContatoRepository,

    @Autowired
    val entityManager: EntityManagerFactory
) {
    lateinit var contato: Contato

    @BeforeEach
    fun start(){
        contato = Contato("0y","9xxxxxx9", "Chefe")
    }

    @Test
    fun auditoriaAposSalvarRetornaObjetoCorreto(){
        repository.save(contato)

        var reader: AuditReader = AuditReaderFactory.get(entityManager.createEntityManager())
        var contatoVindoDaAuditoria: Contato = reader.find(
            Contato::class.java,
            contato.id,
            Date()
        )

        Assert.assertTrue("Chefe" == contatoVindoDaAuditoria.nome)
        Assert.assertEquals(contato, contatoVindoDaAuditoria)
    }

    @Test
    fun auditoriaAposSalvarRetornaObjetoCorretoComAuditQuery(){
        repository.save(contato)

        var reader: AuditReader = AuditReaderFactory.get(entityManager.createEntityManager())

        var query: AuditQuery = reader.createQuery()
            .forRevisionsOfEntity(Contato::class.java, true, true)
            .add(AuditEntity.property("id").eq(contato.id))
            .add(AuditEntity.revisionType().eq(RevisionType.ADD))

        var contatoVindoDaAuditoria: Contato = query.singleResult as Contato

        Assert.assertTrue("Chefe" == contatoVindoDaAuditoria.nome)
        Assert.assertEquals(contato, contatoVindoDaAuditoria)
    }

    @Test
    fun auditoriaAposEdicaoRetornaObjetoCorreto(){
        repository.save(contato)

        contato.nome = "Novo Chefe"
        repository.save(contato)

        var reader: AuditReader = AuditReaderFactory.get(entityManager.createEntityManager())
        var contatoVindoDaAuditoria: Contato = reader.find(
            Contato::class.java,
            contato.id,
            Date()
        )

        Assert.assertTrue("Novo Chefe" == contatoVindoDaAuditoria.nome)
    }

    @Test
    fun auditoriaAposEdicaoRetornaObjetoCorretoComAuditQuery(){
        repository.save(contato)
        contato.nome = "Novo Chefe"
        repository.save(contato)

        var reader: AuditReader = AuditReaderFactory.get(entityManager.createEntityManager())

        var query: AuditQuery = reader.createQuery()
            .forRevisionsOfEntity(Contato::class.java, true, true)
            .add(AuditEntity.property("id").eq(contato.id))
            .add(AuditEntity.revisionType().eq(RevisionType.MOD))

        var contatoVindoDaAuditoria: Contato = query.singleResult as Contato

        Assert.assertTrue("Novo Chefe" == contatoVindoDaAuditoria.nome)
    }

    @Test
    fun auditoriaAposEclusaoRetornaObjetoCorreto(){
        repository.save(contato)
        repository.delete(contato)

        var reader: AuditReader = AuditReaderFactory.get(entityManager.createEntityManager())

        var numerosDasRevisoes: List<Number> = reader.getRevisions(Contato::class.java, contato.id)
        var number: Number = numerosDasRevisoes.last()

        var contatoVindoDaAuditoria: Contato = reader.find(
            Contato::class.java,
            Contato::class.java.name,
            contato.id,
            number,
            true
        )

        Assert.assertEquals(contato.id, contatoVindoDaAuditoria.id)
        Assert.assertNull(contatoVindoDaAuditoria.nome)
    }

    @Test
    fun auditoriaAposRemocaoRetornaObjetoCorretoComAuditQuery(){
        repository.save(contato)
        repository.delete(contato)

        var reader: AuditReader = AuditReaderFactory.get(entityManager.createEntityManager())

        var query: AuditQuery = reader.createQuery()
            .forRevisionsOfEntity(Contato::class.java, true, true)
            .add(AuditEntity.property("id").eq(contato.id))
            .add(AuditEntity.revisionType().eq(RevisionType.DEL))

        var contatoVindoDaAuditoria: Contato = query.singleResult as Contato

        Assert.assertEquals(contato.id, contatoVindoDaAuditoria.id)
        Assert.assertNull(contatoVindoDaAuditoria.nome)
    }
}
package br.com.zup.pix.delecao

import br.com.zup.KeymanagerDeletaGrpcServiceGrpc.KeymanagerDeletaGrpcServiceBlockingStub
import br.com.zup.compartilhado.GrpcClientFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class DeletaChaveControllerTest {

    @Inject
    lateinit var grpcStub: KeymanagerDeletaGrpcServiceBlockingStub

    @Inject
    @field:Client("/")
    lateinit var restClient: HttpClient

    companion object {
        val CLIENTE_ID = UUID.randomUUID().toString()
        val PIX_ID = UUID.randomUUID().toString()
        val DELETA_PIX_URL = "/api/clientes/$CLIENTE_ID/pix/$PIX_ID"
    }

    @Test
    internal fun `deve deletar chave existente`() {

        val request = HttpRequest.DELETE<Void>(DELETA_PIX_URL)
        val response = restClient.toBlocking().exchange<Void, Void>(request)

        assertEquals(HttpStatus.NO_CONTENT, response.status)

    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class MockitoStubFactory {
        @Singleton
        fun grpcClient() = mock(KeymanagerDeletaGrpcServiceBlockingStub::class.java)
    }

}
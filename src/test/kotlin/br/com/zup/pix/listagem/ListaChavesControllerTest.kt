package br.com.zup.pix.listagem

import br.com.zup.*
import br.com.zup.KeymanagerListaGrpcServiceGrpc.KeymanagerListaGrpcServiceBlockingStub
import br.com.zup.compartilhado.GrpcClientFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ListaChavesControllerTest {

    @Inject
    lateinit var grpcClient: KeymanagerListaGrpcServiceBlockingStub

    @Inject
    @field:Client("/")
    lateinit var restClient: HttpClient

    @Test
    internal fun `deve listar chaves do usuario`() {

        val clienteId = UUID.randomUUID().toString()

        val grpcResponse = ListaPixResponse
            .newBuilder()
            .addChaves(ListaPixResponse.itemChave
                .newBuilder()
                .setPixId(UUID.randomUUID().toString())
                .setClienteId(clienteId)
                .setTipoDeChave(TipoDeChave.EMAIL)
                .setTipoDeConta(TipoDeConta.CONTA_CORRENTE)
                .setValor("email@gmail.com")
                .setCriadaEm(Timestamp.newBuilder()
                    .setSeconds(LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant().epochSecond)
                    .setNanos(LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant().nano)
                ).build()
            ).build()

        `when`(grpcClient.lista(ListaPixRequest
            .newBuilder()
            .setClienteId(clienteId)
            .build())
        ).thenReturn(grpcResponse)

        val request = HttpRequest.GET<Any>("/api/clientes/$clienteId/pix")
        val response = restClient.toBlocking().exchange(request, ListagemChavesResponse::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
        assertEquals(1, response.body()!!.chaves.size)

    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    class MockStubFactory {
        @Singleton
        fun grcpClient() = mock(KeymanagerListaGrpcServiceBlockingStub::class.java)
    }

}
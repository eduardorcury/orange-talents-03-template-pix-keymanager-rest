package br.com.zup.pix.detalhe

import br.com.zup.*
import br.com.zup.KeymanagerConsultaGrpcServiceGrpc.KeymanagerConsultaGrpcServiceBlockingStub
import br.com.zup.compartilhado.GrpcClientFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class DetalhaChaveControllerTest {

    @Inject
    @field:Client("/")
    lateinit var restClient: HttpClient

    @Inject
    lateinit var grpcClient: KeymanagerConsultaGrpcServiceBlockingStub

    @Test
    internal fun `deve retornar detalhes de chave pix`() {

        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()
        val uri = "/api/clientes/$clienteId/pix/$pixId"

        val consultaGrpc = ConsultaPixRequest
            .newBuilder()
            .setInterna(ConsultaPixRequest.ConsultaInterna
                .newBuilder()
                .setClienteId(clienteId)
                .setPixId(pixId)
                .build())
            .build()

        val respostaGrpc = ConsultaPixResponse
            .newBuilder()
            .setPixId(pixId)
            .setClienteId(clienteId)
            .setTipoDeChave(TipoDeChave.EMAIL)
            .setValor("email@gmail.com")
            .setTitular(br.com.zup.Titular
                .newBuilder()
                .setCpf("12345")
                .setNome("Eduardo")
                .build())
            .setConta(br.com.zup.Conta
                .newBuilder()
                .setInstituicao("ITAÚ")
                .setNumero("123")
                .setAgencia("0001")
                .setTipo(TipoDeConta.CONTA_CORRENTE)
                .build())
            .setCriadaEm(Timestamp.newBuilder()
                .setSeconds(LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant().epochSecond)
                .setNanos(LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant().nano)
            )
            .build()

        `when`(grpcClient.consulta(consultaGrpc)).thenReturn(respostaGrpc)

        val request = HttpRequest.GET<Void>(uri)
        val response: HttpResponse<DetalhesChavePix> =
            restClient.toBlocking().exchange(request, DetalhesChavePix::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())

    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class MockitoStubFactory {
        @Singleton
        fun grpcClient() = mock(KeymanagerConsultaGrpcServiceBlockingStub::class.java)
    }

}
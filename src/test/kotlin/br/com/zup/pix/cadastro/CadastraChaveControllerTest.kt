package br.com.zup.pix.cadastro

import br.com.zup.CadastroPixResponse
import br.com.zup.KeymanagerCadastraGrpcServiceGrpc.KeymanagerCadastraGrpcServiceBlockingStub
import br.com.zup.compartilhado.GrpcClientFactory
import br.com.zup.pix.enums.TipoDeChave
import br.com.zup.pix.enums.TipoDeConta
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.hateoas.JsonError
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito.*
import java.util.*
import java.util.stream.Stream
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class CadastraChaveControllerTest() {

    @Inject
    lateinit var grpcClient: KeymanagerCadastraGrpcServiceBlockingStub

    @Inject
    @field:Client("/")
    lateinit var restClient: HttpClient

    companion object {

        val CLIENTE_ID = UUID.randomUUID().toString()
        val CADASTRO_PIX_URL = "/api/clientes/$CLIENTE_ID/pix"

        @JvmStatic
        fun geraRequestsInvalidos(): Stream<NovaChaveRequest> {
            return Stream.of(
                NovaChaveRequest(null, null, null),
                NovaChaveRequest(TipoDeChave.EMAIL, null, null),
                NovaChaveRequest(null, null, TipoDeConta.CONTA_POUPANCA),
                NovaChaveRequest(TipoDeChave.EMAIL, "email", TipoDeConta.CONTA_POUPANCA),
                NovaChaveRequest(TipoDeChave.EMAIL, "email@gmail.com", null),
                NovaChaveRequest(TipoDeChave.TELEFONE, "123", TipoDeConta.CONTA_POUPANCA),
                NovaChaveRequest(TipoDeChave.CPF, "123", TipoDeConta.CONTA_POUPANCA),
                NovaChaveRequest(TipoDeChave.ALEATORIA, null, null),
            )
        }
    }

    @Test
    internal fun `deve retornar CREATED e header location ao cadastrar nova chave`() {

        val novaChaveRequest = NovaChaveRequest(
            tipoDeChave = TipoDeChave.EMAIL,
            valor = "email@gmail.com",
            tipoDeConta = TipoDeConta.CONTA_CORRENTE
        )

        val grpcResponse = CadastroPixResponse
            .newBuilder()
            .setPixId(UUID.randomUUID().toString())
            .build()

        `when`(grpcClient.cadastro(novaChaveRequest.toGrpcRequest(CLIENTE_ID)))
            .thenReturn(grpcResponse)

        val request = HttpRequest.POST(CADASTRO_PIX_URL, novaChaveRequest)
        val response = restClient.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertEquals("$CADASTRO_PIX_URL/${grpcResponse.pixId}", response.header("location"))

    }

    @ParameterizedTest
    @MethodSource("geraRequestsInvalidos")
    internal fun `deve retornar BAD_REQUEST para request com dados invalidos`(
        requestInvalido: NovaChaveRequest
    ) {

        val request = HttpRequest.POST(CADASTRO_PIX_URL, requestInvalido)

        val response = assertThrows<HttpClientResponseException> {
            restClient.toBlocking().exchange(request, Any::class.java)
        }

        with(response) {
            assertEquals(HttpStatus.BAD_REQUEST, status)
        }

    }

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class MockitoStubFactory {
        @Singleton
        fun grcpClient() = mock(KeymanagerCadastraGrpcServiceBlockingStub::class.java)
    }

}
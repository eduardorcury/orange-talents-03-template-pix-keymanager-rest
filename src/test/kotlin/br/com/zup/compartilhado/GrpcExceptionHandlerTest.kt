package br.com.zup.compartilhado

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class GrpcExceptionHandlerTest {

    @ParameterizedTest
    @MethodSource("geraErrosGRPC")
    internal fun `deve traduzir erros gRPC corretamente`(
        exception: StatusRuntimeException, statusEsperado: HttpStatus,
    ) {

        val handler = GrpcExceptionHandler()
        val response = handler.handle(HttpRequest.GET<Any>("/api"), exception)

        assertEquals(statusEsperado, response.status)
        assertEquals(exception.status.description, response.body().toString())

    }

    companion object {
        @JvmStatic
        fun geraErrosGRPC(): Stream<Arguments> = Stream.of(
            Arguments.of(
                StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription("Erro de validação")),
                HttpStatus.BAD_REQUEST),
            Arguments.of(
                StatusRuntimeException(Status.NOT_FOUND.withDescription("Recurso não encontrado")),
                HttpStatus.NOT_FOUND),
            Arguments.of(StatusRuntimeException(Status.PERMISSION_DENIED.withDescription("Não autorizado")),
                HttpStatus.FORBIDDEN),
            Arguments.of(StatusRuntimeException(Status.ALREADY_EXISTS.withDescription("Já existente")),
                HttpStatus.UNPROCESSABLE_ENTITY),
            Arguments.of(StatusRuntimeException(Status.UNKNOWN.withDescription("Erro inesperado no servidor")),
                HttpStatus.INTERNAL_SERVER_ERROR),
        )
    }

}

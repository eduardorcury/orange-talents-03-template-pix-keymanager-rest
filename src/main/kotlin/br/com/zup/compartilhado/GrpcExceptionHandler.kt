package br.com.zup.compartilhado

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.JsonError
import io.micronaut.http.server.exceptions.ExceptionHandler
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class GrpcExceptionHandler : ExceptionHandler<StatusRuntimeException, HttpResponse<Any>> {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    override fun handle(request: HttpRequest<*>, exception: StatusRuntimeException): HttpResponse<Any> {

        val description = exception.status.description

        val (httpStatus, message) = when(exception.status.code) {
            Status.Code.INVALID_ARGUMENT -> Pair(HttpStatus.BAD_REQUEST, description)
            Status.Code.NOT_FOUND -> Pair(HttpStatus.NOT_FOUND, description)
            Status.Code.PERMISSION_DENIED -> Pair(HttpStatus.FORBIDDEN, description)
            Status.Code.ALREADY_EXISTS -> Pair(HttpStatus.UNPROCESSABLE_ENTITY, description)
            else -> {
                LOGGER.error("Server gRPC retornou erro inesperado com mensagem $description")
                Pair(HttpStatus.INTERNAL_SERVER_ERROR, "Erro inesperado no servidor")
            }
        }

        return HttpResponse.status<JsonError>(httpStatus).body(JsonError(message))

    }
}
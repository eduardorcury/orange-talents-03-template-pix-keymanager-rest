package br.com.zup.compartilhado

import io.grpc.Status.Code.*
import io.grpc.Status.Code.NOT_FOUND
import io.grpc.StatusRuntimeException
import io.micronaut.aop.InterceptorBean
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import io.micronaut.http.HttpStatus
import io.micronaut.http.HttpStatus.*
import io.micronaut.http.exceptions.HttpStatusException
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
@InterceptorBean(GrpcErrorReceiver::class)
class GrpcExceptionInterceptor : MethodInterceptor<Any, Any?> {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    override fun intercept(context: MethodInvocationContext<Any, Any?>): Any? {
        try{
            return context.proceed()
        } catch (exception: StatusRuntimeException) {
            val description = exception.status.description
            LOGGER.error("Server gRPC retornou erro de status ${exception.status} " +
                    "com mensagem $description")
            when(exception.status.code) {
                INVALID_ARGUMENT -> throw HttpStatusException(BAD_REQUEST, description)
                NOT_FOUND -> throw HttpStatusException(HttpStatus.NOT_FOUND, description)
                PERMISSION_DENIED -> throw HttpStatusException(FORBIDDEN, description)
                ALREADY_EXISTS -> throw HttpStatusException(UNPROCESSABLE_ENTITY, description)
                else -> throw HttpStatusException(INTERNAL_SERVER_ERROR, description)
            }
        }
    }
}
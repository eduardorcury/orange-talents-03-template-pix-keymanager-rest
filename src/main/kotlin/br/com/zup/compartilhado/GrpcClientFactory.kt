package br.com.zup.compartilhado

import br.com.zup.KeymanagerCadastraGrpcServiceGrpc
import br.com.zup.KeymanagerConsultaGrpcServiceGrpc
import br.com.zup.KeymanagerDeletaGrpcServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClientFactory(
    @GrpcChannel("keymanager") val channel: ManagedChannel
) {

    @Singleton
    fun cadastraGrpcStub(): KeymanagerCadastraGrpcServiceGrpc.KeymanagerCadastraGrpcServiceBlockingStub =
        KeymanagerCadastraGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun deletaGrpcStub(): KeymanagerDeletaGrpcServiceGrpc.KeymanagerDeletaGrpcServiceBlockingStub =
        KeymanagerDeletaGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun detalhaGrpcStub(): KeymanagerConsultaGrpcServiceGrpc.KeymanagerConsultaGrpcServiceBlockingStub =
        KeymanagerConsultaGrpcServiceGrpc.newBlockingStub(channel)

}
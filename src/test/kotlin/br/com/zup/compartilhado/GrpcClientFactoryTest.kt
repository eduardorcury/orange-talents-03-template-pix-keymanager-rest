package br.com.zup.compartilhado

import io.grpc.ManagedChannelBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class GrpcClientFactoryTest {

    val channel = ManagedChannelBuilder
        .forAddress("localhost", 50051)
        .build()

    val factory = GrpcClientFactory(channel)

    @Test
    internal fun `deve criar stub grpc de cadastro`() {
        val grpcStun = factory.cadastraGrpcStub()
        assertEquals("localhost:50051", grpcStun.channel.authority())
    }

    @Test
    internal fun `deve criar stub grpc de delecao`() {
        val grpcStun = factory.deletaGrpcStub()
        assertEquals("localhost:50051", grpcStun.channel.authority())
    }

    @Test
    internal fun `deve criar stub grpc de detalhe`() {
        val grpcStun = factory.detalhaGrpcStub()
        assertEquals("localhost:50051", grpcStun.channel.authority())
    }

}
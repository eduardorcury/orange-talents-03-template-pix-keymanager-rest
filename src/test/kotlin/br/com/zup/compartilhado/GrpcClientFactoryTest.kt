package br.com.zup.compartilhado

import io.grpc.ManagedChannelBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class GrpcClientFactoryTest {

    @Test
    internal fun `deve criar stub grpc`() {

        val factory = GrpcClientFactory()
        val grpcStun = factory.keymanagerGrpcStun(ManagedChannelBuilder
            .forAddress("localhost", 50051)
            .build())

        assertEquals("localhost:50051", grpcStun.channel.authority())

    }
}
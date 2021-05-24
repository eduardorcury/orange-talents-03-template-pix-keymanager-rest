package br.com.zup.pix.delecao

import br.com.zup.DeletaPixRequest
import br.com.zup.KeymanagerDeletaGrpcServiceGrpc.KeymanagerDeletaGrpcServiceBlockingStub
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import org.slf4j.LoggerFactory
import javax.inject.Inject

@Controller("/api/clientes/{clienteId}/pix")
class DeletaChaveController(
    @field:Inject val grpcClient: KeymanagerDeletaGrpcServiceBlockingStub
) {

    private val LOGGER = LoggerFactory.getLogger(DeletaChaveController::class.java)

    @Delete("/{pixId}")
    fun deleta(@PathVariable clienteId: String, @PathVariable pixId: String) : HttpResponse<Any> {

        grpcClient.deleta(DeletaPixRequest
            .newBuilder()
            .setClienteId(clienteId)
            .setPixId(pixId)
            .build())

        LOGGER.info("Chave de id $pixId deletada")

        return HttpResponse.noContent()

    }

}
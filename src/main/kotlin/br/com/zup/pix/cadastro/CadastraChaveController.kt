package br.com.zup.pix.cadastro

import br.com.zup.KeymanagerCadastraGrpcServiceGrpc.KeymanagerCadastraGrpcServiceBlockingStub
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType.APPLICATION_JSON
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import org.slf4j.LoggerFactory
import java.net.URI
import javax.inject.Inject
import javax.validation.Valid

@Controller("/api/clientes/{clienteId}/pix")
class CadastraChaveController(
    @field:Inject val grpcClient: KeymanagerCadastraGrpcServiceBlockingStub,
) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Post(consumes = [APPLICATION_JSON], produces = [APPLICATION_JSON])
    fun cadastra(@PathVariable clienteId: String,
                 @Body @Valid request: NovaChaveRequest): HttpResponse<Any> {

        val response = grpcClient
            .cadastro(request.toGrpcRequest(clienteId))
            .also { LOGGER.info("Chave de id ${it.pixId} criada pelo request $request") }

        return HttpResponse.created(location(clienteId, response.pixId))

    }

    val location: (String, String) -> (URI) = { cliente, pixId ->
        HttpResponse.uri("/api/clientes/$cliente/pix/$pixId")
    }

}
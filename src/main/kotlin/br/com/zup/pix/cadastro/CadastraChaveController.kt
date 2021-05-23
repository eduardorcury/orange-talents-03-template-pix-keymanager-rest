package br.com.zup.pix.cadastro

import br.com.zup.CadastroPixRequest
import br.com.zup.KeymanagerCadastraGrpcServiceGrpc.KeymanagerCadastraGrpcServiceBlockingStub
import br.com.zup.compartilhado.GrpcErrorReceiver
import io.grpc.Status.Code.INVALID_ARGUMENT
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType.APPLICATION_JSON
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.validation.Valid

@Controller
@GrpcErrorReceiver
class CadastraChaveController(
    @field:Inject val grpcClient: KeymanagerCadastraGrpcServiceBlockingStub,
) {

    @Post(value = "/api/chaves",
        consumes = [APPLICATION_JSON])
    fun cadastra(@Body @Valid request: NovaChaveRequest): HttpResponse<Any> {

        val response = grpcClient.cadastro(CadastroPixRequest.newBuilder()
            .setIdTitular(request.clienteId)
            .setTipoDeChave(request.tipoDeChave)
            .setValor(request.valor)
            .setTipoDeConta(request.tipoDeConta)
            .build())
        return HttpResponse.created(NovaChaveResponse(response.pixId))

    }

}

data class NovaChaveResponse(
    val pixId: String,
)
package br.com.zup.pix.listagem

import br.com.zup.KeymanagerListaGrpcServiceGrpc.KeymanagerListaGrpcServiceBlockingStub
import br.com.zup.ListaPixRequest
import br.com.zup.ListaPixResponse
import br.com.zup.pix.enums.TipoDeChave
import br.com.zup.pix.enums.TipoDeConta
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

@Controller("/api/clientes/{clienteId}/pix")
class ListaChavesController(
    @Inject val grpcStub: KeymanagerListaGrpcServiceBlockingStub,
) {

    @Get
    fun lista(@PathVariable clienteId: String): HttpResponse<ListagemChavesResponse> {

        val response = grpcStub.lista(ListaPixRequest
            .newBuilder()
            .setClienteId(clienteId)
            .build())

        return HttpResponse.ok(ListagemChavesResponse(response))

    }
}

data class ListagemChavesResponse(
    val chaves: List<ItemChave>,
) {

    data class ItemChave(
        val pixId: String,
        val clienteId: String,
        val tipoDeChave: TipoDeChave,
        val valor: String,
        val tipoDeConta: TipoDeConta,
        val criadaEm: LocalDateTime,
    )

    constructor(response: ListaPixResponse) : this(
        response.chavesList.map { item ->
            ItemChave(
                pixId = item.pixId,
                clienteId = item.clienteId,
                tipoDeChave = TipoDeChave.valueOf(item.tipoDeChave.name),
                valor = item.valor,
                tipoDeConta = TipoDeConta.valueOf(item.tipoDeConta.name),
                criadaEm = LocalDateTime.ofEpochSecond(
                    item.criadaEm.seconds,
                    item.criadaEm.nanos,
                    ZoneOffset.UTC
                )
            )
        }
    )
}
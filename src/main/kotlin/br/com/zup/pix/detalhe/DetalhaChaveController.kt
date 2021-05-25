package br.com.zup.pix.detalhe

import br.com.zup.ConsultaPixRequest
import br.com.zup.ConsultaPixResponse
import br.com.zup.KeymanagerConsultaGrpcServiceGrpc.KeymanagerConsultaGrpcServiceBlockingStub
import br.com.zup.pix.enums.TipoDeChave
import br.com.zup.pix.enums.TipoDeConta
import com.fasterxml.jackson.annotation.JsonFormat
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

@Controller("/api/clientes/{clienteId}/pix")
class DetalhaChaveController(
    @Inject val grpcClient: KeymanagerConsultaGrpcServiceBlockingStub,
) {

    @Get("/{pixId}")
    fun detalha(
        @PathVariable clienteId: String,
        @PathVariable pixId: String,
    ): HttpResponse<Any> {

        val response: DetalhesChavePix = grpcClient.consulta(ConsultaPixRequest
            .newBuilder()
            .setInterna(ConsultaPixRequest.ConsultaInterna
                .newBuilder()
                .setClienteId(clienteId)
                .setPixId(pixId)
                .build())
            .build())
            .let { response -> DetalhesChavePix(response) }

        return HttpResponse.ok(response)
    }

}

data class DetalhesChavePix(
    val pixId: String,
    val clienteId: String,
    val tipoDeChave: TipoDeChave,
    val valor: String,
    val titular: Titular,
    val conta: Conta,
    // @JsonFormat(pattern = "dd/MM/yyyy - hh:mm:ss")
    val criadaEm: LocalDateTime,
) {
    constructor(resposta: ConsultaPixResponse) : this(
        resposta.pixId,
        resposta.clienteId,
        TipoDeChave.valueOf(resposta.tipoDeChave.name),
        resposta.valor,
        Titular(resposta.titular),
        Conta(resposta.conta),
        LocalDateTime.ofEpochSecond(
            resposta.criadaEm.seconds,
            resposta.criadaEm.nanos,
            ZoneOffset.UTC
        )
    )
}

data class Titular(
    val nome: String,
    val cpf: String,
) {
    constructor(titular: br.com.zup.Titular) : this(
        titular.nome,
        titular.cpf
    )
}

data class Conta(
    val instituicao: String,
    val agencia: String,
    val numero: String,
    val tipo: TipoDeConta,
) {
    constructor(conta: br.com.zup.Conta) : this(
        conta.instituicao,
        conta.agencia,
        conta.numero,
        TipoDeConta.valueOf(conta.tipo.name)
    )
}
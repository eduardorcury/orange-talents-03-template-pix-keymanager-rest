package br.com.zup.pix.cadastro

import br.com.zup.CadastroPixRequest
import br.com.zup.pix.enums.TipoDeChave
import br.com.zup.pix.enums.TipoDeConta
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import br.com.zup.TipoDeChave as TipoChaveGrpc
import br.com.zup.TipoDeConta as TipoContaGrpc

@Introspected
@ChavePixValida
data class NovaChaveRequest(
    @field:NotNull val tipoDeChave: TipoDeChave?,
    @Size(max = 77) val valor: String?,
    @field:NotNull val tipoDeConta: TipoDeConta?,
) {

    fun toGrpcRequest(clienteId: String) = CadastroPixRequest.newBuilder()
        .setIdTitular(clienteId)
        .setTipoDeChave(this.tipoDeChave?.toGrpc() ?: TipoChaveGrpc.TIPO_CHAVE_DESCONHECIDO)
        .setValor(this.valor ?: "")
        .setTipoDeConta(this.tipoDeConta?.toGrpc() ?: TipoContaGrpc.TIPO_CONTA_DESCONHECIDO)
        .build()

}

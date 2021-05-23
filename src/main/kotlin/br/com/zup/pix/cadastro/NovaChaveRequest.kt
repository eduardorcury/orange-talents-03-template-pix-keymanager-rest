package br.com.zup.pix.cadastro

import br.com.zup.TipoDeChave
import br.com.zup.TipoDeConta
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Introspected
data class NovaChaveRequest(
    @field:NotBlank val clienteId: String?,
    @field:NotNull val tipoDeChave: TipoDeChave?,
    @Size(max = 77) val valor: String? = "",
    @field:NotNull val tipoDeConta: TipoDeConta?,
)

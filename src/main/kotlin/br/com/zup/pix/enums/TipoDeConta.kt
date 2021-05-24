package br.com.zup.pix.enums

import br.com.zup.TipoDeConta

enum class TipoDeConta {

    CONTA_CORRENTE, CONTA_POUPANCA;

    fun toGrpc(): TipoDeConta = TipoDeConta.valueOf(this.name)

}
package br.com.zup.pix.enums

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.ValueSource

internal class EnumsTest {

    @Nested
    inner class NestedCPF {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings =  ["12345", "040.449.800-04"] )
        internal fun `testa valores de cpf invalidos`(cpf: String?) {
            val resultado = TipoDeChave.CPF.chaveValida(cpf, null)
            assertFalse(resultado)
        }

        @Test
        internal fun `deve retornar valido para CPF valido`() {
            val resultado = TipoDeChave.CPF.chaveValida("04044980004", null)
            assertTrue(resultado)
        }

    }

    @Nested
    inner class NestedTELEFONE {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings =  ["12345"] )
        internal fun `testa valores de telefone invalidos`(telefone: String?) {
            val resultado = TipoDeChave.TELEFONE.chaveValida(telefone, null)
            assertFalse(resultado)
        }

        @Test
        internal fun `deve retornar valido para telefone valido`() {
            val resultado = TipoDeChave.TELEFONE.chaveValida("+5585988714077", null)
            assertTrue(resultado)
        }

    }

    @Nested
    inner class NestedEMAIL {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings =  ["email"] )
        internal fun `testa valores de email invalidos`(email: String?) {
            val resultado = TipoDeChave.EMAIL.chaveValida(email, null)
            assertFalse(resultado)
        }

        @ParameterizedTest
        @ValueSource(strings =  ["email@gmail.com", "email@outlook.com", "email@usp.com.br"] )
        internal fun `deve retornar valido para email valido`(email: String?) {
            val resultado = TipoDeChave.EMAIL.chaveValida(email, null)
            assertTrue(resultado)
        }
    }

    @Nested
    inner class NestedALEATORIA {

        @Test
        internal fun `testa chave aleatoria`() {
            val resultado = TipoDeChave.ALEATORIA.chaveValida("", null)
            assertTrue(resultado)
        }

    }


}
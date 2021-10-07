package com.github.furetur

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class PlusMultiplyParenTest {
    private val parser = PrattParser(
        infixBindingPowers = mapOf(
            Token.Operator("+") to Pair(1, 2),
            Token.Operator("*") to Pair(3, 4),
        )
    )

    private fun String.parse() = parser.parse(tokenize())

    @Test
    fun `should parse (1)`() {
        assertEquals("1", "(1)".parse().stringify())
    }

    @Test
    fun `should parse many ((((0))))`() {
        assertEquals("0", "((((0))))".parse().stringify())
    }

    @Test
    fun `should first parse what is in parenthesis in 1 + (2 + 3)`() {
        assertEquals("(1+(2+3))", "1+(2+3)".parse().stringify())
    }

    @Test
    fun `parenthesis should have the higher precedence in 1 x (2 + 3)`() {
        assertEquals("(1*(2+3))", "1*(2+3)".parse().stringify())
    }
}

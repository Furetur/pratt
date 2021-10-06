package com.github.furetur

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class PowerPlusTest {
    private val parser = PrattParser(
        mapOf(
            Token.Operator("+") to Pair(1, 2),
            Token.Operator("^") to Pair(4, 3),
        )
    )

    private fun String.parse() = parser.parse(tokenize())

    @Test
    fun `power should have higher priority in 1 + 2^3`() {
        assertEquals("(1+(2^3))", "1 + 2^3".parse().stringify())
    }

    @Test
    fun `power should be right associative in 1 + 2^3^4`() {
        assertEquals("(1+(2^(3^4)))", "1 + 2^3^4".parse().stringify())
    }

    @Test
    fun `power should be right associative in 1^2^3 + 4`() {
        assertEquals("((1^(2^3))+4)", "1^2^3 + 4".parse().stringify())
    }

    @Test
    fun `power should be right associative and additions should be left in 1 + 2^3^4 + 5`() {
        assertEquals("((1+(2^(3^4)))+5)", "1 + 2^3^4 + 5".parse().stringify())
    }
}

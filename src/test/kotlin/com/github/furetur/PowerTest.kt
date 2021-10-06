package com.github.furetur

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class PowerTest {
    private val parser = PrattParser(
        mapOf(
            Token.Operator("^") to Pair(2, 1),
        )
    )
    private fun String.parse(): AstNode = parser.parse(tokenize())
    @Test
    fun `should parse 1`() {
        assertEquals("1", "1".parse().stringify())
    }

    @Test
    fun `power should be right associative 1^2^3`() {
        assertEquals("(1^(2^3))", "1^2^3".parse().stringify())
    }

    @Test
    fun `power should be right associative 1^2^3^---^100`() {
        val expression = (1..100).joinToString("^")
        var expected = "100"
        for (i in 99 downTo 1) {
            expected = "($i^$expected)"
        }
        assertEquals(expected, expression.parse().stringify())
    }
}

package com.github.furetur

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PlusMultiplyTest {
    private val parser = PrattParser(
        mapOf(
            Token.Operator("+") to Pair(1, 2),
            Token.Operator("*") to Pair(3, 4)
        )
    )
    private fun String.parse(): AstNode = parser.parse(tokenize())

    @Test
    fun `should parse 1`() {
        assertEquals("1", "1".parse().stringify())
    }

    @Test
    fun `should parse 1 + 2`() {
        assertEquals("(1+2)", "1+2".parse().stringify())
    }

    @Test
    fun `should parse 4 x 5`() {
        assertEquals("(4*5)", "4 * 5".parse().stringify())
    }

    @Test
    fun `should parse 1 + 2 x 3`() {
        assertEquals("(1+(2*3))", "1 + 2 * 3".parse().stringify())
    }

    @Test
    fun `addition should be left associative 1 + 2 + 3`() {
        assertEquals("((1+2)+3)", "1 + 2 + 3".parse().stringify())
    }

    @Test
    fun `addition should be left associative 1+2+3+---+100`() {
        val expression = (1..100).joinToString("+")
        var expected = "1"
        for (i in 2..100) {
            expected = "($expected+$i)"
        }
        kotlin.test.assertEquals(expected, expression.parse().stringify())
    }

    @Test
    fun `multiplication should be left associative 1 x 2 x 3`() {
        assertEquals("((1*2)*3)", "1 * 2 * 3".parse().stringify())
    }

    @Test
    fun `multiplication should be left associative 1x2x3x---x100`() {
        val expression = (1..100).joinToString("*")
        var expected = "1"
        for (i in 2..100) {
            expected = "($expected*$i)"
        }
        kotlin.test.assertEquals(expected, expression.parse().stringify())
    }

    @Test
    fun `should parse 1 + 2x3 + 4`() {
        assertEquals("((1+(2*3))+4)", "1 + 2 * 3 + 4".parse().stringify())
    }

    @Test
    fun `should parse 1x2 + 3`() {
        assertEquals("((1*2)+3)", "1 * 2 + 3".parse().stringify())
    }

    @Test
    fun `should parse 1x2 + 3x4 + 5x6 + 7x8`() {
        assertEquals("((((1*2)+(3*4))+(5*6))+(7*8))", "1*2 + 3*4 + 5*6 + 7*8".parse().stringify())
    }
}

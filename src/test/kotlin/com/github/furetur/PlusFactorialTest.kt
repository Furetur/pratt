package com.github.furetur

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class PlusFactorialTest {
    private val parser = PrattParser(
        infixBindingPowers = mapOf(
            Token.Operator("+") to Pair(1, 2),
            Token.Operator("-") to Pair(1, 2),
        ),
        postfixBindingPowers = mapOf(
            Token.Operator("!") to 3
        )
    )

    private fun String.parse() = parser.parse(tokenize())

    @Test
    fun `should parse 10!!`() {
        assertEquals("((10!)!)", "10!!".parse().stringify())
    }

    @Test
    fun `should parse 1! + 2`() {
        assertEquals("((1!)+2)", "1! + 2".parse().stringify())
    }

    @Test
    fun `plus should have lower precedence in 1 + 2!`() {
        assertEquals("(1+(2!))", "1 + 2!".parse().stringify())
    }

    @Test
    fun `plus should have lower precedence in 1! + 2!`() {
        assertEquals("((1!)+(2!))", "1! + 2!".parse().stringify())
    }

    @Test
    fun `should parse checkerboard 1! + 2 + 3! + 4`() {
        assertEquals("((((1!)+2)+(3!))+4)", "1! + 2 + 3! + 4".parse().stringify())
    }
}

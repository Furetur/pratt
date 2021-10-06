package com.github.furetur

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class PrefixMinusPlusMinusTest {
    private val parser = PrattParser(
        infixBindingPowers = mapOf(
            Token.Operator("+") to Pair(1, 2),
            Token.Operator("-") to Pair(1, 2),
        ),
        prefixBindingPowers = mapOf(
            Token.Operator("-") to 3,
        )
    )
    private fun String.parse(): AstNode = parser.parse(tokenize())
    @Test
    fun `should parse lonely -1`() {
        assertEquals("(-1)", "-1".parse().stringify())
    }

    @Test
    fun `should parse prefix in -1 + 2`() {
        assertEquals("((-1)+2)", "-1 + 2".parse().stringify())
    }

    @Test
    fun `should parse prefix in -1 + 2 + -3`() {
        assertEquals("(((-1)+2)+(-3))", "-1 + 2 + -3".parse().stringify())
    }

    @Test
    fun `should parse prefix many prefixes ----1`() {
        assertEquals("(-(-(-(-1))))", "----1".parse().stringify())
    }

    @Test
    fun `should parse many prefixes in the middle 1 + --2 + 3`() {
        assertEquals("((1+(-(-2)))+3)", "1 + --2 + 3".parse().stringify())
    }

    @Test
    fun `should parse several individual prefixes in -1 + -2 + 3 + -4`() {
        assertEquals("((((-1)+(-2))+3)+(-4))", "-1 + -2 + 3 + -4".parse().stringify())
    }
}

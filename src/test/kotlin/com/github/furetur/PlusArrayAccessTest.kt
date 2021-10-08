package com.github.furetur

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class PlusArrayAccessTest {
    private val parser = PrattParser(
        infixBindingPowers = mapOf(
            Token.Operator("+") to Pair(1, 2),
        ),
        postfixBindingPowers = mapOf(
            Token.Operator("[") to 3
        )
    )

    private fun String.parse() = parser.parse(tokenize())

    // all square brackets are replaced with braces in method names
    @Test
    fun `should parse 1{2}`() {
        assertEquals("(1[2])", "1[2]".parse().stringify())
    }

    @Test
    fun `should parse double 1{2}{3}`() {
        assertEquals("((1[2])[3])", "1[2][3]".parse().stringify())
    }

    @Test
    fun `should parse triple 1{2}{3}{4}`() {
        assertEquals("(((1[2])[3])[4])", "1[2][3][4]".parse().stringify())
    }

    @Test
    fun `array access precedence should be higher than addition in 1 + 2{3}`() {
        assertEquals("(1+(2[3]))", "1 + 2[3]".parse().stringify())
    }

    @Test
    fun `parenthesis should have higher precedence in (1 + 2){3}`() {
        assertEquals("((1+2)[3])", "(1 + 2)[3]".parse().stringify())
    }

    @Test
    fun `should parse double in the middle of expression in 1 + 2{3}{4} + 5`() {
        assertEquals("((1+((2[3])[4]))+5)", "1 + 2[3][4] + 5".parse().stringify())
    }
}

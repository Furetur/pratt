package com.github.furetur

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class PlusMinusTest {
    private val parser = PrattParser(
        mapOf(
            Token.Operator("+") to Pair(1, 2),
            Token.Operator("-") to Pair(1, 2),
        )
    )

    private fun String.parse() = parser.parse(tokenize())

    @Test
    fun `plus and minus should have the save precedence and be left associative in 1+2-3-4+5`() {
        assertEquals("((((1+2)-3)-4)+5)", "1+2-3-4+5".parse().stringify())
    }
}

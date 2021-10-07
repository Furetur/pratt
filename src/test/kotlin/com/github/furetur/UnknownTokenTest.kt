package com.github.furetur

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.Exception

internal class UnknownTokenTest {
    private val parser = PrattParser(
        infixBindingPowers = mapOf(
            Token.Operator("+") to Pair(1, 2),
            Token.Operator("-") to Pair(1, 2),
            Token.Operator("*") to Pair(3, 4),
        ),
        prefixBindingPowers = mapOf(
            Token.Operator("-") to 3,
        )
    )
    private fun String.parse(): AstNode = parser.parse(tokenize())

    @Test
    fun `should throw if begins with unknown unary operator`() {
        assertThrows<Exception> {
            "~1".parse()
        }
    }

    @Test
    fun `should throw if begins with unary operator that is infix`() {
        assertThrows<Exception> {
            "+1".parse()
        }
    }

    @Test
    fun `should throw if begins with unknown unary operator that is postfix`() {
        assertThrows<Exception> {
            "!1".parse()
        }
    }

    @Test
    fun `should throw if second addend has unknown unary operator`() {
        assertThrows<Exception> {
            "1+~1".parse()
        }
    }
    @Test
    fun `should throw if second operand is not present`() {
        assertThrows<Exception> {
            "1+".parse()
        }
    }
    @Test
    fun `should throw if unknown infix operator`() {
        assertThrows<Exception> {
            "1 & 4".parse()
        }
    }
    @Test
    fun `should throw if operand ends with unknown postfix operator`() {
        assertThrows<Exception> {
            "1%".parse()
        }
    }
    @Test
    fun `should throw if unmatched left parenthesis`() {
        assertThrows<Exception> {
            "1 * (2 + 3 * 5".parse()
        }
    }

    @Test
    fun `should throw if unmatched right parenthesis`() {
        assertThrows<Exception> {
            "1 * 5) + 5".parse()
        }
    }
}

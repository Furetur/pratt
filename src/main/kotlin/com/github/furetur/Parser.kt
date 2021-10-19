package com.github.furetur

import com.github.furetur.cursor.ListTokenCursor
import com.github.furetur.cursor.TokenCursor
import com.github.furetur.parselets.BeginningParselet
import com.github.furetur.parselets.FollowingParselet
import kotlin.jvm.Throws

class Parser<T : Token>(
    private val beginningParselets: Map<TokenType, BeginningParselet<T>> = emptyMap(),
    private val followingParselets: Map<TokenType, FollowingParselet<T>> = emptyMap()
) {
    @Throws(ParsingException::class)
    fun parse(tokens: Iterable<T>): Expression<T> = parse(ListTokenCursor(tokens.toList()))

    fun parse(cursor: TokenCursor<T>): Expression<T> {
        return parseExpression(Context(cursor), 0)
    }

    private fun parseExpression(context: Context, minBindingPower: Int): Expression<T> {
        val beginningToken = context.next() ?: throw UnexpectedEndException()
        var leftSide = beginningParselets[beginningToken.tokenType]?.parse(beginningToken, context)
            ?: throw UnexpectedTokenException(beginningToken)

        while (true) {
            val nextOperator = context.peekNext() ?: break

            val followingParselet = followingParselets[nextOperator.tokenType] ?: break
            if (followingParselet.leftBindingPower < minBindingPower) {
                break
            }
            context.next()
            leftSide = followingParselet.parse(leftSide, nextOperator, context)
        }
        return leftSide
    }

    inner class Context(private val cursor: TokenCursor<T>) : TokenCursor<T> by cursor {
        fun parseExpression(minBindingPower: Int = 0) = parseExpression(this, minBindingPower)
    }
}

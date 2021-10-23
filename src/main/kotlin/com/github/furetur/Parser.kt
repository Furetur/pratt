package com.github.furetur

import com.github.furetur.cursor.ListTokenCursor
import com.github.furetur.cursor.TokenCursor
import com.github.furetur.parselets.BeginningParselet
import com.github.furetur.parselets.FollowingParselet
import kotlin.jvm.Throws

class Parser<Tok, TokType>(
    private val getTokenType: (Tok) -> TokType,
    private val beginningParselets: Map<TokType, BeginningParselet<Tok, TokType>> = emptyMap(),
    private val followingParselets: Map<TokType, FollowingParselet<Tok, TokType>> = emptyMap()
) {
    @Throws(ParsingException::class)
    fun parse(tokens: Iterable<Tok>): Node<Tok> = parse(ListTokenCursor(tokens.toList()))

    fun parse(cursor: TokenCursor<Tok>): Node<Tok> {
        return parseExpression(Context(cursor), 0)
    }

    private fun parseExpression(context: Context, minBindingPower: Int): Node<Tok> {
        val beginningToken = context.next() ?: throw UnexpectedEndException()
        var leftSide = beginningParselets[beginningToken.tokenType]?.parse(beginningToken, context)
            ?: throw UnexpectedTokenException(beginningToken.toString())

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

    private val Tok.tokenType: TokType
        get() = getTokenType(this)

    inner class Context(private val cursor: TokenCursor<Tok>) : TokenCursor<Tok> by cursor {
        fun parseExpression(minBindingPower: Int = 0) = parseExpression(this, minBindingPower)

        fun expectNext(tokenType: TokType) {
            val nextToken = next()
            if (nextToken?.tokenType != tokenType) {
                throw UnexpectedTokenException(nextToken.toString())
            }
        }
    }
}

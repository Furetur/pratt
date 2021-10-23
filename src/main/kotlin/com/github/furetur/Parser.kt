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
    /**
     * Attempts to parse the entire stream of tokens.
     * Throws an error if expression ends before the token stream.
     * @throws ParsingException
     */
    @Throws(ParsingException::class)
    fun parseFull(tokens: Iterable<Tok>): Node<Tok> {
        val cursor = ListTokenCursor(tokens.toList())
        val parseResult = parse(cursor)
        if (cursor.isDone) {
            return parseResult
        } else {
            throw UnexpectedTokenException(cursor.nextTextPosition, cursor.peekNext().toString())
        }
    }

    /**
     * Parses the expression moving the cursor.
     * Assumes that the cursor points to the start of the expression.
     * Will stop parsing and moving the cursor when the expression ends.
     * @throws ParsingException
     */
    @Throws(ParsingException::class)
    fun parse(cursor: TokenCursor<Tok>): Node<Tok> {
        return parseExpression(Context(cursor), 0)
    }

    private fun parseExpression(context: Context, minBindingPower: Int): Node<Tok> {
        val beginningToken = context.next() ?: throw UnexpectedEofException(context.currentTextPosition)
        var leftSide = beginningParselets[beginningToken.tokenType]?.parse(beginningToken, context)
            ?: throw UnexpectedTokenException(
                context.currentTextPosition,
                beginningToken.toString(),
                beginningParselets.keys.map { it.toString() }
            )

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
        /**
         * Move the cursor forward and parse the next expression
         */
        fun parseExpression(minBindingPower: Int = 0) = parseExpression(this, minBindingPower)

        /**
         * Eat the next token and throw an exception if it is not [tokenType].
         */
        fun expectNext(tokenType: TokType) {
            val nextToken = next() ?: throw UnexpectedEofException(currentTextPosition)
            if (nextToken.tokenType != tokenType) {
                throw UnexpectedTokenException(currentTextPosition, nextToken.toString(), listOf(tokenType.toString()))
            }
        }
    }
}

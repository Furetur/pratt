package com.github.furetur.parselets

import com.github.furetur.Expression
import com.github.furetur.Parser
import com.github.furetur.Token
import com.github.furetur.TokenType
import com.github.furetur.cursor.expectNext

class ArrayIndexParselet<T : Token>(
    override val leftBindingPower: Int,
    private val closingBracketTokenType: TokenType
) : FollowingParselet<T> {

    override fun parse(firstOperand: Expression<T>, followingToken: T, context: Parser<T>.Context): Expression<T> {
        val indexExpression = context.parseExpression()
        context.expectNext(closingBracketTokenType)
        return Expression.Operator(followingToken, listOf(firstOperand, indexExpression))
    }
}

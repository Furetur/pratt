package com.github.furetur.parselets

import com.github.furetur.Expression
import com.github.furetur.Parser

class ArrayIndexParselet<Tok, TokType>(
    override val leftBindingPower: Int,
    private val closingBracketTokenType: TokType
) : FollowingParselet<Tok, TokType> {

    override fun parse(firstOperand: Expression<Tok>, followingToken: Tok, context: Parser<Tok, TokType>.Context): Expression<Tok> {
        val indexExpression = context.parseExpression()
        context.expectNext(closingBracketTokenType)
        return Expression.Operator(followingToken, listOf(firstOperand, indexExpression))
    }
}

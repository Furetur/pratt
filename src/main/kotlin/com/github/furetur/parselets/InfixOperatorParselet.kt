package com.github.furetur.parselets

import com.github.furetur.Expression
import com.github.furetur.Parser

class InfixOperatorParselet<Tok, TokType>(override val leftBindingPower: Int, val rightBindingPower: Int) :
    FollowingParselet<Tok, TokType> {
    override fun parse(firstOperand: Expression<Tok>, followingToken: Tok, context: Parser<Tok, TokType>.Context): Expression<Tok> {
        val secondOperand = context.parseExpression(rightBindingPower)
        return Expression.Operator(followingToken, listOf(firstOperand, secondOperand))
    }
}

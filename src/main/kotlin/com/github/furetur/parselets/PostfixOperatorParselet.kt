package com.github.furetur.parselets

import com.github.furetur.Expression
import com.github.furetur.Parser

class PostfixOperatorParselet<Tok, TokType>(override val leftBindingPower: Int) : FollowingParselet<Tok, TokType> {
    override fun parse(firstOperand: Expression<Tok>, followingToken: Tok, context: Parser<Tok, TokType>.Context): Expression<Tok> {
        return Expression.Operator(followingToken, listOf(firstOperand))
    }
}

package com.github.furetur.parselets

import com.github.furetur.Expression
import com.github.furetur.Parser
import com.github.furetur.Token

class PostfixOperatorParselet<T : Token>(override val leftBindingPower: Int) : FollowingParselet<T> {
    override fun parse(firstOperand: Expression<T>, followingToken: T, context: Parser<T>.Context): Expression<T> {
        return Expression.Operator(followingToken, listOf(firstOperand))
    }
}

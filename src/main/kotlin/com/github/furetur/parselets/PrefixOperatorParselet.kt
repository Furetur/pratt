package com.github.furetur.parselets

import com.github.furetur.Expression
import com.github.furetur.Parser
import com.github.furetur.Token

class PrefixOperatorParselet<T : Token>(private val rightBindingPower: Int) : BeginningParselet<T> {
    override fun parse(beginningToken: T, context: Parser<T>.Context): Expression<T> {
        val operand = context.parseExpression(rightBindingPower)
        return Expression.Operator(beginningToken, listOf(operand))
    }
}

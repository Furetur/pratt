package com.github.furetur.parselets

import com.github.furetur.Expression
import com.github.furetur.Parser

class PrefixOperatorParselet<Tok, TokType>(private val rightBindingPower: Int) : BeginningParselet<Tok, TokType> {
    override fun parse(beginningToken: Tok, context: Parser<Tok, TokType>.Context): Expression<Tok> {
        val operand = context.parseExpression(rightBindingPower)
        return Expression.Operator(beginningToken, listOf(operand))
    }
}

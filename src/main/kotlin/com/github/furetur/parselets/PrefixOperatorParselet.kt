package com.github.furetur.parselets

import com.github.furetur.Node
import com.github.furetur.OperatorNode
import com.github.furetur.Parser

/**
 * Parselet for parsing prefix operator expressions
 */
class PrefixOperatorParselet<Tok, TokType>(private val rightBindingPower: Int) : BeginningParselet<Tok, TokType> {
    override fun parse(beginningToken: Tok, context: Parser<Tok, TokType>.Context): Node<Tok> {
        val operand = context.parseExpression(rightBindingPower)
        return OperatorNode(beginningToken, listOf(operand))
    }
}

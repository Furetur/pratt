package com.github.furetur.parselets

import com.github.furetur.Node
import com.github.furetur.OperatorNode
import com.github.furetur.Parser

/**
 * Parselet for parsing infix operator expressions
 */
class InfixOperatorParselet<Tok, TokType>(override val leftBindingPower: Int, val rightBindingPower: Int) :
    FollowingParselet<Tok, TokType> {
    override fun parse(firstOperand: Node<Tok>, followingToken: Tok, context: Parser<Tok, TokType>.Context): Node<Tok> {
        val secondOperand = context.parseExpression(rightBindingPower)
        return OperatorNode(followingToken, listOf(firstOperand, secondOperand))
    }
}

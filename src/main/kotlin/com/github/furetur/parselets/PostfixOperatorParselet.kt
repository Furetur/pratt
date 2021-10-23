package com.github.furetur.parselets

import com.github.furetur.Node
import com.github.furetur.OperatorNode
import com.github.furetur.Parser

class PostfixOperatorParselet<Tok, TokType>(override val leftBindingPower: Int) : FollowingParselet<Tok, TokType> {
    override fun parse(firstOperand: Node<Tok>, followingToken: Tok, context: Parser<Tok, TokType>.Context): Node<Tok> {
        return OperatorNode(followingToken, listOf(firstOperand))
    }
}

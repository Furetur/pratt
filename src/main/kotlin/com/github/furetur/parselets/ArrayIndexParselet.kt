package com.github.furetur.parselets

import com.github.furetur.Node
import com.github.furetur.NodeType
import com.github.furetur.Parser

class ArrayIndexParselet<Tok, TokType>(
    override val leftBindingPower: Int,
    private val closingBracketTokenType: TokType
) : FollowingParselet<Tok, TokType> {

    override fun parse(firstOperand: Node<Tok>, followingToken: Tok, context: Parser<Tok, TokType>.Context): Node<Tok> {
        val indexExpression = context.parseExpression()
        context.expectNext(closingBracketTokenType)
        return ArrayIndexNode(firstOperand, indexExpression)
    }
}

data class ArrayIndexNode<Tok>(val array: Node<Tok>, val index: Node<Tok>) : Node<Tok> {
    override val type = NodeType.NonTerminal(listOf(array, index))

    override fun toString(): String = "$array[$index]"
}

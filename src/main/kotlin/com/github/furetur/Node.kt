package com.github.furetur

interface Node<Tok> {
    val type: NodeType<Tok>
}

sealed class NodeType<Tok> {
    data class Terminal<Tok>(val token: Tok) : NodeType<Tok>()
    data class NonTerminal<Tok>(val children: List<Node<Tok>>) : NodeType<Tok>()
}

data class TokenNode<Tok>(val token: Tok) : Node<Tok> {
    override val type = NodeType.Terminal(token)

    override fun toString(): String = token.toString()
}

data class OperatorNode<Tok>(val operator: Tok, val operands: List<Node<Tok>>) : Node<Tok> {
    override val type = NodeType.NonTerminal(operands)

    /**
     * Converts operator to postfix form and encloses it in parentheses
     */
    override fun toString(): String = "(${operands.joinToString(" ")} $operator)"
}

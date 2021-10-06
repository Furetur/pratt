package com.github.furetur

sealed class Token {
    data class Number(val value: Int) : Token() {
        override fun toString(): String = value.toString()
    }
    data class Operator(val symbol: String) : Token() {
        override fun toString(): String = symbol
    }
}

sealed class AstNode {
    data class Number(val value: Int) : AstNode()
    data class Operator(val operator: Token.Operator, val left: AstNode, val right: AstNode) : AstNode()
}

class TokenCursor(val tokens: List<Token>) {
    var nextIndex = 0
    fun next(): Token? = tokens.getOrNull(nextIndex++)
    fun peek(): Token? = tokens.getOrNull(nextIndex)
}

class PrattParser(val bindingPowers: Map<Token.Operator, Pair<Int, Int>>) {
    fun parse(text: List<Token>): AstNode {
        return internalParse(TokenCursor(text), 0)
    }
    fun internalParse(cursor: TokenCursor, minBindingPower: Int): AstNode {
        val nextToken = cursor.next()
        var leftSide: AstNode = if (nextToken is Token.Number) {
            AstNode.Number(nextToken.value)
        } else {
            error("Unexpected token '$nextToken'")
        }

        while (true) {
            val nextOperator = cursor.peek() ?: break
            if (nextOperator !is Token.Operator) error("Unexpected token '$nextOperator', expected an operator")
            val (leftBindingPower, rightBindingPower) = bindingPowers[nextOperator] ?: error("Unknown infix operator '$nextOperator'")
            if (leftBindingPower < minBindingPower) break
            cursor.next()
            val rightSide = internalParse(cursor, rightBindingPower)
            leftSide = AstNode.Operator(nextOperator, leftSide, rightSide)
        }
        return leftSide
    }
}

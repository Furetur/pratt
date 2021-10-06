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
    data class PrefixOperator(val operator: Token.Operator, val operand: AstNode) : AstNode()
}

class TokenCursor(val tokens: List<Token>) {
    var nextIndex = 0
    fun next(): Token? = tokens.getOrNull(nextIndex++)
    fun peek(): Token? = tokens.getOrNull(nextIndex)
}

class PrattParser(
    private val infixBindingPowers: Map<Token.Operator, Pair<Int, Int>> = emptyMap(),
    private val prefixBindingPowers: Map<Token.Operator, Int> = emptyMap()
) {
    fun parse(text: List<Token>): AstNode {
        return internalParse(TokenCursor(text), 0)
    }

    fun internalParse(cursor: TokenCursor, minBindingPower: Int): AstNode {
        var leftSide: AstNode = when (val nextToken = cursor.next()) {
            is Token.Number -> AstNode.Number(nextToken.value)
            is Token.Operator -> {
                val prefixBindingPower = prefixBindingPowers[nextToken] ?: error("Unexpected prefix operator $nextToken")
                val prefixOperand = internalParse(cursor, prefixBindingPower)
                AstNode.PrefixOperator(nextToken, prefixOperand)
            }
            null -> error("Unexpected end of tokens")
        }

        while (true) {
            val nextOperator = cursor.peek() ?: break
            if (nextOperator !is Token.Operator) error("Unexpected token '$nextOperator', expected an operator")
            val (leftBindingPower, rightBindingPower) = infixBindingPowers[nextOperator]
                ?: error("Unknown infix operator '$nextOperator'")
            if (leftBindingPower < minBindingPower) break
            cursor.next()
            val rightSide = internalParse(cursor, rightBindingPower)
            leftSide = AstNode.Operator(nextOperator, leftSide, rightSide)
        }
        return leftSide
    }
}

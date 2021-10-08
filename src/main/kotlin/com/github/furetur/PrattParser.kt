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
    data class PostfixOperator(val operator: Token.Operator, val operand: AstNode) : AstNode()
    data class ArrayAccess(val array: AstNode, val index: AstNode) : AstNode()
}

class TokenCursor(val tokens: List<Token>) {
    var nextIndex = 0
    val isDone: Boolean
        get() = nextIndex !in tokens.indices
    fun next(): Token? = tokens.getOrNull(nextIndex++)
    fun peek(): Token? = tokens.getOrNull(nextIndex)
}

class PrattParser(
    private val infixBindingPowers: Map<Token.Operator, Pair<Int, Int>> = emptyMap(),
    private val prefixBindingPowers: Map<Token.Operator, Int> = emptyMap(),
    private val postfixBindingPowers: Map<Token.Operator, Int> = emptyMap(),
) {
    fun parse(text: List<Token>): AstNode {
        val cursor = TokenCursor(text)
        return internalParse(cursor, 0).also {
            if (!cursor.isDone) error("Cursor is not done")
        }
    }

    fun internalParse(cursor: TokenCursor, minBindingPower: Int): AstNode {
        var leftSide: AstNode = when (val nextToken = cursor.next()) {
            is Token.Number -> AstNode.Number(nextToken.value)
            is Token.Operator -> {
                if (nextToken.symbol == "(") {
                    val body = internalParse(cursor, 0)
                    val next = cursor.next()
                    require(next is Token.Operator && next.symbol == ")") { "Expected closing parenthesis" }
                    body
                } else {
                    val prefixBindingPower = prefixBindingPowers[nextToken] ?: error("Unexpected prefix operator $nextToken")
                    val prefixOperand = internalParse(cursor, prefixBindingPower)
                    AstNode.PrefixOperator(nextToken, prefixOperand)
                }
            }
            null -> error("Unexpected end of tokens")
        }

        while (true) {
            val nextOperator = cursor.peek() ?: break
            if (nextOperator !is Token.Operator) error("Unexpected token '$nextOperator', expected an operator")
            val postfixBindingPower = postfixBindingPowers[nextOperator]
            if (postfixBindingPower != null) {
                if (postfixBindingPower < minBindingPower) {
                    break
                }
                cursor.next()
                leftSide = if (nextOperator.symbol == "[") {
                    val indexExpression = internalParse(cursor, 0)
                    val next = cursor.next()
                    require(next is Token.Operator && next.symbol == "]") { "Expected closing bracket ]" }
                    AstNode.ArrayAccess(leftSide, indexExpression)
                } else {
                    AstNode.PostfixOperator(nextOperator, leftSide)
                }
                continue
            }
            val infixBindingPower = infixBindingPowers[nextOperator]
            if (infixBindingPower != null) {
                val (leftBindingPower, rightBindingPower) = infixBindingPower
                if (leftBindingPower < minBindingPower) break
                cursor.next()
                val rightSide = internalParse(cursor, rightBindingPower)
                leftSide = AstNode.Operator(nextOperator, leftSide, rightSide)
                continue
            }
            break
        }
        return leftSide
    }
}

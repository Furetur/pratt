package com.github.furetur

import guru.zoroark.lixy.LixyTokenType
import guru.zoroark.lixy.lixy
import guru.zoroark.lixy.matchers.anyOf
import guru.zoroark.lixy.matchers.matches

private enum class MyTokenTypes : LixyTokenType {
    OPERATOR, NUMBER, WHITESPACE
}

private val lexer = lixy {
    state {
        anyOf("*", "+", "^", "-", "!", "(", ")", "~", "&", "%", "[", "]") isToken MyTokenTypes.OPERATOR
        anyOf(" ", "\n", "\t") isToken MyTokenTypes.WHITESPACE
        matches("[0-9]+") isToken MyTokenTypes.NUMBER
    }
}

fun String.tokenize(): List<Token> = lexer.tokenize(this).filter { it.tokenType != MyTokenTypes.WHITESPACE }.map {
    when (it.tokenType) {
        MyTokenTypes.OPERATOR -> Token.Operator(it.string)
        MyTokenTypes.NUMBER -> Token.Number(it.string.toInt())
        else -> error("Unexpected token $it")
    }
}

private val parser = PrattParser(
    infixBindingPowers = mapOf(
        Token.Operator("+") to Pair(1, 2),
        Token.Operator("-") to Pair(1, 2),
        Token.Operator("*") to Pair(3, 4),
        Token.Operator("^") to Pair(6, 5),
    ),
    prefixBindingPowers = mapOf(
        Token.Operator("-") to 2,
    ),
    postfixBindingPowers = mapOf(
        Token.Operator("[") to 3,
        Token.Operator("!") to 3
    )
)

fun String.parse() = parser.parse(tokenize())

fun AstNode.stringify(): String = when (this) {
    is AstNode.Number -> value.toString()
    is AstNode.Operator -> "(${left.stringify()}$operator${right.stringify()})"
    is AstNode.PrefixOperator -> "(-${operand.stringify()})"
    is AstNode.PostfixOperator -> "(${operand.stringify()}$operator)"
    is AstNode.ArrayAccess -> "(${array.stringify()}[${index.stringify()}])"
}

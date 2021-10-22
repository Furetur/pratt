package com.github.furetur

import com.github.furetur.dsl.Associativity.RIGHT
import com.github.furetur.dsl.pratt
import com.github.furetur.parselets.ArrayIndexParselet
import com.github.furetur.parselets.GroupingParselet
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

sealed class TokenType {
    data class Operator(val symbol: String) : TokenType()
    object Number : TokenType()
}

sealed class Token(val tokenType: TokenType) {
    data class Operator(val symbol: String) : Token(TokenType.Operator(symbol)) {
        override fun toString(): String = symbol
    }
    data class Number(val value: Int) : Token(TokenType.Number) {
        override fun toString(): String = value.toString()
    }
}

fun String.tokenize(): List<Token> = lexer.tokenize(this).filter { it.tokenType != MyTokenTypes.WHITESPACE }.map {
    when (it.tokenType) {
        MyTokenTypes.OPERATOR -> Token.Operator(it.string)
        MyTokenTypes.NUMBER -> Token.Number(it.string.toInt())
        else -> error("Unexpected token $it")
    }
}

val parser = pratt<Token, TokenType> {
    getTokenType = { it.tokenType }

    atomic(TokenType.Number)

    // prefix
    prefix(TokenType.Operator("-")) precedence 3
    // infix
    infix(TokenType.Operator("+")) precedence 1
    infix(TokenType.Operator("-")) precedence 1
    infix(TokenType.Operator("*")) precedence 2
    infix(TokenType.Operator("^")) precedence 4 associativity RIGHT
    // postfix
    postfix(TokenType.Operator("!")) precedence 4
    // custom
    TokenType.Operator("(") isBeginningTokenOf GroupingParselet(TokenType.Operator(")"))
    TokenType.Operator("[") isFollowingTokenOf ArrayIndexParselet(3, TokenType.Operator("]"))
}

fun String.parse(): Expression<Token> = parser.parse(tokenize())

fun Expression<Token>.stringify(): String = when (this) {
    is Expression.Atomic -> token.toString()
    is Expression.Operator -> "(${operands.joinToString(" ") { it.stringify() }} $operator)"
}

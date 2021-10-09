package com.github.furetur

import com.github.furetur.parselets.ArrayIndexParselet
import com.github.furetur.parselets.AtomicParselet
import com.github.furetur.parselets.GroupingParselet
import com.github.furetur.parselets.InfixOperatorParselet
import com.github.furetur.parselets.PostfixOperatorParselet
import com.github.furetur.parselets.PrefixOperatorParselet
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

sealed class MyPrattTokenType : TokenType {
    data class Operator(val symbol: String) : MyPrattTokenType()
    object Number : MyPrattTokenType()
}

sealed class MyToken(override val tokenType: MyPrattTokenType) : Token {
    data class Operator(val symbol: String) : MyToken(MyPrattTokenType.Operator(symbol)) {
        override fun toString(): String = symbol
    }
    data class Number(val value: Int) : MyToken(MyPrattTokenType.Number) {
        override fun toString(): String = value.toString()
    }
}

fun String.tokenize(): List<MyToken> = lexer.tokenize(this).filter { it.tokenType != MyTokenTypes.WHITESPACE }.map {
    when (it.tokenType) {
        MyTokenTypes.OPERATOR -> MyToken.Operator(it.string)
        MyTokenTypes.NUMBER -> MyToken.Number(it.string.toInt())
        else -> error("Unexpected token $it")
    }
}

private val parser = Parser<MyToken>(
    beginningParselets = mapOf(
        MyPrattTokenType.Operator("-") to PrefixOperatorParselet(2),
        MyPrattTokenType.Number to AtomicParselet(),
        MyPrattTokenType.Operator("(") to GroupingParselet(MyPrattTokenType.Operator(")"))
    ),
    followingParselets = mapOf(
        // infix
        MyPrattTokenType.Operator("+") to InfixOperatorParselet(1, 2),
        MyPrattTokenType.Operator("-") to InfixOperatorParselet(1, 2),
        MyPrattTokenType.Operator("*") to InfixOperatorParselet(3, 4),
        MyPrattTokenType.Operator("^") to InfixOperatorParselet(6, 5),
        // postfix
        MyPrattTokenType.Operator("!") to PostfixOperatorParselet(3),
        MyPrattTokenType.Operator("[") to ArrayIndexParselet(3, MyPrattTokenType.Operator("]"))
    )
)

fun String.parse(): Expression<MyToken> = parser.parse(tokenize())

fun Expression<MyToken>.stringify(): String = when (this) {
    is Expression.Atomic -> token.toString()
    is Expression.Operator -> "(${operands.joinToString(" ") { it.stringify() }} $operator)"
}

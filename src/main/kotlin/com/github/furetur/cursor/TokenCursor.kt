package com.github.furetur.cursor

import com.github.furetur.Token
import com.github.furetur.TokenType
import com.github.furetur.UnexpectedEndException

interface TokenCursor<out T : Token> {
    val isDone: Boolean

    fun next(): T?
    fun peekNext(): T?
}

fun <T : Token> TokenCursor<T>.expectNext(tokenType: TokenType) {
    if (next()?.tokenType != tokenType) {
        throw UnexpectedEndException()
    }
}
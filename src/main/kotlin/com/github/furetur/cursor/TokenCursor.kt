package com.github.furetur.cursor

interface TokenCursor<out Tok> {
    val isDone: Boolean

    fun next(): Tok?
    fun peekNext(): Tok?
}

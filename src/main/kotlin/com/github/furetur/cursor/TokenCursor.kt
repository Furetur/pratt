package com.github.furetur.cursor

/**
 * An interface that allows reading a stream of tokens.
 */
interface TokenCursor<out Tok> {
    val isDone: Boolean

    fun next(): Tok?
    fun peekNext(): Tok?
}

package com.github.furetur.cursor

/**
 * An interface that allows reading a stream of tokens.
 */
interface TokenCursor<out Tok> {
    val isDone: Boolean
    val currentTextPosition: Int
    val nextTextPosition: Int
        get() = currentTextPosition + 1

    fun next(): Tok?
    fun peekNext(): Tok?
}

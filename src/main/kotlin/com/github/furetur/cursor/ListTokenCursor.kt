package com.github.furetur.cursor

import com.github.furetur.Token

class ListTokenCursor<T : Token>(private val tokens: List<T>) : TokenCursor<T> {
    private var nextIndex = 0
    override val isDone: Boolean
        get() = nextIndex !in tokens.indices

    override fun next(): T? = tokens.getOrNull(nextIndex++)
    override fun peekNext(): T? = tokens.getOrNull(nextIndex)
}

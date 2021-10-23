package com.github.furetur.cursor

class ListTokenCursor<Tok>(private val tokens: List<Tok>) : TokenCursor<Tok> {
    private var nextIndex = 0
    override val isDone: Boolean
        get() = nextIndex !in tokens.indices
    override val currentTextPosition: Int
        get() = nextIndex - 1

    override fun next(): Tok? = tokens.getOrNull(nextIndex++)
    override fun peekNext(): Tok? = tokens.getOrNull(nextIndex)
}

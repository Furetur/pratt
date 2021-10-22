package com.github.furetur

sealed class Expression<Tok> {
    data class Atomic<Tok>(val token: Tok) : Expression<Tok>()
    data class Operator<Tok>(val operator: Tok, val operands: List<Expression<Tok>>) : Expression<Tok>()
}

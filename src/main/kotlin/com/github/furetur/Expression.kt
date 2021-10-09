package com.github.furetur

sealed class Expression<T> {
    data class Atomic<T>(val token: T) : Expression<T>()
    data class Operator<T>(val operator: T, val operands: List<Expression<T>>) : Expression<T>()
}

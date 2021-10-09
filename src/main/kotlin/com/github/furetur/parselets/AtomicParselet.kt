package com.github.furetur.parselets

import com.github.furetur.Expression
import com.github.furetur.Parser
import com.github.furetur.Token

class AtomicParselet<T : Token> : BeginningParselet<T> {
    override fun parse(beginningToken: T, context: Parser<T>.Context): Expression<T> {
        return Expression.Atomic(beginningToken)
    }
}

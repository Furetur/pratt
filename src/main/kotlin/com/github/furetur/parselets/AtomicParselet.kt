package com.github.furetur.parselets

import com.github.furetur.Expression
import com.github.furetur.Parser

class AtomicParselet<Tok, TokType> : BeginningParselet<Tok, TokType> {
    override fun parse(beginningToken: Tok, context: Parser<Tok, TokType>.Context): Expression<Tok> {
        return Expression.Atomic(beginningToken)
    }
}

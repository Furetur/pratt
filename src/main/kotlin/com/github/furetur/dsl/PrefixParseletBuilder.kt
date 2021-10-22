package com.github.furetur.dsl

import com.github.furetur.parselets.BeginningParselet
import com.github.furetur.parselets.PrefixOperatorParselet

class PrefixParseletBuilder<Tok, TokType> : BeginningParseletBuilder<Tok, TokType> {
    private var precedence: Int? = null

    infix fun precedence(value: Int) {
        precedence = value
    }

    override fun build(): BeginningParselet<Tok, TokType> {
        val precedence = precedence
        requireNotNull(precedence) { "Precedence not set" }
        return PrefixOperatorParselet(precedence * 2)
    }
}

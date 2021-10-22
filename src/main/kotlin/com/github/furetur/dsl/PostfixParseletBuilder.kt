package com.github.furetur.dsl

import com.github.furetur.parselets.FollowingParselet
import com.github.furetur.parselets.PostfixOperatorParselet

class PostfixParseletBuilder<Tok, TokType> : FollowingParseletBuilder<Tok, TokType> {
    private var precedence: Int? = null

    infix fun precedence(value: Int) {
        precedence = value
    }

    override fun build(): FollowingParselet<Tok, TokType> {
        val precedence = precedence
        requireNotNull(precedence) { "Precedence not set" }
        return PostfixOperatorParselet(precedence * 2)
    }
}

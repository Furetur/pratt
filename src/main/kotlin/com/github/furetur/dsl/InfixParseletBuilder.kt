package com.github.furetur.dsl

import com.github.furetur.parselets.FollowingParselet
import com.github.furetur.parselets.InfixOperatorParselet

enum class Associativity {
    LEFT, RIGHT
}

class InfixParseletBuilder<Tok, TokType> : FollowingParseletBuilder<Tok, TokType> {
    private var precedence: Int? = null
    private var associativity = Associativity.LEFT

    infix fun precedence(value: Int): InfixParseletBuilder<Tok, TokType> {
        precedence = value
        return this
    }

    infix fun associativity(value: Associativity): InfixParseletBuilder<Tok, TokType> {
        associativity = value
        return this
    }

    override fun build(): FollowingParselet<Tok, TokType> {
        val precedence = precedence
        requireNotNull(precedence) { "Precedence not set" }
        return if (associativity == Associativity.LEFT) {
            InfixOperatorParselet(2 * precedence, 2 * precedence + 1)
        } else {
            InfixOperatorParselet(2 * precedence + 1, 2 * precedence)
        }
    }
}

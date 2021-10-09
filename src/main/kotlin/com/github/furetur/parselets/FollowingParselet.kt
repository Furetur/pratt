package com.github.furetur.parselets

import com.github.furetur.Expression
import com.github.furetur.Parser
import com.github.furetur.Token

interface FollowingParselet<T : Token> {
    val leftBindingPower: Int

    fun parse(firstOperand: Expression<T>, followingToken: T, context: Parser<T>.Context): Expression<T>
}

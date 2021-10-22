package com.github.furetur.parselets

import com.github.furetur.Expression
import com.github.furetur.Parser

interface FollowingParselet<Tok, TokType> {
    val leftBindingPower: Int

    fun parse(firstOperand: Expression<Tok>, followingToken: Tok, context: Parser<Tok, TokType>.Context): Expression<Tok>
}

package com.github.furetur.parselets

import com.github.furetur.Node
import com.github.furetur.Parser

interface FollowingParselet<Tok, TokType> {
    val leftBindingPower: Int

    fun parse(firstOperand: Node<Tok>, followingToken: Tok, context: Parser<Tok, TokType>.Context): Node<Tok>
}

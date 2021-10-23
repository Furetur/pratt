package com.github.furetur.parselets

import com.github.furetur.Node
import com.github.furetur.Parser

/**
 * Parselet for parsing expressions that have the special token in the middle (it *follows* other tokens).
 * For example:
 * infix operator expressions like `1+2` where `+` is the special token,
 * postfix operator expressions like `3!` where `!` is the special token,
 * array element access expressions like `arr[5]` where `[` is the special token,
 * function call expressions like `f(1)` where `(` is the special token,
 * C-like ternary operators like `a ? b : c` where `?` is the special token
 */
interface FollowingParselet<Tok, TokType> {
    val leftBindingPower: Int

    fun parse(firstOperand: Node<Tok>, followingToken: Tok, context: Parser<Tok, TokType>.Context): Node<Tok>
}

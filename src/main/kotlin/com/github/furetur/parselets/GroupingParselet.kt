package com.github.furetur.parselets

import com.github.furetur.Node
import com.github.furetur.Parser

/**
 * Parselet for parsing grouped expressions like parenthesised expressions:
 * expression := '(' expression ')'
 */
class GroupingParselet<Tok, TokType>(private val closingTokenType: TokType) : BeginningParselet<Tok, TokType> {
    override fun parse(beginningToken: Tok, context: Parser<Tok, TokType>.Context): Node<Tok> =
        context.parseExpression(0).also { context.expectNext(closingTokenType) }
}

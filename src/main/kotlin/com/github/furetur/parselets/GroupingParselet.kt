package com.github.furetur.parselets

import com.github.furetur.Expression
import com.github.furetur.Parser
import com.github.furetur.Token
import com.github.furetur.TokenType
import com.github.furetur.cursor.expectNext

class GroupingParselet<T : Token>(private val closingTokenType: TokenType) : BeginningParselet<T> {
    override fun parse(beginningToken: T, context: Parser<T>.Context): Expression<T> =
        context.parseExpression(0).also { context.expectNext(closingTokenType) }
}

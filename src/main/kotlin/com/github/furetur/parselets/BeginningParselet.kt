package com.github.furetur.parselets

import com.github.furetur.Expression
import com.github.furetur.Parser
import com.github.furetur.Token

interface BeginningParselet<T : Token> {
    fun parse(beginningToken: T, context: Parser<T>.Context): Expression<T>
}

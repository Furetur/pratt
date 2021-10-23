package com.github.furetur.parselets

import com.github.furetur.Node
import com.github.furetur.Parser

interface BeginningParselet<Tok, TokType> {
    fun parse(beginningToken: Tok, context: Parser<Tok, TokType>.Context): Node<Tok>
}
